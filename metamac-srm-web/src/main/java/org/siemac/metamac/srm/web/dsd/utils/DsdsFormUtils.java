package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource and on whether the resource is in a
 * temporal version or not.
 * Metadata of type {@link InternationalStringDto} are always editable (that's why they are not specified in this class), but only to add new translations.
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
                && CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // ---------------------------------------------------------------------------------------------
    // PRIMARY MEASURE
    // ---------------------------------------------------------------------------------------------

    // CONCEPT

    public static boolean canPrimaryMeasureConceptBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // REPRESENTATION TYPE

    public static boolean canPrimaryMeasureRepresentationTypeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // ENUMERATED REPRESENTATION

    public static boolean canPrimaryMeasureEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
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
                && CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // CONCEPT

    public static boolean canDimensionConceptBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // ROLE

    public static boolean canDimensionRoleBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // REPRESENTATION TYPE

    public static boolean canDimensionRepresentationTypeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // CODELIST (ENUMERATED REPRESENTATION)

    public static boolean canDimensionCodelistEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // CONCEPT SCHEME (ENUMERATED REPRESENTATION)

    public static boolean canDimensionConceptSchemeEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // ---------------------------------------------------------------------------------------------
    // ATTRIBUTES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canAttributeCodeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // CONCEPT

    public static boolean canAttributeConceptBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // USAGE STATUS

    public static boolean canAttributeUsageStatusBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // ROLE

    public static boolean canAttributeRoleBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // RELATED TO

    public static boolean canAttributeRelatedToBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // GROUP KEYS FOR DIMENSION RELATIONSHIP

    public static boolean canAttributeGroupKeysForDimensionRelationshipBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // DIMENSIONS FOR DIMENSION RELATIONSHIP

    public static boolean canAttributeDimensionsForDimensionRelationshipBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // GROUP KEYS FOR GROUP RELATIONSHIP

    public static boolean canAttributeGroupKeysForGroupRelationshipBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // REPRESENTATION TYPE

    public static boolean canAttributeRepresentationTypeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // CODELIST (ENUMERATED REPRESENTATION)

    public static boolean canAttributeCodelistEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // CONCEPT SCHEME (ENUMERATED REPRESENTATION)

    public static boolean canAttributeConceptSchemeEnumeratedRepresentationBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // ---------------------------------------------------------------------------------------------
    // GROUP KEYS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canGroupKeysCodeBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }

    // DIMENSIONS

    public static boolean canGroupKeysDimensionsBeEdited(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        if (dataStructureDefinitionMetamacDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(dataStructureDefinitionMetamacDto);
    }
}
