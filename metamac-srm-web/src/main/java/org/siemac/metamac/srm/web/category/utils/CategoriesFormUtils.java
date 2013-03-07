package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource.
 * The metadata of type {@link InternationalStringDto} are always editable (that's why are not specified in this class), but only to specify new translations.
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
                && CommonUtils.isDefaultMaintainer(categorySchemeDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canCategoryCodeBeEdited(CategorySchemeMetamacDto categorySchemeDto) {
        if (categorySchemeDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(categorySchemeDto.getMaintainer());
    }
}
