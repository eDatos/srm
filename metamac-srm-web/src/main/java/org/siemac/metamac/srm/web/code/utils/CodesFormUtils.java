package org.siemac.metamac.srm.web.code.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource.
 * Metadata of type {@link InternationalStringDto} are always editable (that's why they are not specified in this class), but only to add new translations.
 */
public class CodesFormUtils {

    // ---------------------------------------------------------------------------------------------
    // CODELISTS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canCodelistCodeBeEdited(CodelistMetamacDto codelistDto) {
        if (codelistDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(codelistDto.getLifeCycle().getProcStatus(), codelistDto.getVersionLogic())
                && CommonUtils.isDefaultMaintainer(codelistDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canCodeCodeBeEdited(CodelistMetamacDto codelistDto) {
        if (codelistDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(codelistDto.getMaintainer());
    }
}
