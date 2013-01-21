package org.siemac.metamac.srm.core.code.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.serviceimpl.utils.CodesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
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
    private CodesService                   codesService;

    @Autowired
    private ItemSchemeVersionRepository    itemSchemeVersionRepository;

    @Autowired
    private IdentifiableArtefactRepository identifiableArtefactRepository;

    @Autowired
    @Qualifier("codelistLifeCycle")
    private LifeCycle                      codelistLifeCycle;

    @Autowired
    private SrmValidation                  srmValidation;

    @Autowired
    @Qualifier("codesCopyCallbackMetamac")
    private CodesCopyCallback              codesCopyWithCodesCallback;

    @Autowired
    @Qualifier("codesCopyWithoutCodesCallbackMetamac")
    private CodesCopyCallback              codesCopyWithoutCodesCallback;

    public CodesMetamacServiceImpl() {
    }

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    @Override
    public CodelistVersionMetamac createCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {

        // We must copy replaceTo metadata to set after save codelist, due to flushing
        List<CodelistVersionMetamac> replaceTo = new ArrayList<CodelistVersionMetamac>(codelistVersion.getReplaceToCodelists());
        codelistVersion.removeAllReplaceToCodelists();

        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelist(codelistVersion, null);
        checkCodelistToCreateOrUpdate(ctx, codelistVersion);

        // Fill metadata
        codelistVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        codelistVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        codelistVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        // Save codelist
        codelistVersion = (CodelistVersionMetamac) codesService.createCodelist(ctx, codelistVersion, SrmConstants.VERSION_PATTERN_METAMAC);

        // Fill replaceTo metadata after save entity
        for (CodelistVersionMetamac codelistReplaceTo : replaceTo) {
            codelistVersion.addReplaceToCodelist(codelistReplaceTo);
        }
        codelistVersion = getCodelistVersionMetamacRepository().save(codelistVersion);

        // Add the codelist to the family and variable
        if (codelistVersion.getFamily() != null) {
            codelistVersion.getFamily().addCodelist(codelistVersion);
        }
        if (codelistVersion.getVariable() != null) {
            codelistVersion.getVariable().addCodelist(codelistVersion);
        }

        // Add alphabetical order
        codelistVersion = createCodelistOrderVisualisationAlphabetical(codelistVersion);
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

    @Override
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
        // TODO generar orden para alfabético
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
        codelistVersionMetamac.removeAllReplaceToCodelists();
        codelistVersionMetamac.setDefaultOrderVisualisation(null);

        // Delete
        codesService.deleteCodelist(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac versioningCodelist(ServiceContext ctx, String urnToCopy, Boolean versioningCodes, VersionTypeEnum versionType) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkVersioningCodelist(urnToCopy, versionType, null, null);
        checkCodelistToVersioning(ctx, urnToCopy);

        // Versioning
        CodesCopyCallback callback = versioningCodes == null || versioningCodes ? codesCopyWithCodesCallback : codesCopyWithoutCodesCallback;
        CodelistVersionMetamac codelistNewVersion = (CodelistVersionMetamac) codesService.versioningCodelist(ctx, urnToCopy, versionType, callback);

        // TODO versionar visualizaciones
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
        CodesMetamacInvocationValidator.checkRetrieveByUrn(codeUrn);

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
        code = (CodeMetamac) codesService.createCode(ctx, codelistUrn, code);

        // Add to all visualisations of codelist, at the end of level
        updateOneCodeOrderInLevelPuttingAtTheEnd(codelistVersion.getOrderVisualisations(), code);

        return code;
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
    public void updateCodeParent(ServiceContext ctx, String codeUrn, String parentTargetUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeParent(codeUrn, parentTargetUrn, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
        checkCodelistCanBeModified(codelistVersion);

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        Item parentActual = code.getParent() != null ? code.getParent() : null;
        String parentActualUrn = parentActual != null ? parentActual.getNameableArtefact().getUrn() : null;
        Item parentTarget = parentTargetUrn != null ? retrieveCodeByUrn(ctx, parentTargetUrn) : null;

        if (StringUtils.equals(parentActualUrn, parentTargetUrn)) {
            // nothing
            return;
        }

        // Check target parent is not children of this code
        if (parentTargetUrn != null) {
            checkItemIsNotChildren(ctx, code, parentTargetUrn);
        }

        // Update target parent, adding code
        if (parentTarget == null) {
            code.setParent(null);
            code.getItemSchemeVersion().addItemsFirstLevel(code);
        } else {
            code.setItemSchemeVersionFirstLevel(null);
            parentTarget.addChildren(code);
        }
        code = getCodeMetamacRepository().save(code);

        // Update orders, in previous and new level
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            if (SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
                continue;
            }

            Map<String, CodeOrderVisualisation> mapCodeOrderVisualisationByCodeUrn = SrmServiceUtils.codelistOrderVisualisationToMapByCodeUrn(codelistOrderVisualisation);

            // Update orders of other codes in previous level
            Long codeIndexInLevelBefore = mapCodeOrderVisualisationByCodeUrn.get(code.getNameableArtefact().getUrn()).getCodeIndex();
            List<Item> codesInPreviousLevel = parentActual != null ? parentActual.getChildren() : codelistVersion.getItemsFirstLevel();
            updateAllCodesOrdersInLevelRemovingCode(ctx, codesInPreviousLevel, codeIndexInLevelBefore, mapCodeOrderVisualisationByCodeUrn);

            // Update order in new level
            updateOneCodeOrderInLevelPuttingAtTheEnd(codelistOrderVisualisation, code);
        }
    }

    @Override
    public void updateCodeInOrderVisualisation(ServiceContext ctx, String codeUrn, String codelistOrderVisualisationUrn, Long newCodeIndex) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCodeInOrderVisualisation(codeUrn, codelistOrderVisualisationUrn, newCodeIndex, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
        checkCodelistCanBeModified(codelistVersion);

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByUrn(codelistOrderVisualisationUrn);
        SrmValidationUtils.checkNotAlphabeticalOrderVisualisation(codelistOrderVisualisation);

        Map<String, CodeOrderVisualisation> mapCodeOrderVisualisationByCodeUrn = SrmServiceUtils.codelistOrderVisualisationToMapByCodeUrn(codelistOrderVisualisation);

        // Change order of code
        CodeOrderVisualisation codeOrderVisualisation = mapCodeOrderVisualisationByCodeUrn.get(code.getNameableArtefact().getUrn());
        Long codeIndexInLevelBefore = codeOrderVisualisation.getCodeIndex();
        codeOrderVisualisation.setCodeIndex(newCodeIndex);
        getCodeOrderVisualisationRepository().save(codeOrderVisualisation);

        // Update orders of codes in same level, also checking new order
        List<Item> codesInLevel = getCodesInSameLevel(code);
        updateAllCodesOrdersInLevelChangingOrder(ctx, codesInLevel, code, codeIndexInLevelBefore, newCodeIndex, mapCodeOrderVisualisationByCodeUrn);
        getCodeMetamacRepository().save(code);
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

        // Before delete code, get order of code in each level
        Map<String, CodeOrderVisualisation> codeOrdersInVisualisations = codeOrderInAllVisualisationToMap(urn);

        CodeMetamac code = retrieveCodeByUrn(ctx, urn);
        List<Item> codesInSameLevel = getCodesInSameLevel(code);

        // Delete code
        codesService.deleteCode(ctx, urn);

        // Reorder codes in same level
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            if (SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
                continue;
            }
            Map<String, CodeOrderVisualisation> mapCodeOrderVisualisationByCodeUrn = SrmServiceUtils.codelistOrderVisualisationToMapByCodeUrn(codelistOrderVisualisation);
            updateAllCodesOrdersInLevelRemovingCode(ctx, codesInSameLevel, codeOrdersInVisualisations.get(codelistOrderVisualisation.getNameableArtefact().getUrn()).getCodeIndex(),
                    mapCodeOrderVisualisationByCodeUrn);
        }
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

        // Create
        setCodelistFamilyUrnUnique(codelistFamily);
        return getCodelistFamilyRepository().save(codelistFamily);
    }

    @Override
    public CodelistFamily updateCodelistFamily(ServiceContext ctx, CodelistFamily codelistFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelistFamily(codelistFamily, null);

        // If code has been changed, update URN
        if (codelistFamily.getNameableArtefact().getIsCodeUpdated()) {
            setCodelistFamilyUrnUnique(codelistFamily);
        }
        codelistFamily.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getCodelistFamilyRepository().save(codelistFamily);
    }

    @Override
    public CodelistFamily retrieveCodelistFamilyByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CodelistFamily codelistFamily = retrieveCodelistFamilyByUrn(urn);
        return codelistFamily;
    }

    @Override
    public PagedResult<CodelistFamily> findCodelistFamiliesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistFamily.class).distinctRoot().build();
        }
        PagedResult<CodelistFamily> codelistFamilyPagedResult = getCodelistFamilyRepository().findByCondition(conditions, pagingParameter);
        return codelistFamilyPagedResult;
    }

    @Override
    public void deleteCodelistFamily(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);

        // Delete
        CodelistFamily codelistFamilyToDelete = retrieveCodelistFamilyByUrn(urn);

        // Delete association with codelists
        // TODO Is necessary to check the codelist status?
        codelistFamilyToDelete.removeAllCodelists();
        getCodelistFamilyRepository().save(codelistFamilyToDelete);

        getCodelistFamilyRepository().delete(codelistFamilyToDelete);
    }

    @Override
    public void addCodelistsToCodelistFamily(ServiceContext ctx, List<String> codelistUrns, String codelistFamilyUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkAddCodelistsToCodelistFamily(codelistUrns, codelistFamilyUrn, null);
        // TODO Is necessary to check the codelist status?

        CodelistFamily codelistFamily = retrieveCodelistFamilyByUrn(codelistFamilyUrn);

        // Update codelists
        for (String codelistUrn : codelistUrns) {
            CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
            if (codelistVersion.getFamily() != null) {
                codelistVersion.getFamily().removeCodelist(codelistVersion);
            }
            codelistFamily.addCodelist(codelistVersion);
            getCodelistVersionMetamacRepository().save(codelistVersion);
        }
    }

    @Override
    public void removeCodelistFromCodelistFamily(ServiceContext ctx, String codelistUrn, String codelistFamilyUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRemoveCodelistFromCodelistFamily(codelistUrn, codelistFamilyUrn, null);
        // TODO Is necessary to check the codelist status?

        CodelistFamily codelistFamily = retrieveCodelistFamilyByUrn(codelistFamilyUrn);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);

        // Do not remove the codelist if it has not been associated with the family previously
        if (!SrmServiceUtils.isCodelistInList(codelistVersion.getMaintainableArtefact().getUrn(), codelistFamily.getCodelists())) {
            return;
        }

        // Remove codelist from family
        codelistFamily.removeCodelist(codelistVersion);
        getCodelistFamilyRepository().save(codelistFamily);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE FAMILIES
    // ------------------------------------------------------------------------------------

    @Override
    public VariableFamily createVariableFamily(ServiceContext ctx, VariableFamily variableFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateVariableFamily(variableFamily, null);
        setVariableFamilyUrnUnique(variableFamily);

        // Create
        return getVariableFamilyRepository().save(variableFamily);
    }

    @Override
    public VariableFamily updateVariableFamily(ServiceContext ctx, VariableFamily variableFamily) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariableFamily(variableFamily, null);

        // If code has been changed, update URN
        if (variableFamily.getNameableArtefact().getIsCodeUpdated()) {
            setVariableFamilyUrnUnique(variableFamily);
        }
        variableFamily.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getVariableFamilyRepository().save(variableFamily);
    }

    @Override
    public VariableFamily retrieveVariableFamilyByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        VariableFamily variableFamily = retrieveVariableFamilyByUrn(urn);
        return variableFamily;
    }

    @Override
    public void deleteVariableFamily(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);

        VariableFamily variableFamilyToDelete = retrieveVariableFamilyByUrn(urn);
        // Check variables of family to delete has more families, because family of variable is required (in exception, say only one variable)
        for (Variable variable : variableFamilyToDelete.getVariables()) {
            if (variable.getFamilies().size() == 1) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ONLY_IN_ONE_FAMILY).withMessageParameters(variable.getNameableArtefact().getUrn(), urn)
                        .build();
            }
        }

        // Delete associations with variables
        variableFamilyToDelete.removeAllVariables();
        getVariableFamilyRepository().save(variableFamilyToDelete);

        // Delete
        getVariableFamilyRepository().delete(variableFamilyToDelete);
    }

    @Override
    public PagedResult<VariableFamily> findVariableFamiliesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

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
    public Variable createVariable(ServiceContext ctx, Variable variable) throws MetamacException {

        // We must copy replaceTo metadata to set after save variable, due to flushing
        List<Variable> replaceTo = new ArrayList<Variable>(variable.getReplaceToVariables());
        variable.removeAllReplaceToVariables();

        // Validation
        CodesMetamacInvocationValidator.checkCreateVariable(variable, null);
        checkVariableToCreateOrUpdate(ctx, variable);
        setVariableUrnUnique(variable);

        // Create
        variable = getVariableRepository().save(variable);

        // Fill replaceTo metadata after save entity
        for (Variable variableReplaceTo : replaceTo) {
            variable.addReplaceToVariable(variableReplaceTo);
        }
        variable = getVariableRepository().save(variable);
        return variable;
    }

    @Override
    public Variable updateVariable(ServiceContext ctx, Variable variable) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariable(variable, null);
        checkVariableToCreateOrUpdate(ctx, variable);

        // If code has been changed, update URN
        if (variable.getNameableArtefact().getIsCodeUpdated()) {
            setVariableUrnUnique(variable);
        }
        variable.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getVariableRepository().save(variable);
    }

    @Override
    public Variable retrieveVariableByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        Variable variable = retrieveVariableByUrn(urn);
        return variable;
    }

    @Override
    public PagedResult<Variable> findVariablesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(Variable.class).distinctRoot().build();
        }
        PagedResult<Variable> variablePagedResult = getVariableRepository().findByCondition(conditions, pagingParameter);
        return variablePagedResult;
    }

    @Override
    public void deleteVariable(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);

        Variable variableToDelete = retrieveVariableByUrn(urn);

        // Check variable has not concepts, variable elements neither codelists (in exception, say only one)
        if (CollectionUtils.isNotEmpty(variableToDelete.getCodelists())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_WITH_RELATIONS)
                    .withMessageParameters(variableToDelete.getNameableArtefact().getUrn(), variableToDelete.getCodelists().get(0).getMaintainableArtefact().getUrn()).build();
        }
        if (CollectionUtils.isNotEmpty(variableToDelete.getConcepts())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_WITH_RELATIONS)
                    .withMessageParameters(variableToDelete.getNameableArtefact().getUrn(), variableToDelete.getConcepts().get(0).getNameableArtefact().getUrn()).build();
        }
        if (CollectionUtils.isNotEmpty(variableToDelete.getVariableElements())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_WITH_RELATIONS)
                    .withMessageParameters(variableToDelete.getNameableArtefact().getUrn(), variableToDelete.getVariableElements().get(0).getNameableArtefact().getUrn()).build();
        }
        // TODO ¿Se puede eliminar una variable que reemplazó o fue reemplazada por otra?

        // Delete associations with variable families
        variableToDelete.removeAllFamilies();
        getVariableRepository().save(variableToDelete);

        // Delete
        getVariableRepository().delete(variableToDelete);
    }

    @Override
    public void addVariablesToVariableFamily(ServiceContext ctx, List<String> variablesUrn, String familyUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkAddVariablesToFamily(variablesUrn, familyUrn, null);

        VariableFamily family = retrieveVariableFamilyByUrn(familyUrn);
        for (String variableUrn : variablesUrn) {
            Variable variable = retrieveVariableByUrn(variableUrn);

            // Do not add the variable if it has been associated with the family previously
            if (SrmServiceUtils.isVariableInList(variable.getNameableArtefact().getUrn(), family.getVariables())) {
                continue;
            }
            // Add variable to family
            family.addVariable(variable);
        }
        getVariableFamilyRepository().save(family);
    }

    @Override
    public void removeVariableFromVariableFamily(ServiceContext ctx, String variableUrn, String familyUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRemoveVariableFromFamily(variableUrn, familyUrn, null);

        VariableFamily family = retrieveVariableFamilyByUrn(familyUrn);
        Variable variable = retrieveVariableByUrn(variableUrn);

        // Do not remove the variable if it has not been associated with the family previously
        if (!SrmServiceUtils.isVariableInList(variable.getNameableArtefact().getUrn(), family.getVariables())) {
            return;
        }

        // Check variable has more families
        if (variable.getFamilies().size() == 1) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ONLY_IN_ONE_FAMILY).withMessageParameters(variableUrn, familyUrn).build();
        }

        // Remove variable from family
        family.removeVariable(variable);
        getVariableFamilyRepository().save(family);
    }

    // ------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ------------------------------------------------------------------------------------

    @Override
    public VariableElement createVariableElement(ServiceContext ctx, VariableElement variableElement) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateVariableElement(variableElement, null);
        setVariableElementUrnUnique(variableElement);

        // Create
        variableElement = getVariableElementRepository().save(variableElement);
        return variableElement;
    }

    @Override
    public VariableElement updateVariableElement(ServiceContext ctx, VariableElement variableElement) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariableElement(variableElement, null);

        // If code has been changed, update URN
        if (variableElement.getNameableArtefact().getIsCodeUpdated()) {
            setVariableElementUrnUnique(variableElement);
        }
        variableElement.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getVariableElementRepository().save(variableElement);
    }

    @Override
    public VariableElement retrieveVariableElementByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        VariableElement variableElement = retrieveVariableElementByUrn(urn);
        return variableElement;
    }

    @Override
    public PagedResult<VariableElement> findVariableElementsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).distinctRoot().build();
        }
        PagedResult<VariableElement> variableElementPagedResult = getVariableElementRepository().findByCondition(conditions, pagingParameter);
        return variableElementPagedResult;
    }

    @Override
    public void deleteVariableElement(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);

        VariableElement variableElementToDelete = retrieveVariableElementByUrn(urn);

        // Check variableElement has not codes
        if (CollectionUtils.isNotEmpty(variableElementToDelete.getCodes())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_WITH_RELATIONS)
                    .withMessageParameters(variableElementToDelete.getNameableArtefact().getUrn(), variableElementToDelete.getCodes().get(0).getNameableArtefact().getUrn()).build(); // say one
        }
        // TODO Un elemento de variable puede eliminarse si no tiene operaciones de segregación o fusión
        // TODO ¿Se puede eliminar una variableElement que reemplazó o fue reemplazada por otra?

        // Delete
        getVariableElementRepository().delete(variableElementToDelete);
    }

    @Override
    public void addVariableElementsToVariable(ServiceContext ctx, List<String> variableElementsUrn, String variableUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkAddVariableElementsToVariable(variableElementsUrn, variableUrn, null);

        Variable variable = retrieveVariableByUrn(variableUrn);
        for (String variableElementUrn : variableElementsUrn) {
            VariableElement variableElement = retrieveVariableElementByUrn(variableElementUrn);

            // Do not add the variableElement if it has been associated with the variable previously
            if (SrmServiceUtils.isVariableElementInList(variableElement.getNameableArtefact().getUrn(), variable.getVariableElements())) {
                continue;
            }
            // Add variableElement to variable
            variable.addVariableElement(variableElement);
        }
        getVariableRepository().save(variable);
    }

    // ------------------------------------------------------------------------------------
    // CODELIST VISUALISATIONS
    // ------------------------------------------------------------------------------------
    @Override
    public CodelistOrderVisualisation createCodelistOrderVisualisation(ServiceContext ctx, String codelistUrn, CodelistOrderVisualisation codelistOrderVisualisation) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelistOrderVisualisation(codelistUrn, codelistOrderVisualisation, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        checkCodelistCanBeModified(codelistVersion);

        // Create order visualisation
        codelistOrderVisualisation.setCodelistVersion(codelistVersion);
        // Add all codes, without hierarchy, but with default order by level
        addAllCodesOrderVisualisationOfLevel(codelistOrderVisualisation, codelistVersion.getItemsFirstLevel());

        // Create
        setCodelistOrderVisualisationUrnUnique(codelistOrderVisualisation);
        return getCodelistOrderVisualisationRepository().save(codelistOrderVisualisation);
    }

    @Override
    public CodelistOrderVisualisation retrieveCodelistOrderVisualisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByUrn(urn);
        return codelistOrderVisualisation;
    }

    @Override
    public CodelistOrderVisualisation updateCodelistOrderVisualisation(ServiceContext ctx, CodelistOrderVisualisation codelistOrderVisualisation) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelistOrderVisualisation(codelistOrderVisualisation, null);
        checkCodelistCanBeModified(codelistOrderVisualisation.getCodelistVersion());;

        // If code has been changed, update URN
        if (codelistOrderVisualisation.getNameableArtefact().getIsCodeUpdated()) {
            if (codelistOrderVisualisation.getNameableArtefact().getUrn().endsWith("." + SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE);
            }
            setCodelistOrderVisualisationUrnUnique(codelistOrderVisualisation);
        }
        codelistOrderVisualisation.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getCodelistOrderVisualisationRepository().save(codelistOrderVisualisation);
    }

    @Override
    public void deleteCodelistOrderVisualisation(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        CodelistOrderVisualisation codelistOrderVisualisationToDelete = retrieveCodelistOrderVisualisationByUrn(urn);
        SrmValidationUtils.checkNotAlphabeticalOrderVisualisation(codelistOrderVisualisationToDelete);

        // Check codelist and default visualisation
        CodelistVersionMetamac codelistVersion = codelistOrderVisualisationToDelete.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);
        if (codelistVersion.getDefaultOrderVisualisation() != null && codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn().equals(urn)) {
            codelistVersion.setDefaultOrderVisualisation(null);
            getCodelistVersionMetamacRepository().save(codelistVersion);
        }

        // Delete
        getCodelistOrderVisualisationRepository().delete(codelistOrderVisualisationToDelete);
    }

    @Override
    public List<CodelistOrderVisualisation> retrieveCodelistOrderVisualisationsByCodelist(ServiceContext ctx, String codelistUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(codelistUrn);

        // Retrieve
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        return codelistVersion.getOrderVisualisations();
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

        // Maintainer
        srmValidation.checkMaintainer(ctx, codelist.getMaintainableArtefact(), codelist.getMaintainableArtefact().getIsImported());

        // Visualisations
        if (codelist.getDefaultOrderVisualisation() != null) {
            // Check it is visualisation of this codelist
            if (!codelist.getDefaultOrderVisualisation().getCodelistVersion().getMaintainableArtefact().getUrn().equals(codelist.getMaintainableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT)
                        .withMessageParameters(ServiceExceptionParameters.CODELIST_DEFAULT_ORDER_VISUALISATION).build();
            }
        }
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

    private CodelistFamily retrieveCodelistFamilyByUrn(String urn) throws MetamacException {
        CodelistFamily codelistFamily = getCodelistFamilyRepository().findByUrn(urn);
        if (codelistFamily == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build(); // TODO quitar lo de "IDENTIFICABLE" y
                                                                                                                                                                 // dejar sólo artefact?
        }
        return codelistFamily;
    }

    private VariableFamily retrieveVariableFamilyByUrn(String urn) throws MetamacException {
        VariableFamily variableFamily = getVariableFamilyRepository().findByUrn(urn);
        if (variableFamily == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return variableFamily;
    }

    private Variable retrieveVariableByUrn(String urn) throws MetamacException {
        Variable variable = getVariableRepository().findByUrn(urn);
        if (variable == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return variable;
    }

    private VariableElement retrieveVariableElementByUrn(String urn) throws MetamacException {
        VariableElement variableElement = getVariableElementRepository().findByUrn(urn);
        if (variableElement == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return variableElement;
    }

    private CodelistOrderVisualisation retrieveCodelistOrderVisualisationByUrn(String urn) throws MetamacException {
        CodelistOrderVisualisation codelistOrderVisualisation = getCodelistOrderVisualisationRepository().findByUrn(urn);
        if (codelistOrderVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return codelistOrderVisualisation;
    }

    private void checkCodelistCanBeModified(CodelistVersionMetamac codelistVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(codelistVersion.getLifeCycleMetadata(), codelistVersion.getMaintainableArtefact().getUrn());
    }

    /**
     * Common validations to create or update a variable
     */
    private void checkVariableToCreateOrUpdate(ServiceContext ctx, Variable variable) throws MetamacException {
        // Check variable doesnt replace self
        if (SrmServiceUtils.isVariableInList(variable.getNameableArtefact().getUrn(), variable.getReplaceToVariables())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_CAN_NOT_REPLACE_ITSELF).withMessageParameters(variable.getNameableArtefact().getUrn()).build();
        }
    }

    /**
     * Generate urn, check it is unique and set to codelist family. Set also urnProvider
     */
    private void setCodelistFamilyUrnUnique(CodelistFamily codelistFamily) throws MetamacException {
        String urn = GeneratorUrnUtils.generateCodelistFamilyUrn(codelistFamily);
        identifiableArtefactRepository.checkUrnUnique(urn, codelistFamily.getNameableArtefact().getId());

        codelistFamily.getNameableArtefact().setUrn(urn);
        codelistFamily.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to variable family. Set also urnProvider
     */
    private void setVariableFamilyUrnUnique(VariableFamily variableFamily) throws MetamacException {
        String urn = GeneratorUrnUtils.generateVariableFamilyUrn(variableFamily);
        identifiableArtefactRepository.checkUrnUnique(urn, variableFamily.getNameableArtefact().getId());

        variableFamily.getNameableArtefact().setUrn(urn);
        variableFamily.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to variable. Set also urnProvider
     */
    private void setVariableUrnUnique(Variable variable) throws MetamacException {
        String urn = GeneratorUrnUtils.generateVariableUrn(variable);
        identifiableArtefactRepository.checkUrnUnique(urn, variable.getNameableArtefact().getId());

        variable.getNameableArtefact().setUrn(urn);
        variable.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to variable element. Set also urnProvider
     */
    private void setVariableElementUrnUnique(VariableElement variableElement) throws MetamacException {
        String urn = GeneratorUrnUtils.generateVariableElementUrn(variableElement);
        identifiableArtefactRepository.checkUrnUnique(urn, variableElement.getNameableArtefact().getId());

        variableElement.getNameableArtefact().setUrn(urn);
        variableElement.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to variable element. Set also urnProvider
     */
    private void setCodelistOrderVisualisationUrnUnique(CodelistOrderVisualisation codelistOrderVisualisation) throws MetamacException {
        String urn = GeneratorUrnUtils.generateCodelistOrderVisualisationUrn(codelistOrderVisualisation);
        identifiableArtefactRepository.checkUrnUnique(urn, codelistOrderVisualisation.getNameableArtefact().getId());

        codelistOrderVisualisation.getNameableArtefact().setUrn(urn);
        codelistOrderVisualisation.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Add all codes to codelist visualisation, without hierarchy, but with default order by level
     */
    private void addAllCodesOrderVisualisationOfLevel(CodelistOrderVisualisation codelistOrderVisualisation, List<Item> codesInLevel) {
        int i = 0;
        for (Item item : codesInLevel) {
            CodeMetamac code = (CodeMetamac) item;
            CodeOrderVisualisation codeOrderVisualisation = new CodeOrderVisualisation();
            codeOrderVisualisation.setCode(code);
            codeOrderVisualisation.setCodelistVisualisation(codelistOrderVisualisation);
            codeOrderVisualisation.setCodeIndex(Long.valueOf(i));
            codelistOrderVisualisation.addCode(codeOrderVisualisation);
            i++;

            // Children
            addAllCodesOrderVisualisationOfLevel(codelistOrderVisualisation, code.getChildren());
        }
    }

    private void updateAllCodesOrdersInLevelRemovingCode(ServiceContext ctx, List<Item> codesInLevel, Long codeIndexOfCodeToRemove,
            Map<String, CodeOrderVisualisation> mapCodeOrderVisualisationByCodeUrn) throws MetamacException {
        for (Item code : codesInLevel) {
            CodeOrderVisualisation codeOrderVisualisationInLevel = mapCodeOrderVisualisationByCodeUrn.get(code.getNameableArtefact().getUrn());
            if (codeOrderVisualisationInLevel.getCodeIndex() > codeIndexOfCodeToRemove) {
                codeOrderVisualisationInLevel.setCodeIndex(codeOrderVisualisationInLevel.getCodeIndex() - 1);
                getCodeOrderVisualisationRepository().save(codeOrderVisualisationInLevel);
            }
        }
    }

    private void updateAllCodesOrdersInLevelChangingOrder(ServiceContext ctx, List<Item> codesInLevel, Item codeToChangeOrder, Long orderBeforeUpdate, Long orderAfterUpdate,
            Map<String, CodeOrderVisualisation> mapCodeOrderVisualisationByCodeUrn) throws MetamacException {

        // Checks orders
        if (orderAfterUpdate > codesInLevel.size()) {
            throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.CODE_ORDER_VISUALISATION_INDEX);
        }

        // Update orders
        for (Item codeInLevel : codesInLevel) {
            if (codeInLevel.getId().equals(codeToChangeOrder.getId())) {
                continue;
            }
            CodeOrderVisualisation codeOrderVisualisationInLevel = mapCodeOrderVisualisationByCodeUrn.get(codeInLevel.getNameableArtefact().getUrn());
            Long orderIndexInLevel = codeOrderVisualisationInLevel.getCodeIndex();
            if (orderAfterUpdate < orderBeforeUpdate) {
                if (orderIndexInLevel >= orderAfterUpdate && orderIndexInLevel < orderBeforeUpdate) {
                    codeOrderVisualisationInLevel.setCodeIndex(orderIndexInLevel + 1);
                    getCodeOrderVisualisationRepository().save(codeOrderVisualisationInLevel);
                }
            } else if (orderAfterUpdate > orderBeforeUpdate) {
                if (orderIndexInLevel > orderBeforeUpdate && orderIndexInLevel <= orderAfterUpdate) {
                    codeOrderVisualisationInLevel.setCodeIndex(orderIndexInLevel - 1);
                    getCodeOrderVisualisationRepository().save(codeOrderVisualisationInLevel);
                }
            }
        }
    }

    private void updateOneCodeOrderInLevelPuttingAtTheEnd(List<CodelistOrderVisualisation> codelistOrderVisualisations, CodeMetamac code) {
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistOrderVisualisations) {
            if (SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
                return;
            }
            updateOneCodeOrderInLevelPuttingAtTheEnd(codelistOrderVisualisation, code);
        }
    }

    private void updateOneCodeOrderInLevelPuttingAtTheEnd(CodelistOrderVisualisation codelistOrderVisualisation, CodeMetamac code) {
        if (SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
            return;
        }
        CodeOrderVisualisation codeOrderVisualisation = SrmServiceUtils.filterCodeOrderVisualisationsByCode(codelistOrderVisualisation.getCodes(), code.getNameableArtefact().getUrn());
        if (codeOrderVisualisation == null) {
            codeOrderVisualisation = new CodeOrderVisualisation();
            codeOrderVisualisation.setCode(code);
            codelistOrderVisualisation.addCode(codeOrderVisualisation);
        }
        codeOrderVisualisation.setCodeIndex(Long.valueOf(getCodesInSameLevel(code).size()) - 1); // start in 0. note: code is already added to list
        getCodeOrderVisualisationRepository().save(codeOrderVisualisation);
    }

    private List<Item> getCodesInSameLevel(CodeMetamac code) {
        return code.getParent() != null ? code.getParent().getChildren() : code.getItemSchemeVersionFirstLevel().getItemsFirstLevel();
    }

    private Map<String, CodeOrderVisualisation> codeOrderInAllVisualisationToMap(String urn) {
        Map<String, CodeOrderVisualisation> codeIndexInVisualisations = new HashMap<String, CodeOrderVisualisation>();
        List<CodeOrderVisualisation> codeOrderVisualisations = getCodeOrderVisualisationRepository().findByCodeUrn(urn);
        for (CodeOrderVisualisation codeOrderVisualisation : codeOrderVisualisations) {
            codeIndexInVisualisations.put(codeOrderVisualisation.getCodelistVisualisation().getNameableArtefact().getUrn(), codeOrderVisualisation);
        }
        return codeIndexInVisualisations;
    }

    /**
     * We can not move a code to its child
     */
    private void checkItemIsNotChildren(ServiceContext ctx, Item item, String parentTargetUrn) throws MetamacException {
        Item parentTarget = retrieveCodeByUrn(ctx, parentTargetUrn);
        while (parentTarget != null) {
            if (parentTarget.getId().equals(item.getId())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.ITEM_PARENT);
            }
            parentTarget = parentTarget.getParent();
        }
    }

    private CodelistVersionMetamac createCodelistOrderVisualisationAlphabetical(CodelistVersionMetamac codelistVersion) throws MetamacException {
        CodelistOrderVisualisation alphabeticalOrderVisualisation = new CodelistOrderVisualisation();
        alphabeticalOrderVisualisation.setNameableArtefact(new NameableArtefact());
        alphabeticalOrderVisualisation.getNameableArtefact().setCode(SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE);
        // NameableArtefact
        InternationalString name = new InternationalString();
        name.addText(new LocalisedString("es", "Orden alfabético"));
        name.addText(new LocalisedString("en", "Alphabetical order"));
        name.addText(new LocalisedString("pt", "Ordem alfabética"));
        alphabeticalOrderVisualisation.getNameableArtefact().setName(name);

        // note: do not add codes, they will be added when publish
        codelistVersion.addOrderVisualisation(alphabeticalOrderVisualisation);

        setCodelistOrderVisualisationUrnUnique(alphabeticalOrderVisualisation);
        return getCodelistVersionMetamacRepository().save(codelistVersion);
    }

}
