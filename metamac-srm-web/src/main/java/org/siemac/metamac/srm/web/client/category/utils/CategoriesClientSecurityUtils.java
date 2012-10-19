package org.siemac.metamac.srm.web.client.category.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class CategoriesClientSecurityUtils {

    // CATEGORY SCHEMES

    public static boolean canCreateCategoryScheme() {
        return true;
        // TODO return SharedCategorysSecurityUtils.canCreateCategoryScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canUpdateCategoryScheme() {
        return true;
        // TODO return SharedCategorysSecurityUtils.canUpdateCategoryScheme(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteCategoryScheme() {
        return true;
        // return SharedCategorysSecurityUtils.canDeleteCategoryScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendCategorySchemeToProductionValidation() {
        return true;
        // return SharedCategorysSecurityUtils.canSendCategorySchemeToProductionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canSendCategorySchemeToDiffusionValidation() {
        return true;
        // return SharedCategorysSecurityUtils.canSendCategorySchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canRejectCategorySchemeValidation(ProcStatusEnum procStatus) {
        return true;
        // return SharedCategorysSecurityUtils.canRejectCategorySchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canPublishCategorySchemeInternally() {
        return true;
        // return SharedCategorysSecurityUtils.canPublishCategorySchemeInternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canPublishCategorySchemeExternally() {
        return true;
        // return SharedCategorysSecurityUtils.canPublishCategorySchemeExternally(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canVersioningCategoryScheme() {
        return true;
        // return SharedCategorysSecurityUtils.canVersioningCategoryScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canAnnounceCategoryScheme() {
        return true;
        // return SharedCategorysSecurityUtils.canAnnounceCategoryScheme(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    public static boolean canCancelCategorySchemeValidity() {
        return true;
        // return SharedCategorysSecurityUtils.canCancelCategorySchemeValidity(MetamacSrmWeb.getCurrentUser(), type, operationCode);
    }

    // CATEGORIES

    public static boolean canCreateCategory(ProcStatusEnum procStatus) {
        return true;
        // return SharedCategorysSecurityUtils.canCreateCategory(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canUpdateCategory(ProcStatusEnum procStatus) {
        return true;
        // return SharedCategorysSecurityUtils.canUpdateCategory(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

    public static boolean canDeleteCategory(ProcStatusEnum procStatus) {
        return true;
        // return SharedCategorysSecurityUtils.canDeleteCategory(MetamacSrmWeb.getCurrentUser(), procStatus, type, operationCode);
    }

}
