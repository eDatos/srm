package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource.
 * Metadata of type {@link InternationalStringDto} are always editable (that's why they are not specified in this class), but only to add new translations.
 */
public class ConceptsFormUtils {

    // ---------------------------------------------------------------------------------------------
    // CONCEPT SCHEMES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canConceptSchemeCodeBeEdited(ConceptSchemeMetamacDto conceptSchemeDto) {
        if (conceptSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(conceptSchemeDto.getLifeCycle().getProcStatus(), conceptSchemeDto.getVersionLogic())
                && CommonUtils.isDefaultMaintainer(conceptSchemeDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canConceptCodeBeEdited(ConceptSchemeMetamacDto conceptSchemeDto) {
        if (conceptSchemeDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(conceptSchemeDto.getMaintainer());
    }

    // REPRESENTATION TYPE

    public static boolean canConceptRepresentationTypeBeEdited(ConceptSchemeMetamacDto conceptSchemeDto) {
        if (conceptSchemeDto == null) {
            return false;
        }
        return CommonUtils.isDefaultMaintainer(conceptSchemeDto.getMaintainer());
    }
}
