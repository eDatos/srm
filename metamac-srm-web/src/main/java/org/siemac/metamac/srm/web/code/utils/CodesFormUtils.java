package org.siemac.metamac.srm.web.code.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource and on whether the resource is in a
 * temporal version or not.
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
                && CommonUtils.canSdmxMetadataAndStructureBeModified(codelistDto);
    }

    // VARIABLE

    public static boolean canCodelistVariableBeEdited(CodelistMetamacDto codelistDto) {
        if (codelistDto == null) {
            return false;
        }
        return !VersionUtil.isTemporalVersion(codelistDto.getVersionLogic());
    }

    // ---------------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canCodeCodeBeEdited(CodelistMetamacDto codelistDto) {
        if (codelistDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(codelistDto);
    }

    // ---------------------------------------------------------------------------------------------
    // VARIABLE
    // ---------------------------------------------------------------------------------------------

    // IS GEOGRAPHICAL

    public static boolean canVariableIsGeographicalBeEdited(VariableDto variableDto) {
        if (variableDto == null) {
            return false;
        }
        // when the variable type is geographical, can always be turned into a non geographical variable
        if (VariableTypeEnum.GEOGRAPHICAL.equals(variableDto.getType())) {
            return true;
        } else {
            // the metadata can only be edited when the variable has no variable elements
            return !BooleanUtils.isTrue(variableDto.getHasVariableElements());
        }
    }
}
