package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource and on whether the resource is in a
 * temporal version or not.
 * Metadata of type {@link InternationalStringDto} are always editable (that's why they are not specified in this class), but only to add new translations.
 */
public class CategoriesFormUtils {

    // ---------------------------------------------------------------------------------------------
    // CATEGORY SCHEMES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canCategorySchemeCodeBeEdited(CategorySchemeMetamacDto categorySchemeDto) {
        if (categorySchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(categorySchemeDto.getLifeCycle().getProcStatus(), categorySchemeDto.getVersionLogic())
                && CommonUtils.canSdmxMetadataAndStructureBeModified(categorySchemeDto);
    }

    // ---------------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canCategoryCodeBeEdited(CategorySchemeMetamacDto categorySchemeDto) {
        if (categorySchemeDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(categorySchemeDto);
    }
}
