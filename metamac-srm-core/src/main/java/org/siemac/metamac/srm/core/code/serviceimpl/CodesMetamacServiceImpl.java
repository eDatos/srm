package org.siemac.metamac.srm.core.code.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopyHierarchy;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
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

import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseVersioningCopyUtils;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;
import com.arte.statistic.sdmx.srm.core.code.serviceimpl.utils.CodesVersioningCopyUtils.CodesVersioningCopyCallback;

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
    @Qualifier("codesVersioningCopyCallbackMetamac")
    private CodesVersioningCopyCallback    codesVersioningCopyWithCodesCallback;

    @Autowired
    @Qualifier("codesVersioningCopyWithoutCodesCallbackMetamac")
    private CodesVersioningCopyCallback    codesVersioningCopyWithoutCodesCallback;

    public CodesMetamacServiceImpl() {
    }

    // ------------------------------------------------------------------------------------
    // CODELISTS
    // ------------------------------------------------------------------------------------

    @Override
    public CodelistVersionMetamac createCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {

        // Fill and validate Codelist
        preCreateCodelist(ctx, codelistVersion);

        // In creation, 'replaceTo' metadata must be copied to set after save codelist, due to flushing
        List<CodelistVersionMetamac> replaceTo = new ArrayList<CodelistVersionMetamac>(codelistVersion.getReplaceToCodelists());
        codelistVersion.removeAllReplaceToCodelists();

        // Save codelist
        codelistVersion = (CodelistVersionMetamac) codesService.createCodelist(ctx, codelistVersion, SrmConstants.VERSION_PATTERN_METAMAC);

        // Post create actions
        codelistVersion = createCodelistOrderVisualisationAlphabetical(ctx, codelistVersion); // Add alphabetical order
        codelistVersion = createCodelistOpennessVisualisationAllOpened(ctx, codelistVersion); // Add all opened visualization
        // Execute common actions after creation
        postCreateCodelist(ctx, codelistVersion, replaceTo);

        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac preCreateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelist(codelistVersion, null);
        checkCodelistToCreateOrUpdate(ctx, codelistVersion);

        // Fill metadata
        codelistVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        codelistVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        codelistVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);

        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac postCreateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion, List<CodelistVersionMetamac> replaceTo) throws MetamacException {
        // Fill replaceTo metadata after save entity, fill replacedBy metadata too
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
        return codelistVersion;
    }

    // Note: variable can not be changed after codelist is published, because other restrictions could be violated (see ConceptsMetamacService.checkConceptEnumeratedRepresentation)
    @Override
    public CodelistVersionMetamac updateCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelist(codelistVersion, null);
        checkCodelistToCreateOrUpdate(ctx, codelistVersion);

        // Fill replaceBy metadata
        for (CodelistVersionMetamac replaceTo : codelistVersion.getReplaceToCodelists()) {
            replaceTo.setReplacedByCodelist(codelistVersion);
        }

        // if variable is changed, remove variable elements of codes
        if (codelistVersion.getIsVariableUpdated()) {
            getCodeMetamacRepository().clearCodesVariableElementByCodelist(codelistVersion);
        }

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
    public CodelistVersionMetamac publishInternallyCodelist(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        return (CodelistVersionMetamac) codelistLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
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
        codelistVersionMetamac.removeAllReplaceToCodelists(); // codelist can be deleted although it replaces to another codelist
        codelistVersionMetamac.setDefaultOrderVisualisation(null);
        codelistVersionMetamac.setDefaultOpennessVisualisation(null);
        codesService.deleteCodelist(ctx, urn);
    }

    @Override
    public CodelistVersionMetamac versioningCodelist(ServiceContext ctx, String urnToCopy, Boolean versioningCodes, VersionTypeEnum versionType) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkVersioningCodelist(urnToCopy, versionType, null, null);
        checkCodelistToVersioning(ctx, urnToCopy);

        // Versioning
        CodesVersioningCopyCallback callback = versioningCodes == null || versioningCodes ? codesVersioningCopyWithCodesCallback : codesVersioningCopyWithoutCodesCallback;
        CodelistVersionMetamac codelistVersionToCopy = retrieveCodelistByUrn(ctx, urnToCopy);
        CodelistVersionMetamac codelistNewVersion = (CodelistVersionMetamac) codesService.versioningCodelist(ctx, urnToCopy, versionType, callback);

        // Versioning visualisations // TODO cuando se implementen las versiones dummy, decidir dónde ubicar este código
        codelistNewVersion = versioningCodelistOrderVisualisations(ctx, codelistVersionToCopy, codelistNewVersion);
        codelistNewVersion = versioningCodelistOpennessVisualisations(ctx, codelistVersionToCopy, codelistNewVersion);
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

        preCreateCode(ctx, codelistVersion, code);
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersion);

        // Save code
        code = (CodeMetamac) codesService.createCode(ctx, codelistUrn, code);
        // Add to all visualisations of codelist
        code = initVisualisationsToCodeCreated(codelistVersion, code);
        return code;
    }

    @Override
    public void copyCodesInCodelist(ServiceContext ctx, String codelistTargetUrn, String parentTargetUrn, List<CodeToCopyHierarchy> codesToCopy) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCopyCodesInCodelist(codelistTargetUrn, parentTargetUrn, codesToCopy, null);
        if (CollectionUtils.isEmpty(codesToCopy)) {
            return;
        }
        CodelistVersionMetamac codelistVersionTarget = retrieveCodelistByUrn(ctx, codelistTargetUrn);
        checkCodelistCanBeModified(codelistVersionTarget);
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersionTarget);

        CodeMetamac parentTarget = null;
        if (parentTargetUrn != null) {
            parentTarget = retrieveCodeByUrn(ctx, parentTargetUrn);
            if (!parentTarget.getItemSchemeVersion().getMaintainableArtefact().getUrn().equals(codelistVersionTarget.getMaintainableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.ITEM_PARENT).build();
            }
        }

        // Get codelist source from one code, to check all codes belong to this codelist and to check variable
        CodelistVersionMetamac codelistVersionSource = retrieveCodelistByCodeUrn(ctx, codesToCopy.get(0).getUrn());

        // Copy
        for (CodeToCopyHierarchy codeToCopyHierarchy : codesToCopy) {
            copyCodeInCodelist(ctx, codelistVersionSource, codelistVersionTarget, parentTarget, codeToCopyHierarchy);
        }
    }

    @Override
    public CodeMetamac preCreateCode(ServiceContext ctx, CodelistVersionMetamac codelistVersion, CodeMetamac code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkCreateCode(codelistVersion, code, null);
        checkCodeToCreateOrUpdate(ctx, codelistVersion, code);

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
    public CodeMetamac updateCodeVariableElement(ServiceContext ctx, String codeUrn, String variableElementUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeVariableElement(codeUrn, variableElementUrn, null);
        // Note: It is not necessary to check the codelist status. Variable element can be modified although the codelist is published

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        if (variableElementUrn == null) {
            // Delete
            if (code.getVariableElement() != null) {
                code.getVariableElement().removeCode(code);
            }
        } else {
            // Add
            VariableElement variableElement = retrieveVariableElementByUrn(variableElementUrn);
            CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
            checkCodeVariableElement(codelistVersion, variableElement);

            if (code.getVariableElement() != null) {
                code.getVariableElement().removeCode(code);
            }
            variableElement.addCode(code);
            code.setShortName(null); // reset name
        }
        return getCodeMetamacRepository().save(code);
    }

    @Override
    public void updateCodeParent(ServiceContext ctx, String codeUrn, String parentTargetUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeParent(codeUrn, parentTargetUrn, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
        checkCodelistCanBeModified(codelistVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersion);

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        Item parentActual = code.getParent() != null ? code.getParent() : null;
        String parentActualUrn = parentActual != null ? parentActual.getNameableArtefact().getUrn() : null;
        CodeMetamac parentTarget = parentTargetUrn != null ? retrieveCodeByUrn(ctx, parentTargetUrn) : null;

        if (StringUtils.equals(parentActualUrn, parentTargetUrn)) {
            // nothing
            return;
        }

        // Check target parent is not children of this code
        if (parentTargetUrn != null) {
            checkItemIsNotChildren(ctx, code, parentTargetUrn);
        }

        // Update target parent
        if (parentTarget == null) {
            code.setParent(null);
            code.setItemSchemeVersionFirstLevel(codelistVersion);
        } else {
            code.setParent(parentTarget);
            code.setItemSchemeVersionFirstLevel(null);
        }

        // Update orders, in previous and new level
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        Integer orderInLastPosition = null; // no avoid innecessary extra queries
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            // remove from previous level
            reorderCodesDeletingOneCode(codelistVersion, codelistOrderVisualisation, parentActual, code);
            // add to new level
            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
                setCodeOrderInAlphabeticalPositionAndReorderCodesInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                orderInLastPosition = setCodeOrderInLastPositionInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code, orderInLastPosition);
            }
        }
        // Note: openness configuration remains inmutable
        code = getCodeMetamacRepository().save(code);
    }

    @Override
    public void updateCodeInOrderVisualisation(ServiceContext ctx, String codeUrn, String codelistOrderVisualisationUrn, Integer newCodeIndex) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeInOrderVisualisation(codeUrn, codelistOrderVisualisationUrn, newCodeIndex, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByCodeUrn(ctx, codeUrn);
        checkCodelistCanBeModified(codelistVersion);

        CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByUrn(codelistOrderVisualisationUrn);
        SrmValidationUtils.checkNotAlphabeticalOrderVisualisation(codelistOrderVisualisation);

        CodeMetamac code = retrieveCodeByUrn(ctx, codeUrn);
        // Reorder codes in same level
        reorderCodesDeletingOneCode(codelistVersion, codelistOrderVisualisation, code.getParent(), code);
        reorderCodesAddingOneCodeInMiddle(codelistVersion, codelistOrderVisualisation, code, newCodeIndex);
        // Update code
        SrmServiceUtils.setCodeOrder(code, codelistOrderVisualisation.getColumnIndex(), newCodeIndex);
        getCodeMetamacRepository().save(code);
    }

    @Override
    public void updateCodesInOpennessVisualisation(ServiceContext ctx, String opennessVisualisation, Map<String, Boolean> openness) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodeInOpennessVisualisation(opennessVisualisation, openness, null);
        CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByUrn(opennessVisualisation);
        CodelistVersionMetamac codelistVersion = codelistOpennessVisualisation.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);
        SrmValidationUtils.checkNotOpenedOpennessVisualisation(codelistOpennessVisualisation);
        // Note: In repository method 'updateCodeOpennessColumn' check code belongs to this codelist

        // Update codes
        Integer columnIndex = codelistOpennessVisualisation.getColumnIndex();
        for (String codeUrn : openness.keySet()) {
            getCodeMetamacRepository().updateCodeOpennessColumn(codelistVersion, codeUrn, columnIndex, openness.get(codeUrn));
        }
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
        srmValidation.checkItemsStructureCanBeModified(ctx, codelistVersion);

        CodeMetamac code = retrieveCodeByUrn(ctx, urn);
        Item parent = code.getParent();
        // Before delete code, update order of other codes in level
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            reorderCodesDeletingOneCode(codelistVersion, codelistOrderVisualisation, parent, code);
        }
        // Openness visualisations of other codes in level are not affected

        // Delete code
        codesService.deleteCode(ctx, urn);
    }

    @Override
    public List<CodeMetamacVisualisationResult> retrieveCodesByCodelistUrn(ServiceContext ctx, String codelistUrn, String locale, String orderVisualisationUrn, String opennessVisualisationUrn)
            throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveCodesByCodelistUrn(codelistUrn, locale, orderVisualisationUrn, opennessVisualisationUrn, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        Integer orderColumnIndex = null;
        if (orderVisualisationUrn != null) {
            CodelistOrderVisualisation codelistOrderVisualisation = retrieveCodelistOrderVisualisationByUrn(ctx, orderVisualisationUrn);
            orderColumnIndex = codelistOrderVisualisation.getColumnIndex();
        }
        Integer opennessColumnIndex = null;
        if (opennessVisualisationUrn != null) {
            CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByUrn(ctx, opennessVisualisationUrn);
            opennessColumnIndex = codelistOpennessVisualisation.getColumnIndex();
        }

        // Retrieve
        return getCodeMetamacRepository().findCodesByCodelistUnordered(codelistVersion.getId(), locale, orderColumnIndex, opennessColumnIndex);
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
        // Note: It is not necessary to check the codelist status. Family can be modified although the codelist is published
        codelistFamilyToDelete.removeAllCodelists();
        getCodelistFamilyRepository().save(codelistFamilyToDelete);

        getCodelistFamilyRepository().delete(codelistFamilyToDelete);
    }

    @Override
    public void addCodelistsToCodelistFamily(ServiceContext ctx, List<String> codelistUrns, String codelistFamilyUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkAddCodelistsToCodelistFamily(codelistUrns, codelistFamilyUrn, null);
        // Note: It is not necessary to check the codelist status. Family can be modified although the codelist is published

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
        // Note: It is not necessary to check the codelist status. Family can be modified although the codelist is published

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

        // Validation
        CodesMetamacInvocationValidator.checkCreateVariable(variable, null);
        checkVariableToCreateOrUpdate(ctx, variable);

        // In creation, 'replaceTo' metadata must be copied to set after save variable, due to flushing
        List<Variable> replaceTo = new ArrayList<Variable>(variable.getReplaceToVariables());
        variable.removeAllReplaceToVariables();

        // Create
        setVariableUrnUnique(variable);
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
            for (VariableElement variableElement : variable.getVariableElements()) {
                setVariableElementUrnUnique(variable, variableElement);
            }
        }
        variable.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Fill replaceBy metadata
        for (Variable variableReplaceTo : variable.getReplaceToVariables()) {
            variableReplaceTo.setReplacedByVariable(variable);
        }

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
        // Check variable has not concepts, variable elements neither codelists (in exception, say only one)
        Variable variableToDelete = retrieveVariableByUrn(urn);
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

        // Delete associations with variable families
        variableToDelete.removeAllFamilies();
        getVariableRepository().save(variableToDelete);

        // Delete
        variableToDelete.removeAllReplaceToVariables(); // variable can be deleted although it replaces to another variable
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
        checkVariableElementToCreateOrUpdate(ctx, variableElement);

        // In creation, 'replaceTo' metadata must be copied to set after save variable, due to flushing
        List<VariableElement> replaceTo = new ArrayList<VariableElement>(variableElement.getReplaceToVariableElements());
        variableElement.removeAllReplaceToVariableElements();

        // Create
        setVariableElementUrnUnique(variableElement.getVariable(), variableElement);
        variableElement = getVariableElementRepository().save(variableElement);

        // Fill replaceTo metadata after save entity
        for (VariableElement variableElementReplaceTo : replaceTo) {
            variableElement.addReplaceToVariableElement(variableElementReplaceTo);
        }
        variableElement = getVariableElementRepository().save(variableElement);
        return variableElement;
    }

    @Override
    public VariableElement updateVariableElement(ServiceContext ctx, VariableElement variableElement) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateVariableElement(variableElement, null);
        checkVariableElementToCreateOrUpdate(ctx, variableElement);

        // If code has been changed, update URN
        if (variableElement.getNameableArtefact().getIsCodeUpdated()) {
            setVariableElementUrnUnique(variableElement.getVariable(), variableElement);
        }
        variableElement.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Fill replaceBy metadata
        for (VariableElement replaceTo : variableElement.getReplaceToVariableElements()) {
            replaceTo.setReplacedByVariableElement(variableElement);
        }

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

    @SuppressWarnings("unchecked")
    @Override
    public PagedResult<VariableElement> findVariableElementsForCodesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter, String codelistUrn)
            throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkFindVariableElementsForCodesByCondition(conditions, pagingParameter, codelistUrn, null);

        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        if (codelistVersion.getVariable() == null) {
            return SrmServiceUtils.pagedResultZeroResults(pagingParameter);
        }

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).distinctRoot().build();
        }
        // Same variable
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(VariableElement.class).withProperty(VariableElementProperties.variable().nameableArtefact().urn())
                .eq(codelistVersion.getVariable().getNameableArtefact().getUrn()).buildSingle());
        PagedResult<VariableElement> variableElementPagedResult = getVariableElementRepository().findByCondition(conditions, pagingParameter);
        return variableElementPagedResult;
    }

    @Override
    public void deleteVariableElement(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        // Check variableElement has not codes
        VariableElement variableElementToDelete = retrieveVariableElementByUrn(urn);
        if (CollectionUtils.isNotEmpty(variableElementToDelete.getCodes())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_WITH_RELATIONS)
                    .withMessageParameters(variableElementToDelete.getNameableArtefact().getUrn(), variableElementToDelete.getCodes().get(0).getNameableArtefact().getUrn()).build(); // say one
        }
        // Check variableElement has not operations
        List<VariableElementOperation> variableElementsOperations = getVariableElementOperationRepository().findByVariableElementUrn(urn);
        if (CollectionUtils.isNotEmpty(variableElementsOperations)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_WITH_OPERATIONS)
                    .withMessageParameters(variableElementToDelete.getNameableArtefact().getUrn()).build();
        }

        // Delete
        variableElementToDelete.removeAllReplaceToVariableElements(); // variable element can be deleted although it replaces to another variable element
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

    @Override
    public VariableElementOperation createVariableElementFusionOperation(ServiceContext ctx, List<String> sources, String target) throws MetamacException {
        return createVariableElementOperationCommon(VariableElementOperationTypeEnum.FUSION, sources, Arrays.asList(target));
    }

    @Override
    public VariableElementOperation createVariableElementSegregationOperation(ServiceContext ctx, String source, List<String> targets) throws MetamacException {
        return createVariableElementOperationCommon(VariableElementOperationTypeEnum.SEGREGATION, Arrays.asList(source), targets);
    }

    @Override
    public VariableElementOperation retrieveVariableElementOperationByCode(ServiceContext ctx, String code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveVariableElementOperationByCode(code, null);

        // Retrieve
        VariableElementOperation variableElementOperation = retrieveVariableElementOperationByCode(code);
        return variableElementOperation;
    }

    @Override
    public void deleteVariableElementOperation(ServiceContext ctx, String code) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteVariableElementOperation(code, null);

        // Delete
        VariableElementOperation variableElementOperationToDelete = retrieveVariableElementOperationByCode(code);
        getVariableElementOperationRepository().delete(variableElementOperationToDelete);
    }

    @Override
    public List<VariableElementOperation> retrieveVariableElementsOperationsByVariable(ServiceContext ctx, String variableUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(variableUrn);

        // Retrieve
        Variable variable = retrieveVariableByUrn(variableUrn);
        return variable.getVariableElementsOperations();
    }

    @Override
    public List<VariableElementOperation> retrieveVariableElementsOperationsByVariableElement(ServiceContext ctx, String variableElementUrn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(variableElementUrn);

        // Retrieve
        return getVariableElementOperationRepository().findByVariableElementUrn(variableElementUrn);
    }

    /**
     * Get a column available in Code to store orders
     */
    private Integer getCodelistOrderVisualisationAvailable(CodelistVersionMetamac codelistVersion) throws MetamacException {
        int index = -1;
        for (int i = 1; i <= SrmConstants.CODELIST_ORDER_VISUALISATION_MAXIMUM_NUMBER; i++) {
            boolean alreadyExists = false;
            for (CodelistOrderVisualisation codelistOrderVisualisationPrevious : codelistVersion.getOrderVisualisations()) {
                if (codelistOrderVisualisationPrevious.getColumnIndex().intValue() == i) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new MetamacException(ServiceExceptionType.CODELIST_ORDER_VISUALISATION_MAXIMUM_REACHED, codelistVersion.getMaintainableArtefact().getUrn());
        }
        return Integer.valueOf(index);
    }

    /**
     * Get a column available in Code to store opennesss
     */
    private Integer getCodelistOpennessVisualisationAvailable(CodelistVersionMetamac codelistVersion) throws MetamacException {
        int index = -1;
        for (int i = 1; i <= SrmConstants.CODELIST_OPENNESS_VISUALISATION_MAXIMUM_NUMBER; i++) {
            boolean alreadyExists = false;
            for (CodelistOpennessVisualisation codelistOpennessVisualisationPrevious : codelistVersion.getOpennessVisualisations()) {
                if (codelistOpennessVisualisationPrevious.getColumnIndex().intValue() == i) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new MetamacException(ServiceExceptionType.CODELIST_OPENNESS_VISUALISATION_MAXIMUM_REACHED, codelistVersion.getMaintainableArtefact().getUrn());
        }
        return Integer.valueOf(index);
    }

    // ------------------------------------------------------------------------------------
    // CODELIST ORDER VISUALISATIONS
    // ------------------------------------------------------------------------------------
    @Override
    public CodelistOrderVisualisation createCodelistOrderVisualisation(ServiceContext ctx, String codelistUrn, CodelistOrderVisualisation codelistOrderVisualisation) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelistOrderVisualisation(codelistUrn, codelistOrderVisualisation, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        checkCodelistCanBeModified(codelistVersion);

        // Create order visualisation
        codelistOrderVisualisation.setColumnIndex(getCodelistOrderVisualisationAvailable(codelistVersion));
        codelistOrderVisualisation.setCodelistVersion(codelistVersion);

        // Assign default order to all codes, by level (copy from alphabetic)
        getCodeMetamacRepository().copyCodesOrderColumn(codelistVersion, SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX, codelistOrderVisualisation.getColumnIndex());

        // Create
        setCodelistOrderVisualisationUrnUnique(codelistVersion, codelistOrderVisualisation);
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
        checkCodelistCanBeModified(codelistOrderVisualisation.getCodelistVersion());

        // If code has been changed, update URN
        if (codelistOrderVisualisation.getNameableArtefact().getIsCodeUpdated()) {
            if (codelistOrderVisualisation.getNameableArtefact().getUrn().endsWith("." + SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE);
            }
            setCodelistOrderVisualisationUrnUnique(codelistOrderVisualisation.getCodelistVersion(), codelistOrderVisualisation);
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

        // Check codelist
        CodelistVersionMetamac codelistVersion = codelistOrderVisualisationToDelete.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);
        // Clear if it is default visualisation
        if (codelistVersion.getDefaultOrderVisualisation() != null && codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn().equals(urn)) {
            codelistVersion.setDefaultOrderVisualisation(null);
            getCodelistVersionMetamacRepository().save(codelistVersion);
        }

        // Delete
        getCodelistOrderVisualisationRepository().delete(codelistOrderVisualisationToDelete);
        // Clear ordenations in code columns
        getCodeMetamacRepository().clearCodesOrderColumn(codelistVersion, codelistOrderVisualisationToDelete.getColumnIndex());
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
    // CODELIST OPENNESS VISUALISATIONS
    // ------------------------------------------------------------------------------------
    @Override
    public CodelistOpennessVisualisation createCodelistOpennessVisualisation(ServiceContext ctx, String codelistUrn, CodelistOpennessVisualisation codelistOpennessVisualisation)
            throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkCreateCodelistOpennessVisualisation(codelistUrn, codelistOpennessVisualisation, null);
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        checkCodelistCanBeModified(codelistVersion);

        // Create openness visualisation
        codelistOpennessVisualisation.setColumnIndex(getCodelistOpennessVisualisationAvailable(codelistVersion));
        codelistOpennessVisualisation.setCodelistVersion(codelistVersion);

        // Assign default openness to all codes
        getCodeMetamacRepository().updateCodesOpennessColumnByCodelist(codelistVersion, codelistOpennessVisualisation.getColumnIndex(), SrmConstants.CODELIST_OPENNESS_VISUALISATION_DEFAULT_VALUE);

        // Create
        setCodelistOpennessVisualisationUrnUnique(codelistVersion, codelistOpennessVisualisation);
        return getCodelistOpennessVisualisationRepository().save(codelistOpennessVisualisation);
    }

    @Override
    public CodelistOpennessVisualisation retrieveCodelistOpennessVisualisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CodelistOpennessVisualisation codelistOpennessVisualisation = retrieveCodelistOpennessVisualisationByUrn(urn);
        return codelistOpennessVisualisation;
    }

    @Override
    public CodelistOpennessVisualisation updateCodelistOpennessVisualisation(ServiceContext ctx, CodelistOpennessVisualisation codelistOpennessVisualisation) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkUpdateCodelistOpennessVisualisation(codelistOpennessVisualisation, null);
        checkCodelistCanBeModified(codelistOpennessVisualisation.getCodelistVersion());

        // If code has been changed, update URN
        if (codelistOpennessVisualisation.getNameableArtefact().getIsCodeUpdated()) {
            if (codelistOpennessVisualisation.getNameableArtefact().getUrn().endsWith("." + SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE);
            }
            setCodelistOpennessVisualisationUrnUnique(codelistOpennessVisualisation.getCodelistVersion(), codelistOpennessVisualisation);
        }
        codelistOpennessVisualisation.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getCodelistOpennessVisualisationRepository().save(codelistOpennessVisualisation);
    }

    @Override
    public void deleteCodelistOpennessVisualisation(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CodesMetamacInvocationValidator.checkDeleteArtefact(urn);
        CodelistOpennessVisualisation codelistOpennessVisualisationToDelete = retrieveCodelistOpennessVisualisationByUrn(urn);
        SrmValidationUtils.checkNotOpenedOpennessVisualisation(codelistOpennessVisualisationToDelete);

        // Check codelist
        CodelistVersionMetamac codelistVersion = codelistOpennessVisualisationToDelete.getCodelistVersion();
        checkCodelistCanBeModified(codelistVersion);
        // Clear if it is default visualisation
        if (codelistVersion.getDefaultOpennessVisualisation() != null && codelistVersion.getDefaultOpennessVisualisation().getNameableArtefact().getUrn().equals(urn)) {
            codelistVersion.setDefaultOpennessVisualisation(null);
            getCodelistVersionMetamacRepository().save(codelistVersion);
        }

        // Delete
        getCodelistOpennessVisualisationRepository().delete(codelistOpennessVisualisationToDelete);
        // Clear configurations in code columns
        getCodeMetamacRepository().clearCodesOpennessColumnByCodelist(codelistVersion, codelistOpennessVisualisationToDelete.getColumnIndex());
    }

    @Override
    public List<CodelistOpennessVisualisation> retrieveCodelistOpennessVisualisationsByCodelist(ServiceContext ctx, String codelistUrn) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkRetrieveByUrn(codelistUrn);

        // Retrieve
        CodelistVersionMetamac codelistVersion = retrieveCodelistByUrn(ctx, codelistUrn);
        return codelistVersion.getOpennessVisualisations();
    }

    // ------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // ------------------------------------------------------------------------------------

    /**
     * Typecast to Metamac type
     */
    private List<CodelistVersionMetamac> codelistVersionsToCodelistVersionsMetamac(List<CodelistVersion> sources) {
        List<CodelistVersionMetamac> targets = new ArrayList<CodelistVersionMetamac>(sources.size());
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

        // Visualisations (check they belong to this codelist)
        if (codelist.getDefaultOrderVisualisation() != null
                && !codelist.getDefaultOrderVisualisation().getCodelistVersion().getMaintainableArtefact().getUrn().equals(codelist.getMaintainableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CODELIST_DEFAULT_ORDER_VISUALISATION)
                    .build();
        }
        if (codelist.getDefaultOpennessVisualisation() != null
                && !codelist.getDefaultOpennessVisualisation().getCodelistVersion().getMaintainableArtefact().getUrn().equals(codelist.getMaintainableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT)
                    .withMessageParameters(ServiceExceptionParameters.CODELIST_DEFAULT_OPENNESS_VISUALISATION).build();
        }

        // Check codelist doesnt replace self
        if (SrmServiceUtils.isCodelistInList(codelist.getMaintainableArtefact().getUrn(), codelist.getReplaceToCodelists())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_CAN_NOT_REPLACE_ITSELF).withMessageParameters(codelist.getMaintainableArtefact().getUrn()).build();
        }

        // Check replaceTo metadata
        for (CodelistVersionMetamac replaceTo : codelist.getReplaceToCodelists()) {
            // Check codelist is externally published
            SrmValidationUtils.checkArtefactProcStatus(replaceTo.getLifeCycleMetadata(), replaceTo.getMaintainableArtefact().getUrn(), ProcStatusEnum.EXTERNALLY_PUBLISHED);
            // Check any codelist was not already replaced by another codelist
            if (replaceTo.getReplacedByCodelist() != null && !replaceTo.getReplacedByCodelist().getMaintainableArtefact().getUrn().equals(codelist.getMaintainableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_IS_ALREADY_REPLACED).withMessageParameters(replaceTo.getMaintainableArtefact().getUrn())
                        .build();
            }
        }
    }

    private void checkCodelistToVersioning(ServiceContext ctx, String urnToCopy) throws MetamacException {
        CodelistVersionMetamac codelistVersionToCopy = retrieveCodelistByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(codelistVersionToCopy.getMaintainableArtefact(), codelistVersionToCopy.getLifeCycleMetadata());
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
    private void checkCodeToCreateOrUpdate(ServiceContext ctx, CodelistVersionMetamac codelistVersion, CodeMetamac code) throws MetamacException {
        checkCodelistCanBeModified(codelistVersion);
        checkCodeVariableElement(codelistVersion, code.getVariableElement());
    }

    /**
     * Checks variable element belongs to same variable of codelist
     */
    private void checkCodeVariableElement(CodelistVersionMetamac codelistVersion, VariableElement variableElement) throws MetamacException {
        if (variableElement != null && !codelistVersion.getVariable().getNameableArtefact().getUrn().equals(variableElement.getVariable().getNameableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CODE_VARIABLE_ELEMENT).build();
        }
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
        List<CodeMetamac> targets = new ArrayList<CodeMetamac>(sources.size());
        for (Item source : sources) {
            targets.add((CodeMetamac) source);
        }
        return targets;
    }

    private CodelistFamily retrieveCodelistFamilyByUrn(String urn) throws MetamacException {
        CodelistFamily codelistFamily = getCodelistFamilyRepository().findByUrn(urn);
        if (codelistFamily == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
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

    private VariableElementOperation retrieveVariableElementOperationByCode(String code) throws MetamacException {
        VariableElementOperation variableElementOperation = getVariableElementOperationRepository().findByCode(code);
        if (variableElementOperation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENT_OPERATION_NOT_FOUND).withMessageParameters(code).build();
        }
        return variableElementOperation;
    }
    private CodelistOrderVisualisation retrieveCodelistOrderVisualisationByUrn(String urn) throws MetamacException {
        CodelistOrderVisualisation codelistOrderVisualisation = getCodelistOrderVisualisationRepository().findByUrn(urn);
        if (codelistOrderVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return codelistOrderVisualisation;
    }

    private CodelistOpennessVisualisation retrieveCodelistOpennessVisualisationByUrn(String urn) throws MetamacException {
        CodelistOpennessVisualisation codelistOpennessVisualisation = getCodelistOpennessVisualisationRepository().findByUrn(urn);
        if (codelistOpennessVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return codelistOpennessVisualisation;
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
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_CAN_NOT_REPLACE_ITSELF).withMessageParameters(variable.getNameableArtefact().getUrn()).build();
        }

        // Check any variable in "replaceTo" was not already replaced by another variable
        for (Variable replaceTo : variable.getReplaceToVariables()) {
            if (replaceTo.getReplacedByVariable() != null && !replaceTo.getReplacedByVariable().getNameableArtefact().getUrn().equals(variable.getNameableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_IS_ALREADY_REPLACED).withMessageParameters(replaceTo.getNameableArtefact().getUrn()).build();
            }
        }
    }

    /**
     * Common validations to create or update a variable element
     */
    private void checkVariableElementToCreateOrUpdate(ServiceContext ctx, VariableElement variableElement) throws MetamacException {
        // Check variable doesnt replace self
        if (SrmServiceUtils.isVariableElementInList(variableElement.getNameableArtefact().getUrn(), variableElement.getReplaceToVariableElements())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_CAN_NOT_REPLACE_ITSELF).withMessageParameters(variableElement.getNameableArtefact().getUrn())
                    .build();
        }

        // Check replaceTo belong to same variable
        for (VariableElement variableElementReplaceTo : variableElement.getReplaceToVariableElements()) {
            if (!variableElement.getVariable().getNameableArtefact().getUrn().equals(variableElementReplaceTo.getVariable().getNameableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE)
                        .withMessageParameters(variableElementReplaceTo.getNameableArtefact().getUrn()).build();
            }
        }

        // Check do not change variable (do with contains method to be more efficient, instead parse urn)
        if (variableElement.getId() != null) {
            if (!variableElement.getNameableArtefact().getUrn().contains("=" + variableElement.getVariable().getNameableArtefact().getCode() + ".")) {
                throw new MetamacException(ServiceExceptionType.METADATA_UNMODIFIABLE, ServiceExceptionParameters.VARIABLE_ELEMENT_VARIABLE);
            }
        }

        // Check any variable element in "replaceTo" was not already replaced by another variable element
        for (VariableElement replaceTo : variableElement.getReplaceToVariableElements()) {
            if (replaceTo.getReplacedByVariableElement() != null && !replaceTo.getReplacedByVariableElement().getNameableArtefact().getUrn().equals(variableElement.getNameableArtefact().getUrn())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.ARTEFACT_IS_ALREADY_REPLACED).withMessageParameters(replaceTo.getNameableArtefact().getUrn()).build();
            }
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
    private void setVariableElementUrnUnique(Variable variable, VariableElement variableElement) throws MetamacException {
        String urn = GeneratorUrnUtils.generateVariableElementUrn(variable, variableElement);
        identifiableArtefactRepository.checkUrnUnique(urn, variableElement.getNameableArtefact().getId());

        variableElement.getNameableArtefact().setUrn(urn);
        variableElement.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to visualisation. Set also urnProvider
     */
    private void setCodelistOrderVisualisationUrnUnique(CodelistVersion codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation) throws MetamacException {
        String urn = GeneratorUrnUtils.generateCodelistOrderVisualisationUrn(codelistVersion, codelistOrderVisualisation);
        identifiableArtefactRepository.checkUrnUnique(urn, codelistOrderVisualisation.getNameableArtefact().getId());

        codelistOrderVisualisation.getNameableArtefact().setUrn(urn);
        codelistOrderVisualisation.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * Generate urn, check it is unique and set to visualisation. Set also urnProvider
     */
    private void setCodelistOpennessVisualisationUrnUnique(CodelistVersion codelistVersion, CodelistOpennessVisualisation codelistOpennessVisualisation) throws MetamacException {
        String urn = GeneratorUrnUtils.generateCodelistOpennessVisualisationUrn(codelistVersion, codelistOpennessVisualisation);
        identifiableArtefactRepository.checkUrnUnique(urn, codelistOpennessVisualisation.getNameableArtefact().getId());

        codelistOpennessVisualisation.getNameableArtefact().setUrn(urn);
        codelistOpennessVisualisation.getNameableArtefact().setUrnProvider(urn);
    }

    /**
     * A code can not be moved to its child
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

    @Override
    public CodelistVersionMetamac createCodelistOrderVisualisationAlphabetical(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        CodelistOrderVisualisation alphabeticalOrderVisualisation = new CodelistOrderVisualisation();
        alphabeticalOrderVisualisation.setNameableArtefact(new NameableArtefact());
        alphabeticalOrderVisualisation.getNameableArtefact().setCode(SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE);
        // NameableArtefact
        InternationalString name = new InternationalString();
        name.addText(new LocalisedString("es", "Orden alfabético"));
        name.addText(new LocalisedString("en", "Alphabetical order"));
        name.addText(new LocalisedString("pt", "Ordem alfabética"));
        alphabeticalOrderVisualisation.getNameableArtefact().setName(name);
        alphabeticalOrderVisualisation.setColumnIndex(SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX);
        setCodelistOrderVisualisationUrnUnique(codelistVersion, alphabeticalOrderVisualisation);

        codelistVersion.addOrderVisualisation(alphabeticalOrderVisualisation);
        codelistVersion = getCodelistVersionMetamacRepository().save(codelistVersion);

        sortCodesInAlphabeticalOrder(ctx, codelistVersion, alphabeticalOrderVisualisation);
        return codelistVersion;
    }

    private boolean isCodesSiblings(Item i1, Item i2) {
        if (i1.getParent() == null && i2.getParent() == null) {
            return true;
        } else if (i1.getParent() != null && i2.getParent() != null && i1.getParent().getId().equals(i2.getParent().getId())) {
            return true;
        }
        return false;
    }

    class AlphabeticalByLevelComparator implements Comparator<Item> {

        @Override
        public int compare(Item i1, Item i2) {
            if (isCodesSiblings(i1, i2)) {
                return i1.getNameableArtefact().getCode().compareTo(i2.getNameableArtefact().getCode());
            } else if (i1.getParent() == null) {
                return 1;
            } else if (i2.getParent() == null) {
                return -1;
            } else {
                return i1.getParent().getId().compareTo(i2.getParent().getId());
            }
        }
    }

    @Override
    public void sortCodesInAlphabeticalOrder(ServiceContext ctx, CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation alphabeticalOrderVisualisation) {
        // Add codes, ordered by semantic identifier
        sortCodes(ctx, codelistVersion, alphabeticalOrderVisualisation, new AlphabeticalByLevelComparator());
    }

    @Override
    public void sortCodesByOrder(ServiceContext ctx, CodelistVersionMetamac codelistVersion, final CodelistOrderVisualisation orderVisualisation) {
        // Add codes, ordered by semantic identifier

        Comparator<Item> comparator = new Comparator<Item>() {

            @Override
            public int compare(Item i1, Item i2) {
                CodeMetamac c1 = (CodeMetamac) i1;
                CodeMetamac c2 = (CodeMetamac) i2;
                if (isCodesSiblings(i1, i2)) {
                    // The elements with order set as null will set to the end.
                    Integer c1Order = SrmServiceUtils.getCodeOrder(c1, orderVisualisation.getColumnIndex());
                    Integer c2Order = SrmServiceUtils.getCodeOrder(c2, orderVisualisation.getColumnIndex());
                    if (c1Order == null && c2Order == null) {
                        return 0;
                    } else if (c1Order == null) {
                        return 1;
                    } else if (c2Order == null) {
                        return -1;
                    } else {
                        return c1Order.compareTo(c2Order);
                    }
                } else if (i1.getParent() == null) {
                    return 1;
                } else if (i2.getParent() == null) {
                    return -1;
                } else {
                    return i1.getParent().getId().compareTo(i2.getParent().getId());
                }
            }

        };

        sortCodes(ctx, codelistVersion, orderVisualisation, comparator);
    }

    public void sortCodes(ServiceContext ctx, CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation orderVisualisation, Comparator<Item> comparator) {
        if (!CollectionUtils.isEmpty(codelistVersion.getItems())) {
            List<Item> codesOrdered = new ArrayList<Item>(codelistVersion.getItems());

            Collections.sort(codesOrdered, comparator);

            String previousParentUrn = null;
            int previousOrder = -1;
            for (Item item : codesOrdered) {
                CodeMetamac code = (CodeMetamac) item;
                String actualParentUrn = code.getParent() != null ? code.getParent().getNameableArtefact().getUrn() : null;
                if (!StringUtils.equals(previousParentUrn, actualParentUrn)) {
                    previousOrder = -1; // another level
                    previousParentUrn = code.getParent() != null ? code.getParent().getNameableArtefact().getUrn() : null;
                }
                int order = previousOrder == -1 ? 0 : previousOrder + 1;
                SrmServiceUtils.setCodeOrder(code, orderVisualisation.getColumnIndex(), Integer.valueOf(order));
                getCodeMetamacRepository().save(code);
                previousOrder = order;
            }
        }
    }

    @Override
    public CodelistVersionMetamac createCodelistOpennessVisualisationAllOpened(ServiceContext ctx, CodelistVersionMetamac codelistVersion) throws MetamacException {
        CodelistOpennessVisualisation allExpandedOpennessVisualisation = new CodelistOpennessVisualisation();
        allExpandedOpennessVisualisation.setNameableArtefact(new NameableArtefact());
        allExpandedOpennessVisualisation.getNameableArtefact().setCode(SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_CODE);
        // NameableArtefact
        InternationalString name = new InternationalString();
        name.addText(new LocalisedString("es", "Todos desplegados"));
        name.addText(new LocalisedString("en", "All opened"));
        name.addText(new LocalisedString("pt", "Tudo aberto"));
        allExpandedOpennessVisualisation.getNameableArtefact().setName(name);
        allExpandedOpennessVisualisation.setColumnIndex(SrmConstants.CODELIST_OPENNESS_VISUALISATION_ALL_EXPANDED_COLUMN_INDEX);
        setCodelistOpennessVisualisationUrnUnique(codelistVersion, allExpandedOpennessVisualisation);

        codelistVersion.addOpennessVisualisation(allExpandedOpennessVisualisation);
        codelistVersion = getCodelistVersionMetamacRepository().save(codelistVersion);

        // Add codes with expanded = true as default
        getCodeMetamacRepository().updateCodesOpennessColumnByCodelist(codelistVersion, allExpandedOpennessVisualisation.getColumnIndex(), Boolean.TRUE);
        return codelistVersion;
    }

    @Override
    public CodelistVersionMetamac recreateCodelistOpennessVisualisationAllOpened(ServiceContext ctx, CodelistVersionMetamac codelistVersion,
            CodelistOpennessVisualisation allExpandedOpennessVisualisation) throws MetamacException {
        getCodeMetamacRepository().updateCodesOpennessColumnByCodelist(codelistVersion, allExpandedOpennessVisualisation.getColumnIndex(), Boolean.TRUE);
        return codelistVersion;
    }

    private VariableElementOperation createVariableElementOperationCommon(VariableElementOperationTypeEnum operationType, List<String> sources, List<String> targets) throws MetamacException {

        // Validation
        CodesMetamacInvocationValidator.checkAddVariableElementOperation(sources, targets, null);

        VariableElementOperation variableElementOperation = new VariableElementOperation();
        variableElementOperation.setOperationType(operationType);
        variableElementOperation.setCode(UUID.randomUUID().toString());

        // Add sources and targets
        for (String sourceVariableElementUrn : sources) {
            VariableElement sourceVariableElement = retrieveVariableElementByUrn(sourceVariableElementUrn);
            variableElementOperation.addSource(sourceVariableElement);
        }
        for (String targetVariableElementUrn : targets) {
            VariableElement targetVariableElement = retrieveVariableElementByUrn(targetVariableElementUrn);
            variableElementOperation.addTarget(targetVariableElement);
        }

        // Check all elements belong to same variable and has validTo filled
        List<VariableElement> variableElements = new ArrayList<VariableElement>(variableElementOperation.getSources().size() + variableElementOperation.getTargets().size());
        variableElements.addAll(variableElementOperation.getSources());
        variableElements.addAll(variableElementOperation.getTargets());
        Variable variable = null;
        for (VariableElement variableElement : variableElements) {
            // ValidTo
            if (variableElement.getValidTo() == null) {
                throw new MetamacException(ServiceExceptionType.VARIABLE_ELEMENT_MUST_HAVE_VALID_TO_FILLED, variableElement.getNameableArtefact().getUrn());
            }
            // Variable
            if (variable == null) {
                variable = variableElement.getVariable();
            } else {
                if (!variable.getNameableArtefact().getUrn().equals(variableElement.getVariable().getNameableArtefact().getUrn())) {
                    throw new MetamacException(ServiceExceptionType.VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE, variableElement.getNameableArtefact().getUrn());
                }
            }
        }

        // Create
        variableElementOperation.setVariable(variable);
        variableElementOperation = getVariableElementOperationRepository().save(variableElementOperation);

        return variableElementOperation;
    }

    private void reorderCodesDeletingOneCode(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, Item parent, CodeMetamac code) throws MetamacException {
        Integer columnIndex = codelistOrderVisualisation.getColumnIndex();
        Integer previousOrder = SrmServiceUtils.getCodeOrder(code, columnIndex);
        getCodeMetamacRepository().reorderCodesDeletingOneCode(codelistVersion, parent, code, columnIndex, previousOrder);
    }

    private void reorderCodesAddingOneCodeInMiddle(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, CodeMetamac code, Integer order)
            throws MetamacException {
        Integer columnIndex = codelistOrderVisualisation.getColumnIndex();
        getCodeMetamacRepository().reorderCodesAddingOneCodeInMiddle(codelistVersion, code, columnIndex, order);
    }

    private void setCodeOrderInAlphabeticalPositionAndReorderCodesInLevel(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, Item parent, CodeMetamac code)
            throws MetamacException {
        if (!SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
            return;
        }
        Integer alphabeticalPosition = getCodeMetamacRepository().getCodeAlphabeticPositionInLevel(codelistVersion, parent, code);
        Integer columnIndex = SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX;
        if (alphabeticalPosition == null) {
            alphabeticalPosition = Integer.valueOf(0);
        }
        reorderCodesAddingOneCodeInMiddle(codelistVersion, codelistOrderVisualisation, code, alphabeticalPosition);
        SrmServiceUtils.setCodeOrder(code, columnIndex, alphabeticalPosition);
    }

    private Integer setCodeOrderInLastPositionInLevel(CodelistVersionMetamac codelistVersion, CodelistOrderVisualisation codelistOrderVisualisation, Item parent, CodeMetamac code,
            Integer precalculateOrder) throws MetamacException {
        Integer columnIndex = codelistOrderVisualisation.getColumnIndex();
        Integer orderInNewLevel = null;
        if (precalculateOrder == null) {
            SrmServiceUtils.setCodeOrder(code, columnIndex, -1); // min value
            Integer actualMaximumOrderInLevel = getCodeMetamacRepository().getCodeMaximumOrderInLevel(codelistVersion, parent, columnIndex);
            if (actualMaximumOrderInLevel == null) {
                orderInNewLevel = 0;
            } else {
                orderInNewLevel = actualMaximumOrderInLevel + 1;
            }
        } else {
            orderInNewLevel = precalculateOrder;
        }
        SrmServiceUtils.setCodeOrder(code, columnIndex, orderInNewLevel);
        return orderInNewLevel;
    }

    @Override
    public CodelistVersionMetamac versioningCodelistOrderVisualisations(ServiceContext ctx, CodelistVersionMetamac source, CodelistVersionMetamac target) throws MetamacException {
        for (CodelistOrderVisualisation codelistOrderVisualisationSource : source.getOrderVisualisations()) {
            versioningCodelistOrderVisualisation(codelistOrderVisualisationSource, target);
        }
        target = getCodelistVersionMetamacRepository().save(target);
        for (CodelistOrderVisualisation codelistOrderVisualisationTarget : target.getOrderVisualisations()) {
            if (source.getDefaultOrderVisualisation().getNameableArtefact().getCode().equals(codelistOrderVisualisationTarget.getNameableArtefact().getCode())) {
                target.setDefaultOrderVisualisation(codelistOrderVisualisationTarget);
                break;
            }
        }
        target = getCodelistVersionMetamacRepository().save(target);
        return target;
    }

    private CodelistOrderVisualisation versioningCodelistOrderVisualisation(CodelistOrderVisualisation source, CodelistVersionMetamac codelistTarget) throws MetamacException {
        CodelistOrderVisualisation target = new CodelistOrderVisualisation();
        target.setColumnIndex(source.getColumnIndex());
        target.setNameableArtefact(BaseVersioningCopyUtils.copyNameableArtefact(source.getNameableArtefact()));
        setCodelistOrderVisualisationUrnUnique(codelistTarget, target);
        codelistTarget.addOrderVisualisation(target);
        return target;
    }

    @Override
    public CodelistVersionMetamac versioningCodelistOpennessVisualisations(ServiceContext ctx, CodelistVersionMetamac source, CodelistVersionMetamac target) throws MetamacException {
        for (CodelistOpennessVisualisation codelistOpennessVisualisationSource : source.getOpennessVisualisations()) {
            versioningCodelistOpennessVisualisation(codelistOpennessVisualisationSource, target);
        }
        target = getCodelistVersionMetamacRepository().save(target);
        for (CodelistOpennessVisualisation codelistOpennessVisualisationTarget : target.getOpennessVisualisations()) {
            if (source.getDefaultOpennessVisualisation().getNameableArtefact().getCode().equals(codelistOpennessVisualisationTarget.getNameableArtefact().getCode())) {
                target.setDefaultOpennessVisualisation(codelistOpennessVisualisationTarget);
                break;
            }
        }
        target = getCodelistVersionMetamacRepository().save(target);
        return target;
    }

    private CodelistOpennessVisualisation versioningCodelistOpennessVisualisation(CodelistOpennessVisualisation source, CodelistVersionMetamac codelistTarget) throws MetamacException {
        CodelistOpennessVisualisation target = new CodelistOpennessVisualisation();
        target.setColumnIndex(source.getColumnIndex());
        target.setNameableArtefact(BaseVersioningCopyUtils.copyNameableArtefact(source.getNameableArtefact()));
        setCodelistOpennessVisualisationUrnUnique(codelistTarget, target);
        codelistTarget.addOpennessVisualisation(target);
        return target;
    }

    private CodeMetamac initVisualisationsToCodeCreated(CodelistVersionMetamac codelistVersion, CodeMetamac code) throws MetamacException {

        // Order visualisations
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        Integer orderInLastPosition = null; // no avoid innecessary extra queries
        for (CodelistOrderVisualisation codelistOrderVisualisation : codelistVersion.getOrderVisualisations()) {
            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(codelistOrderVisualisation)) {
                setCodeOrderInAlphabeticalPositionAndReorderCodesInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                orderInLastPosition = setCodeOrderInLastPositionInLevel(codelistVersion, codelistOrderVisualisation, code.getParent(), code, orderInLastPosition);
            }
        }

        // Openness visualisations
        Boolean defaultOpennessValue = SrmConstants.CODELIST_OPENNESS_VISUALISATION_DEFAULT_VALUE;
        Boolean allExpandedAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        for (CodelistOpennessVisualisation codelistOpennessVisualisation : codelistVersion.getOpennessVisualisations()) {
            Boolean value = null;
            if (!allExpandedAlreadyUpdated && SrmServiceUtils.isAllExpandedOpennessVisualisation(codelistOpennessVisualisation)) {
                value = Boolean.TRUE;
                allExpandedAlreadyUpdated = Boolean.TRUE;
            } else {
                value = defaultOpennessValue;
            }
            SrmServiceUtils.setCodeOpenness(code, codelistOpennessVisualisation.getColumnIndex(), value);
        }

        code = getCodeMetamacRepository().save(code);
        return code;
    }

    private void copyCodeInCodelist(ServiceContext ctx, CodelistVersionMetamac codelistVersionSource, CodelistVersionMetamac codelistVersionTarget, CodeMetamac parent,
            CodeToCopyHierarchy codeToCopyHierarchy) throws MetamacException {

        // Copy metadata from source
        CodeMetamac codeSource = retrieveCodeByUrn(ctx, codeToCopyHierarchy.getUrn());
        if (!codeSource.getItemSchemeVersion().getMaintainableArtefact().getUrn().equals(codelistVersionSource.getMaintainableArtefact().getUrn())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.URN).build();
        }
        CodeMetamac codeTarget = new CodeMetamac();
        codeTarget.setNameableArtefact(new NameableArtefact());
        if (codeToCopyHierarchy.getCode() != null) {
            codeTarget.getNameableArtefact().setCode(codeToCopyHierarchy.getCode());
        } else {
            codeTarget.getNameableArtefact().setCode(codeSource.getNameableArtefact().getCode());
        }
        codeTarget.getNameableArtefact().setUriProvider(null);
        codeTarget.getNameableArtefact().setName(BaseCopyAllMetadataUtils.copy(codeSource.getNameableArtefact().getName()));
        codeTarget.getNameableArtefact().setDescription(BaseCopyAllMetadataUtils.copy(codeSource.getNameableArtefact().getDescription()));
        codeTarget.getNameableArtefact().setComment(null);
        codeTarget.setShortName(BaseCopyAllMetadataUtils.copy(codeSource.getShortName()));
        if (codelistVersionTarget.getVariable() != null && codelistVersionSource.getVariable() != null
                && codelistVersionSource.getVariable().getNameableArtefact().getUrn().equals(codelistVersionTarget.getVariable().getNameableArtefact().getUrn())) {
            codeTarget.setVariableElement(codeSource.getVariableElement());
        }
        for (Annotation annotationSource : codeSource.getNameableArtefact().getAnnotations()) {
            codeTarget.getNameableArtefact().addAnnotation(BaseCopyAllMetadataUtils.copy(annotationSource));
        }
        codeTarget.setParent(parent);

        // Create
        codeTarget = createCode(ctx, codelistVersionTarget.getMaintainableArtefact().getUrn(), codeTarget);

        // Children
        for (CodeToCopyHierarchy child : codeToCopyHierarchy.getChildren()) {
            copyCodeInCodelist(ctx, codelistVersionSource, codelistVersionTarget, codeTarget, child);
        }
    }
}
