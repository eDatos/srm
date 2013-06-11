package org.siemac.metamac.srm.web.code.utils;

import java.util.Date;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCodesSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.CodelistFamilyViewImpl;
import org.siemac.metamac.srm.web.code.widgets.CodelistMainFormLayout;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CodesClientSecurityUtils {

    //
    // When a codelist is being version in background, the codelist and its codes CANNOT be modified. That's why an extra check (is isTaskInBackground TRUE or not) has been added to all these
    // security methods.
    //

    // CODELISTS

    public static boolean canCreateCodelist() {
        return SharedCodesSecurityUtils.canCreateCodelist(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateCodelist(ProcStatusEnum procStatus, Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canUpdateCodelist(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCodelist(ProcStatusEnum procStatus, Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        if (CommonUtils.isMaintainableArtefactPublished(procStatus)) {
            return false;
        }
        return SharedCodesSecurityUtils.canDeleteCodelist(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCodelistToProductionValidation(Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canSendCodelistToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCodelistToDiffusionValidation(Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canSendCodelistToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectCodelistValidation(ProcStatusEnum procStatus, Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canRejectCodelistValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishCodelistInternally(Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canPublishCodelistInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishCodelistExternally(Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canPublishCodelistExternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canVersioningCodelist(Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canVersioningCodelist(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCreateCodelistTemporalVersion(Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canCreateCodelistTemporalVersion(MetamacSrmWeb.getCurrentUser());
    }

    // public static boolean canAnnounceCodelist() {
    // }

    public static boolean canCancelCodelistValidity(CodelistMetamacBasicDto codelistMetamacDto) {
        return canCancelCodelistValidity(codelistMetamacDto.getUrn(), codelistMetamacDto.getIsTaskInBackground(), codelistMetamacDto.getMaintainer(), codelistMetamacDto.getVersionLogic(),
                codelistMetamacDto.getLifeCycle().getProcStatus(), codelistMetamacDto.getValidTo());
    }

    public static boolean canCancelCodelistValidity(String urn, Boolean isTaskInBackground, RelatedResourceDto maintainer, String versionLogic, ProcStatusEnum procStatus, Date validTo) {

        // validity was already ended
        if (validTo != null) {
            return false;
        }

        // only externally published resources can be canceled
        if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return false;
        }

        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canEndCodelistValidity(MetamacSrmWeb.getCurrentUser()) && CommonUtils.canSdmxMetadataAndStructureBeModified(urn, maintainer, versionLogic);
    }

    public static boolean canCreateCategorisation(ProcStatusEnum procStatus, Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategorisation(ProcStatusEnum procStatus, Boolean isTaskInBackground, CategorisationDto categorisationDto) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }

        if (BooleanUtils.isTrue(categorisationDto.getFinalLogic())) {

            // if it is final, can NEVER be deleted
            return false;

        } else {

            if (CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {

                return SharedCodesSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus);

            } else {

                // if it does not have the default maintainer, can NEVER be deleted
                return false;
            }
        }
    }

    public static boolean canCancelCategorisationValidity(ProcStatusEnum procStatus, Boolean isTaskInBackground, CategorisationDto categorisationDto) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }

        if (categorisationDto.getValidTo() != null) { // The validity has been canceled previously
            return false;
        }

        // Only categorisations of default maintainer can be canceled

        if (CommonUtils.isDefaultMaintainer(categorisationDto.getMaintainer())) {
            return SharedCodesSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus);
        } else {
            return false;
        }
    }

    /**
     * This method is called from the {@link CodelistMainFormLayout}
     * 
     * @param isTaskInBackground
     * @return
     */
    public static boolean canAddCodelistToCodelistFamily(Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    /**
     * This method is called from the {@link CodelistFamilyViewImpl}
     * 
     * @return
     */
    public static boolean canAddCodelistToCodelistFamily() {
        // If the codelist has a background running is not checked here. The service (in metamac-srm-core) should throw and exception.
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRemoveCodelistFromCodelistFamily() {
        // If the codelist has a background running is not checked here. The service (in metamac-srm-core) should throw and exception.
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCopyCodelist(RelatedResourceDto maintainer, Boolean isTaskInBackground) {
        if (isTaskInBackground(isTaskInBackground)) {
            return false;
        }
        // Only resources from other organisations can be copied
        return SharedCodesSecurityUtils.canCopyCodelist(MetamacSrmWeb.getCurrentUser()) && !CommonUtils.isDefaultMaintainer(maintainer);
    }

    // CODES

    public static boolean canCreateCode(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        // Maintainer and temporal version is checked because the structure of an imported resource (or a resource in temporal version) can not be modified
        return SharedCodesSecurityUtils.canCreateCode(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(codelistMetamacDto);
    }

    public static boolean canUpdateCode(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canUpdateCode(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canImportCodes(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canImportCodes(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canUpdateCodeParent(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        // Maintainer and temporal version is checked because the structure of an imported resource (or a resource in temporal version) can not be modified
        return SharedCodesSecurityUtils.canModifyCodeFromCodelist(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(codelistMetamacDto);
    }

    public static boolean canDeleteCode(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        // Maintainer and temporal version is checked because the structure of an imported resource (or a resource in temporal version) can not be modified
        return SharedCodesSecurityUtils.canModifyCodeFromCodelist(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(codelistMetamacDto);
    }

    public static boolean canUpdateCodeVariableElement(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canUpdateCodeVariableElement(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canExportCodes(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canExportCodesTsv(MetamacSrmWeb.getCurrentUser());
    }

    // ORDERS

    public static boolean canCreateCodelistOrderVisualisation(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canImportCodelistOrderVisualisations(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canImportCodelistOrderVisualisations(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canUpdateCodelistOrderVisualisation(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canDeleteCodelistOrderVisualisation(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canExportCodesOrder(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canExportCodeOrdersTsv(MetamacSrmWeb.getCurrentUser());
    }

    // OPENNESS LEVELS

    public static boolean canCreateCodelistOpennessVisualisation(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canCrudCodelistOpennessVisualisation(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canUpdateCodelistOpennessVisualisation(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canCrudCodelistOpennessVisualisation(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    public static boolean canDeleteCodelistOpennessVisualisation(CodelistMetamacDto codelistMetamacDto) {
        if (isTaskInBackground(codelistMetamacDto.getIsTaskInBackground())) {
            return false;
        }
        return SharedCodesSecurityUtils.canCrudCodelistOpennessVisualisation(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus());
    }

    // CODELIST FAMILY

    public static boolean canCreateCodelistFamily() {
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateCodelistFamily() {
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canDeleteCodelistFamily() {
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    // VARIABLE FAMILY

    public static boolean canCreateVariableFamily() {
        return SharedCodesSecurityUtils.canCrudVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateVariableFamily() {
        return SharedCodesSecurityUtils.canCrudVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canDeleteVariableFamily() {
        return SharedCodesSecurityUtils.canCrudVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAddVariablesToVariableFamily() {
        return SharedCodesSecurityUtils.canAddVariablesToVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRemoveVariablesFromVariableFamily() {
        return SharedCodesSecurityUtils.canRemoveVariableFromVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    // VARIABLE

    public static boolean canCreateVariable() {
        return SharedCodesSecurityUtils.canCrudVariable(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateVariable() {
        return SharedCodesSecurityUtils.canCrudVariable(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canDeleteVariable() {
        return SharedCodesSecurityUtils.canCrudVariable(MetamacSrmWeb.getCurrentUser());
    }

    // VARIABLE ELEMENT

    public static boolean canCreateVariableElement() {
        return SharedCodesSecurityUtils.canCrudVariableElement(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canImportVariableElements() {
        return SharedCodesSecurityUtils.canImportVariableElements(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateVariableElement() {
        return SharedCodesSecurityUtils.canCrudVariableElement(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canDeleteVariableElement() {
        return SharedCodesSecurityUtils.canCrudVariableElement(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canFusionVariableElements() {
        return SharedCodesSecurityUtils.canCrudVariableElement(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSegregateVariableElement() {
        return SharedCodesSecurityUtils.canCrudVariableElement(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canDeleteVariableElementOperation() {
        return SharedCodesSecurityUtils.canCrudVariableElement(MetamacSrmWeb.getCurrentUser());
    }

    //
    // PRIVATE METHODS
    //

    private static boolean isTaskInBackground(Boolean isTaskInBackground) {
        return BooleanUtils.isTrue(isTaskInBackground);
    }
}
