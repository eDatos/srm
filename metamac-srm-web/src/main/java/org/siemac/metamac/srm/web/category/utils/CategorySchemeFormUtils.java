package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

public class CategorySchemeFormUtils {

    // CODE

    public static boolean canCodeBeEdited(CategorySchemeMetamacDto categorySchemeDto) {
        if (categorySchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(categorySchemeDto.getLifeCycle().getProcStatus(), categorySchemeDto.getVersionLogic())
                && CommonUtils.isDefaultMaintainer(categorySchemeDto.getMaintainer());
    }

    // NAME

    public static boolean canNameBeEdited(CategorySchemeMetamacDto categorySchemeDto) {
        if (categorySchemeDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(categorySchemeDto.getMaintainer());
    }

    // DESCRIPTION

    public static boolean canDescriptionBeEdited(CategorySchemeMetamacDto categorySchemeDto) {
        if (categorySchemeDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(categorySchemeDto.getMaintainer());
    }
}
