package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource.
 * The metadata of type {@link InternationalStringDto} are always editable (that's why are not specified in this class), but only to specify new translations.
 */
public class DsdsFormUtils {

    // ---------------------------------------------------------------------------------------------
    // DSDs
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canDsdCodeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(dataStructureDefinitionMetamacDto.getLifeCycle().getProcStatus(),
                dataStructureDefinitionMetamacDto.getVersionLogic())
                && CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // PRIMARY MEASURE
    // ---------------------------------------------------------------------------------------------

    public static boolean canPrimaryMeasureConceptBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // DIMENSIONS
    // ---------------------------------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // ---------------------------------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    // GROUP KEYS
    // ---------------------------------------------------------------------------------------------

}
