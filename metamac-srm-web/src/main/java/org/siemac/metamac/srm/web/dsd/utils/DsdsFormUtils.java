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

    // CONCEPT

    public static boolean canPrimaryMeasureConceptBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // REPRESENTATION TYPE

    public static boolean canPrimaryMeasureRepresentationTypeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ENUMERATED REPRESENTATION

    public static boolean canPrimaryMeasureEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // DIMENSIONS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canDimensionCodeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto, String typeDimensionComponent) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }

        return !org.siemac.metamac.srm.web.dsd.utils.CommonUtils.isDimensionTypeTimeDimension(typeDimensionComponent)
                && CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // CONCEPT

    public static boolean canDimensionConceptBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ROLE

    public static boolean canDimensionRoleBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // REPRESENTATION TYPE

    public static boolean canDimensionRepresentationTypeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // CODELIST (ENUMERATED REPRESENTATION)

    public static boolean canDimensionCodelistEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // CONCEPT SCHEME (ENUMERATED REPRESENTATION)

    public static boolean canDimensionConceptSchemeEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canAttributeCodeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // USAGE STATUS

    public static boolean canAttributeUsageStatusBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // REPRESENTATION TYPE

    public static boolean canAttributeRepresentationTypeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // GROUP KEYS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canGroupKeysCodeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }

    // DIMENSIONS

    public static boolean canGroupKeysDimensionsBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(dataStructureDefinitionMetamacDto.getMaintainer());
    }
}
