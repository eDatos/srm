package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource and on whether the resource is in a
 * temporal version or not.
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
                && CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeDto);
    }

    // TYPE

    public static boolean canConceptSchemeTypeBeEdited(ConceptSchemeMetamacDto conceptSchemeDto) {
        if (conceptSchemeDto == null) {
            return false;
        }

        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isMaintainableArtefactPublished(conceptSchemeDto.getLifeCycle().getProcStatus())) {

            // If it is published, cannot be edited
            return false;

        } else if (CommonUtils.isDefaultMaintainer(conceptSchemeDto.getMaintainer()) && !org.siemac.metamac.srm.web.client.utils.CommonUtils.isInitialVersion(conceptSchemeDto.getVersionLogic())) {

            // If it is a created artefact and the version is greater than 01.000, cannot be edited
            return false;

        } else {

            // There are more situations when the type cannot be edited, but will be check in the service (CORE)
            return true;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canConceptCodeBeEdited(ConceptSchemeMetamacDto conceptSchemeDto) {
        if (conceptSchemeDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeDto);
    }

    // REPRESENTATION TYPE

    public static boolean canConceptRepresentationTypeBeEdited(ConceptSchemeMetamacDto conceptSchemeDto) {
        if (conceptSchemeDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeDto);
    }

    // ENUMERATED REPRESENTATION

    public static boolean canConceptEnumeratedRepresentationBeEdited(ConceptSchemeMetamacDto conceptSchemeDto) {
        if (conceptSchemeDto == null) {
            return false;
        }
        return CommonUtils.canSdmxMetadataAndStructureBeModified(conceptSchemeDto);
    }
}
