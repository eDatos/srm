package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCategoriesSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CategoriesClientSecurityUtils {

    // CATEGORY SCHEMES

    public static boolean canCreateCategoryScheme() {
        return SharedCategoriesSecurityUtils.canCreateCategoryScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateCategoryScheme(ProcStatusEnum procStatus) {
        return SharedCategoriesSecurityUtils.canUpdateCategoryScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategoryScheme() {
        return SharedCategoriesSecurityUtils.canDeleteCategoryScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCategorySchemeToProductionValidation() {
        return SharedCategoriesSecurityUtils.canSendCategorySchemeToProductionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canSendCategorySchemeToDiffusionValidation() {
        return SharedCategoriesSecurityUtils.canSendCategorySchemeToDiffusionValidation(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canRejectCategorySchemeValidation(ProcStatusEnum procStatus) {
        return SharedCategoriesSecurityUtils.canRejectCategorySchemeValidation(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canPublishCategorySchemeInternally() {
        return SharedCategoriesSecurityUtils.canPublishCategorySchemeInternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canPublishCategorySchemeExternally() {
        return SharedCategoriesSecurityUtils.canPublishCategorySchemeExternally(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canVersioningCategoryScheme() {
        return SharedCategoriesSecurityUtils.canVersioningCategoryScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCreateCategorySchemeTemporalVersion() {
        return SharedCategoriesSecurityUtils.canCreateCategorySchemeTemporalVersion(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAnnounceCategoryScheme() {
        return SharedCategoriesSecurityUtils.canAnnounceCategoryScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCancelCategorySchemeValidity() {
        return SharedCategoriesSecurityUtils.canEndCategorySchemeValidity(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canCreateCategorisation(ProcStatusEnum procStatus) {
        return SharedCategoriesSecurityUtils.canModifyCategorisationFromCategoryScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategorisation(ProcStatusEnum procStatus, CategorisationDto categorisationDto) {
        // Maintainer and temporal version is checked because the creation/deletion of a categorisation is not allowed when the resource is imported (i am not the maintainer) or the version is a
        // temporal one
        return SharedCategoriesSecurityUtils.canModifyCategorisationFromCategoryScheme(MetamacSrmWeb.getCurrentUser(), procStatus)
                && CommonUtils.canSdmxMetadataAndStructureBeModified(categorisationDto);
    }

    public static boolean canCopyCategoryScheme(RelatedResourceDto maintainer) {
        // Only resources from other organisations can be copied
        return SharedCategoriesSecurityUtils.canCopyCategoryScheme(MetamacSrmWeb.getCurrentUser()) && !CommonUtils.isDefaultMaintainer(maintainer);
    }

    // CATEGORIES

    public static boolean canCreateCategory(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        // Maintainer and temporal version is checked because the structure of an imported resource can not be modified
        return SharedCategoriesSecurityUtils.canModifyCategoryFromCategoryScheme(MetamacSrmWeb.getCurrentUser(), categorySchemeMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(categorySchemeMetamacDto);
    }

    public static boolean canUpdateCategory(ProcStatusEnum procStatus) {
        return SharedCategoriesSecurityUtils.canModifyCategoryFromCategoryScheme(MetamacSrmWeb.getCurrentUser(), procStatus);
    }

    public static boolean canDeleteCategory(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        // Maintainer and temporal version is checked because the structure of an imported resource can not be modified
        return SharedCategoriesSecurityUtils.canModifyCategoryFromCategoryScheme(MetamacSrmWeb.getCurrentUser(), categorySchemeMetamacDto.getLifeCycle().getProcStatus())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(categorySchemeMetamacDto);
    }
}
