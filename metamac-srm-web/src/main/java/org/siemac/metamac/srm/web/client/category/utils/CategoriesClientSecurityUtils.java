package org.siemac.metamac.srm.web.client.category.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedItemsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class CategoriesClientSecurityUtils {

    // CATEGORY SCHEMES

    public static boolean canCreateCategoryScheme() {
        return SharedItemsSecurityUtils.canCreateItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateCategoryScheme(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canUpdateItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategoryScheme() {
        return SharedItemsSecurityUtils.canDeleteItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCategorySchemeToProductionValidation() {
        return SharedItemsSecurityUtils.canSendItemSchemeToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCategorySchemeToDiffusionValidation() {
        return SharedItemsSecurityUtils.canSendItemSchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectCategorySchemeValidation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canRejectItemSchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishCategorySchemeInternally() {
        return SharedItemsSecurityUtils.canPublishItemSchemeInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishCategorySchemeExternally() {
        return SharedItemsSecurityUtils.canPublishItemSchemeExternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canVersioningCategoryScheme() {
        return SharedItemsSecurityUtils.canVersioningItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceCategoryScheme() {
        return SharedItemsSecurityUtils.canAnnounceItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelCategorySchemeValidity() {
        return SharedItemsSecurityUtils.canEndItemSchemeValidity(MetamacSrmWeb.getCurrentUser());
    }

    // CATEGORIES

    public static boolean canCreateCategory(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canUpdateCategory(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategory(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

}
