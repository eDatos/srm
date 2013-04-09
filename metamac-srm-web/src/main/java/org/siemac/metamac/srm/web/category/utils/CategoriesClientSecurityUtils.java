package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCategoriesSecurityUtils;
import org.siemac.metamac.srm.core.security.shared.SharedItemsSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;

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

    public static boolean canCreateCategorySchemeTemporalVersion() {
        return SharedCategoriesSecurityUtils.canCreateCategorySchemeTemporalVersion(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceCategoryScheme() {
        return SharedItemsSecurityUtils.canAnnounceItemScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelCategorySchemeValidity() {
        return SharedItemsSecurityUtils.canEndItemSchemeValidity(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCreateCategorisation(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategorisation(ProcStatusEnum procStatus, CategorisationDto categorisationDto) {
        // Maintainer and temporal version is checked because the creation/deletion of a categorisation is not allowed when the resource is imported (i am not the maintainer) or the version is a
        // temporal one
        return SharedItemsSecurityUtils.canModifyCategorisation(MetamacSrmWeb.getCurrentUser(), procStatus) && CommonUtils.canSdmxMetadataAndStructureBeModified(categorisationDto);
    }

    // CATEGORIES

    public static boolean canCreateCategory(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        // Maintainer and temporal version is checked because the structure of an imported resource can not be modified
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), categorySchemeMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(categorySchemeMetamacDto);
    }

    public static boolean canUpdateCategory(ProcStatusEnum procStatus) {
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategory(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        // Maintainer and temporal version is checked because the structure of an imported resource can not be modified
        return SharedItemsSecurityUtils.canModifyItemFromItemScheme(MetamacSrmWeb.getCurrentUser(), categorySchemeMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(categorySchemeMetamacDto);
    }
}
