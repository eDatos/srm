package org.siemac.metamac.srm.web.code.utils;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCodesSecurityUtils;
import org.siemac.metamac.srm.core.security.shared.SharedItemsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

public class CodesClientSecurityUtils {

    // CODELISTS

    public static boolean canCreateCodelist() {
        return SharedItemsSecurityUtils.canCreateItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateCodelist(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canUpdateItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCodelist() {
        return SharedItemsSecurityUtils.canDeleteItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCodelistToProductionValidation() {
        return SharedItemsSecurityUtils.canSendItemSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCodelistToDiffusionValidation() {
        return SharedItemsSecurityUtils.canSendItemSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectCodelistValidation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canRejectItemSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishCodelistInternally() {
        return SharedItemsSecurityUtils.canPublishItemSchemeInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishCodelistExternally() {
        return SharedItemsSecurityUtils.canPublishItemSchemeExternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canVersioningCodelist() {
        return SharedItemsSecurityUtils.canVersioningItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceCodelist() {
        return SharedItemsSecurityUtils.canAnnounceItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelCodelistValidity() {
        return SharedItemsSecurityUtils.canEndItemSchemeValidity(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canModifyCategorisation(CodelistMetamacDto codelistMetamacDto) {
        // Maintainer is checked because the creation/deletion of a categorisation is not allowed when the resource is imported (i am not the maintainer)
        return SharedItemsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.isDefaultMaintainer(codelistMetamacDto.getMaintainer());
    }

    public static boolean canAddCodelistToCodelistFamily() {
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRemoveCodelistFromCodelistFamily() {
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    // CODES

    public static boolean canCreateCode(CodelistMetamacDto codelistMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.isDefaultMaintainer(codelistMetamacDto.getMaintainer());
    }

    public static boolean canUpdateCode(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateCodeParent(CodelistMetamacDto codelistMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.isDefaultMaintainer(codelistMetamacDto.getMaintainer());
    }

    public static boolean canDeleteCode(CodelistMetamacDto codelistMetamacDto) {
        // Maintainer is checked because the structure of an imported resource can not be modified
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), codelistMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.isDefaultMaintainer(codelistMetamacDto.getMaintainer());
    }

    public static boolean canUpdateCodeVariableElement(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    // ORDERS

    public static boolean canCreateCodelistOrderVisualisation(ProcStatusEnum procStatus) {
        return SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateCodelistOrderVisualisation(ProcStatusEnum procStatus) {
        return SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCodelistOrderVisualisation(ProcStatusEnum procStatus) {
        return SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(MetamacSrmWeb.getCurrentUser(), procStatus);
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
}
