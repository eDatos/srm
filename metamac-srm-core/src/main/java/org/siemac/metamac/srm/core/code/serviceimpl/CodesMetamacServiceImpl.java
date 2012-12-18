package org.siemac.metamac.srm.core.code.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.serviceimpl.utils.CodesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.utils.CodesDoCopyUtils.CodesCopyCallback;

/**
 * Implementation of CodesMetamacService.
 */
@Service("codesMetamacService")
public class CodesMetamacServiceImpl extends CodesMetamacServiceImplBase {

    @Autowired
    private CodesService                codesService;

    @Autowired
    private OrganisationsMetamacService organisationsService;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Autowired
    @Qualifier("codelistLifeCycle")
    private LifeCycle                   codelistLifeCycle;

    @Autowired
    @Qualifier("codesCopyCallbackMetamac")
    private CodesCopyCallback           codesCopyCallback;

    public CodesMetamacServiceImpl() {
    }

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    @Override
    public CodelistVersionMetamac createCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelist(codelistVersion, null);
        checkCodelistToCreateOrUpdate(ctx, codelistVersion);

        // Fill metadata
        codelistVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        codelistVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        codelistVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        // Save codelist
        codelistVersion = (CodelistVersionMetamac) codesService.createCodelist(ctx, codelistVersion, SrmConstants.VERSION_PATTERN_METAMAC);

        // Add the codelist to the family
        CodelistFamily family = codelistVersion.getFamily();
        if (family != null) {
            family.addCodelist(codelistVersion);
        }

        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac updateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelist(codelistVersion, null);
        checkCodelistToCreateOrUpdate(ctx, codelistVersion);

        // Save codelist
        return (CodelistVersionMetamac) codesService.updateCodelist(ctx, codelistVersion);
    }

    public CodelistVersionMetamac retrieveCodelistByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codesService.retrieveCodelistByUrn(ctx, urn);
    }

    @Override
    public List<CodelistVersionMetamac> retrieveCodelistVersions(ServiceContext ctx, String urn) throws MetamacException {
        // Retrieve codelistVersions
        List<CodelistVersion> codelistVersions = codesService.retrieveCodelistVersions(ctx, urn);

        // Typecast to CodelistVersionMetamac
        return codelistVersionsToCodelistVersionsMetamac(codelistVersions);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<CodelistVersion> codelistVersionPagedResult = codesService.findCodelistsByCondition(ctx, conditions, pagingParameter);
        return pagedResultCodelistVersionToMetamac(codelistVersionPagedResult);
    }

    @Override
    public CodelistVersionMetamac sendCodelistToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac sendCodelistToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac rejectCodelistProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac rejectCodelistDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac publishInternallyCodelist(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.publishInternally(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac publishExternallyCodelist(ServiceContext ctx, String urn) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public void deleteCodelist(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CodelistVersionMetamac codelistVersionMetamac = retrieveCodelistByUrn(ctx, urn);
        checkCodelistCanBeModified(codelistVersionMetamac);

        // Delete
        codesService.deleteCodelist(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac versioningCodelist(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkVersioningCodelist(urnToCopy, versionType, null, null);
        checkCodelistToVersioning(ctx, urnToCopy);

        // Versioning
        CodelistVersionMetamac codelistNewVersion = (CodelistVersionMetamac) codesService.versioningCodelist(ctx, urnToCopy, versionType, codesCopyCallback);

        return codelistNewVersion;
    }

    @Override
    public CodelistVersionMetamac endCodelistValidity(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkEndValidity(urn, null);

        // Retrieve version in specific procStatus
        CodelistVersionMetamac codelistVersion = getCodelistVersionMetamacRepository().retrieveCodelistVersionByProcStatus(urn, new ProcStatusEnum[]{ProcStatusEnum.EXTERNALLY_PUBLISHED});

        // End validity
        codelistVersion = (CodelistVersionMetamac) codesService.endCodelistValidity(ctx, urn, null);

        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac retrieveCodelistByCodeUrn(ServiceContext ctx, String codeUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveCodelistByCodeUrn(codeUrn, null);

        // Retrieve
        CodelistVersionMetamac codelistVersion = getCodelistVersionMetamacRepository().findByCode(codeUrn);
        if (codelistVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(codeUrn).build();
        }
        return codelistVersion;
    }

    // ------------------------------------------------------------------------------------
    // CODES
    // ------------------------------------------------------------------------------------

    @Override
    public CodeMetamac createCode(ServiceContext ctx, String codelistUrn, CodeMetamac code) throws MetamacException {
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);

        // Validation
        CodesMetamacInvocationValidator.checkCreateCode(codelistVersion, code, null);
        checkCodeToCreateOrUpdate(ctx, codelistVersion, code);

        // Save code
        return (CodeMetamac) codesService.createCode(ctx, codelistUrn, code);
    }

    @Override
    public CodeMetamac updateCode(ServiceContext ctx, CodeMetamac code) throws MetamacException {
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, code.getNameableArtefact().getUrn());

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCode(codelistVersion, code, null);
        checkCodeToCreateOrUpdate(ctx, codelistVersion, code);

        return (CodeMetamac) codesService.updateCode(ctx, code);
    }

    @Override
    public CodeMetamac retrieveCodeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return (CodeMetamac) codesService.retrieveCodeByUrn(ctx, urn);
    }

    @Override
    public PagedResult<CodeMetamac> findCodesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Code> codesPagedResult = codesService.findCodesByCondition(ctx, conditions, pagingParameter);
        return pagedResultCodeToMetamac(codesPagedResult);
    }

    @Override
    public void deleteCode(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, urn);
        checkCodelistCanBeModified(codelistVersion);

        // Delete
        codesService.deleteCode(ctx, urn);
    }

    @Override
    public List<CodeMetamac> retrieveCodesByCodelistUrn(ServiceContext ctx, String codelistUrn) throws MetamacException {
        // Retrieve
        List<Code> codes = codesService.retrieveCodesByCodelistUrn(ctx, codelistUrn);

        // Typecast
        List<CodeMetamac> codesMetamac = codesToCodeMetamac(codes);
        return codesMetamac;
    }

    // ------------------------------------------------------------------------------------
    // CODELIST FAMILIES
    // ------------------------------------------------------------------------------------

    @Override
    public CodelistFamily createCodelistFamily(ServiceContext ctx, CodelistFamily codelistFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelistFamily(codelistFamily, null);
        validateCodelistFamilyIdentifierUnique(ctx, codelistFamily);

        // Create
        return getCodelistFamilyRepository().save(codelistFamily);
    }

    @Override
    public CodelistFamily updateCodelistFamily(ServiceContext ctx, CodelistFamily codelistFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelistFamily(codelistFamily, null);
        validateCodelistFamilyIdentifierUnique(ctx, codelistFamily);

        // Create
        return getCodelistFamilyRepository().save(codelistFamily);
    }

    @Override
    public CodelistFamily retrieveCodelistFamilyByIdentifier(ServiceContext ctx, String identifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveCodelistFamilyByIdentifier(identifier, null);

        // Retrieve
        CodelistFamily codelistFamily = retrieveCodelistFamilyByIdentifier(identifier);
        return codelistFamily;
    }

    @Override
    public PagedResult<CodelistFamily> findCodelistFamiliesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindCodelistFamiliesByCondition(conditions, pagingParameter, null);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).distinctRoot().build();
        }
        PagedResult<CodelistFamily> codelistFamilyPagedResult = getCodelistFamilyRepository().findByCondition(conditions, pagingParameter);
        return codelistFamilyPagedResult;
    }

    @Override
    public void deleteCodelistFamily(ServiceContext ctx, String identifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteCodelistFamily(identifier, null);

        // Delete
        CodelistFamily codelistFamilyToDelete = retrieveCodelistFamilyByIdentifier(identifier);

        // Delete association with codelists
        // TODO Is necessary to check the codelist status?
        codelistFamilyToDelete.removeAllCodelists();
        getCodelistFamilyRepository().save(codelistFamilyToDelete);

        getCodelistFamilyRepository().delete(codelistFamilyToDelete);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------------------

    @Override
    public VariableFamily createVariableFamily(ServiceContext ctx, VariableFamily variableFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateVariableFamily(variableFamily, null);
        validateVariableFamilyIdentifierUnique(ctx, variableFamily);

        // Create
        return getVariableFamilyRepository().save(variableFamily);
    }

    @Override
    public VariableFamily updateVariableFamily(ServiceContext ctx, VariableFamily variableFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariableFamily(variableFamily, null);
        validateVariableFamilyIdentifierUnique(ctx, variableFamily);

        // Create
        return getVariableFamilyRepository().save(variableFamily);
    }

    @Override
    public VariableFamily retrieveVariableFamilyByIdentifier(ServiceContext ctx, String identifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveVariableFamilyByIdentifier(identifier, null);

        // Retrieve
        VariableFamily variableFamily = retrieveVariableFamilyByIdentifier(identifier);
        return variableFamily;
    }

    @Override
    public void deleteVariableFamily(ServiceContext ctx, String identifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteVariableFamily(identifier, null);

        VariableFamily variableFamilyToDelete = retrieveVariableFamilyByIdentifier(identifier);

        // Delete associations with variables
        variableFamilyToDelete.removeAllVariables();
        getVariableFamilyRepository().save(variableFamilyToDelete);

        // Delete
        getVariableFamilyRepository().delete(variableFamilyToDelete);
    }

    @Override
    public PagedResult<VariableFamily> findVariableFamiliesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindVariableFamiliesByCondition(conditions, pagingParameter, null);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).distinctRoot().build();
        }
        PagedResult<VariableFamily> variableFamilyPagedResult = getVariableFamilyRepository().findByCondition(conditions, pagingParameter);
        return variableFamilyPagedResult;
    }

    // ------------------------------------------------------------------------------------
    // VARIABLES
    // ------------------------------------------------------------------------------------

    @Override
    public Variable createVariable(ServiceContext ctx, List<String> familyIdentifiers, Variable variable) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateVariable(familyIdentifiers, variable, null);
        validateVariableIdentifierUnique(ctx, variable);

        // Add variable to the families
        for (String familyIdentifier : familyIdentifiers) {
            VariableFamily family = retrieveVariableFamilyByIdentifier(familyIdentifier);
            variable.addFamily(family);
        }

        // Create
        return getVariableRepository().save(variable);
    }

    @Override
    public Variable updateVariable(ServiceContext ctx, Variable variable) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariable(variable, null);
        validateVariableIdentifierUnique(ctx, variable);

        // Create
        return getVariableRepository().save(variable);
    }

    @Override
    public Variable retrieveVariableByIdentifier(ServiceContext ctx, String identifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveVariableByIdentifier(identifier, null);

        // Retrieve
        Variable variable = retrieveVariableByIdentifier(identifier);
        return variable;
    }

    @Override
    public PagedResult<Variable> findVariablesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindVariablesByCondition(conditions, pagingParameter, null);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).distinctRoot().build();
        }
        PagedResult<Variable> variablePagedResult = getVariableRepository().findByCondition(conditions, pagingParameter);
        return variablePagedResult;
    }

    @Override
    public void deleteVariable(ServiceContext ctx, String identifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteVariable(identifier, null);

        Variable variableToDelete = retrieveVariableByIdentifier(identifier);

        // Delete associations with variable families
        variableToDelete.removeAllFamilies();

        // Delete associations with codelists
        // TODO Check that the associated codelists are no published
        variableToDelete.removeAllCodelists();

        getVariableRepository().save(variableToDelete);

        // Delete
        getVariableRepository().delete(variableToDelete);
    }

    @Override
    public void addVariableToFamily(ServiceContext ctx, String variableIdentifier, String familyIdentifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkAddVariableToFamily(variableIdentifier, familyIdentifier, null);

        VariableFamily family = retrieveVariableFamilyByIdentifier(familyIdentifier);
        Variable variable = retrieveVariableByIdentifier(variableIdentifier);

        // Do not add the variable if it has been associated with the family previously
        if (SrmValidationUtils.isVariableInList(variable.getIdentifier(), family.getVariables())) {
            return;
        }

        // Add variable to family
        family.addVariable(variable);
        getVariableFamilyRepository().save(family);
    }

    @Override
    public void removeVariableFromFamily(ServiceContext ctx, String variableIdentifier, String familyIdentifier) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRemoveVariableFromFamily(variableIdentifier, familyIdentifier, null);

        VariableFamily family = retrieveVariableFamilyByIdentifier(familyIdentifier);
        Variable variable = retrieveVariableByIdentifier(variableIdentifier);

        // Do not remove the variable if it has not been associated with the family previously
        if (!SrmValidationUtils.isVariableInList(variable.getIdentifier(), family.getVariables())) {
            return;
        }

        // Remove variable from family
        family.removeVariable(variable);
        getVariableFamilyRepository().save(family);
    }

    // ------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------------------

    /**
     * Typecast to Metamac type
     */
    private List<CodelistVersionMetamac> codelistVersionsToCodelistVersionsMetamac(List<CodelistVersion> sources) {
        List<CodelistVersionMetamac> targets = new ArrayList<CodelistVersionMetamac>();
        for (ItemSchemeVersion source : sources) {
            targets.add((CodelistVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CodelistVersionMetamac> pagedResultCodelistVersionToMetamac(PagedResult<CodelistVersion> source) {
        List<CodelistVersionMetamac> codelistVersionsMetamac = codelistVersionsToCodelistVersionsMetamac(source.getValues());
        return new PagedResult<CodelistVersionMetamac>(codelistVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Common validations to create or update a codelist
     */
    private void checkCodelistToCreateOrUpdate(ServiceContext ctx, CodelistVersionMetamac codelist) throws MetamacException {

        if (codelist.getId() != null) {
            // Proc status
            checkCodelistCanBeModified(codelist);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(codelist.getMaintainableArtefact());
        }

        // Maintainer internally or externally published
        String maintainerUrn = codelist.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn();
        OrganisationSchemeVersionMetamac maintainerOrganisationSchemeVersion = organisationsService.retrieveOrganisationSchemeByOrganisationUrn(ctx, maintainerUrn);
        SrmValidationUtils.checkArtefactInternallyOrExternallyPublished(maintainerOrganisationSchemeVersion.getMaintainableArtefact().getUrn(),
                maintainerOrganisationSchemeVersion.getLifeCycleMetadata());
    }

    private void checkCodelistToVersioning(ServiceContext ctx, String urnToCopy) throws MetamacException {
        CodelistVersionMetamac codelistVersionToCopy = retrieveCodelistByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(codelistVersionToCopy.getLifeCycleMetadata(), urnToCopy);
        // Check does not exist any version 'no final'
        ItemSchemeVersion codelistVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinal(codelistVersionToCopy.getItemScheme().getId());
        if (codelistVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED)
                    .withMessageParameters(codelistVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    /**
     * Common validations to create or update a code
     */
    private void checkCodeToCreateOrUpdate(ServiceContext ctx, CodelistVersionMetamac codelistVersion, Code code) throws MetamacException {
        checkCodelistCanBeModified(codelistVersion);
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CodeMetamac> pagedResultCodeToMetamac(PagedResult<Code> source) {
        List<CodeMetamac> codesMetamac = codesToCodeMetamac(source.getValues());
        return new PagedResult<CodeMetamac>(codesMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(), source.getAdditionalResultRows());
    }

    /**
     * Typecast to Metamac type
     */
    private List<CodeMetamac> codesToCodeMetamac(List<Code> sources) {
        List<CodeMetamac> targets = new ArrayList<CodeMetamac>();
        for (Item source : sources) {
            targets.add((CodeMetamac) source);
        }
        return targets;
    }

    private CodelistFamily retrieveCodelistFamilyByIdentifier(String identifier) throws MetamacException {
        CodelistFamily codelistFamily = getCodelistFamilyRepository().findByIdentifier(identifier);
        if (codelistFamily == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CODELIST_FAMILY_NOT_FOUND).withMessageParameters(identifier).build();
        }
        return codelistFamily;
    }

    private VariableFamily retrieveVariableFamilyByIdentifier(String identifier) throws MetamacException {
        VariableFamily variableFamily = getVariableFamilyRepository().findByIdentifier(identifier);
        if (variableFamily == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND).withMessageParameters(identifier).build();
        }
        return variableFamily;
    }

    private Variable retrieveVariableByIdentifier(String identifier) throws MetamacException {
        Variable variable = getVariableRepository().findByIdentifier(identifier);
        if (variable == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_NOT_FOUND).withMessageParameters(identifier).build();
        }
        return variable;
    }

    /**
     * Checks if codelist family identifier is unique
     */
    private void validateCodelistFamilyIdentifierUnique(ServiceContext ctx, CodelistFamily codelistFamily) throws MetamacException {
        String familyIdentifier = codelistFamily.getIdentifier();
        Long familyId = codelistFamily != null ? codelistFamily.getId() : null;

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).withProperty(CodelistFamilyProperties.identifier()).ignoreCaseEq(familyIdentifier)
                .distinctRoot().build();

        if (familyId != null) {
            ConditionalCriteria notOwnEntity = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).not().withProperty(CodelistFamilyProperties.id()).eq(familyId).buildSingle();
            conditions.add(notOwnEntity);
        }

        PagedResult<CodelistFamily> families = getCodelistFamilyRepository().findByCondition(conditions, PagingParameter.noLimits());
        if (families != null && families.getValues().size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CODELIST_FAMILY_DUPLICATED_IDENTIFIER).withMessageParameters(familyIdentifier).build();
        }
    }

    /**
     * Checks if variable family identifier is unique
     */
    private void validateVariableFamilyIdentifierUnique(ServiceContext ctx, VariableFamily variableFamily) throws MetamacException {
        String familyIdentifier = variableFamily.getIdentifier();
        Long familyId = variableFamily != null ? variableFamily.getId() : null;

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).withProperty(VariableFamilyProperties.identifier()).ignoreCaseEq(familyIdentifier)
                .distinctRoot().build();

        if (familyId != null) {
            ConditionalCriteria notOwnEntity = ConditionalCriteriaBuilder.criteriaFor(VariableFamily.class).not().withProperty(VariableFamilyProperties.id()).eq(familyId).buildSingle();
            conditions.add(notOwnEntity);
        }

        PagedResult<VariableFamily> families = getVariableFamilyRepository().findByCondition(conditions, PagingParameter.noLimits());
        if (families != null && families.getValues().size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_FAMILY_DUPLICATED_IDENTIFIER).withMessageParameters(familyIdentifier).build();
        }
    }

    /**
     * Checks if variable identifier is unique
     */
    private void validateVariableIdentifierUnique(ServiceContext ctx, Variable variable) throws MetamacException {
        String variableIdentifier = variable.getIdentifier();
        Long variableId = variable != null ? variable.getId() : null;

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).withProperty(VariableProperties.identifier()).ignoreCaseEq(variableIdentifier).distinctRoot()
                .build();

        if (variableId != null) {
            ConditionalCriteria notOwnEntity = ConditionalCriteriaBuilder.criteriaFor(Variable.class).not().withProperty(VariableProperties.id()).eq(variableId).buildSingle();
            conditions.add(notOwnEntity);
        }

        PagedResult<Variable> variables = getVariableRepository().findByCondition(conditions, PagingParameter.noLimits());
        if (variables != null && variables.getValues().size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_DUPLICATED_IDENTIFIER).withMessageParameters(variableIdentifier).build();
        }
    }

    private void checkCodelistCanBeModified(CodelistVersionMetamac codelistVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(codelistVersion.getLifeCycleMetadata(), codelistVersion.getMaintainableArtefact().getUrn());
    }
}
