package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

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
