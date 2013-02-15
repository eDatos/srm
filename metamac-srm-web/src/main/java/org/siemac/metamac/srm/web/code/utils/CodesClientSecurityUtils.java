package org.siemac.metamac.srm.web.code.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCodesSecurityUtils;
import org.siemac.metamac.srm.core.security.shared.SharedItemsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

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

    public static boolean canModifyCategorisation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    // CODES

    public static boolean canCreateCode(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateCode(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCode(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    // ORDERS

    public static boolean canModifiyCodelistOrderVisualisation(ProcStatusEnum procStatus) {
        return SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    // CODELIST FAMILY

    public static boolean canModifyCodelistFamily() {
        return SharedCodesSecurityUtils.canCrudCodelistFamily(MetamacSrmWeb.getCurrentUser());
    }

    // VARIABLE FAMILY

    public static boolean canModifyVariableFamily() {
        return SharedCodesSecurityUtils.canCrudVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAddVariablesToVariableFamily() {
        return SharedCodesSecurityUtils.canAddVariablesToVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRemoveVariablesFromVariableFamily() {
        return SharedCodesSecurityUtils.canRemoveVariableFromVariableFamily(MetamacSrmWeb.getCurrentUser());
    }

    // VARIABLE

    public static boolean canModifyVariable() {
        return SharedCodesSecurityUtils.canCrudVariable(MetamacSrmWeb.getCurrentUser());
    }

    // VARIABLE ELEMENT

    public static boolean canCrudVariableElement() {
        return SharedCodesSecurityUtils.canCrudVariableElement(MetamacSrmWeb.getCurrentUser());
    }
}
