package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

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
