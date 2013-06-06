package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource and on whether the resource is in a
 * temporal version or not (ORGANISATIONS DO NOT HAVE TEMPORAL VERSIONS).
 * Metadata of type {@link InternationalStringDto} are always editable (that's why they are not specified in this class), but only to add new translations.
 */
public class OrganisationsFormUtils {

    // ---------------------------------------------------------------------------------------------
    // ORGANISATION SCHEMES
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canOrganisationSchemeCodeBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        // CODE cannot be modified if status is INTERNALLY_PUBLISHED or EXTERNALLY_PUBLISHED, or if version is greater than VERSION_INITIAL_VERSION (01.000)
        // and if the OrganisationType is not AGENCY_SCHEME, DATA_PROVIDER_SCHEME or DATA_CONSUMER_SCHEME
        // and maintainer is the default one
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canCodeBeEdited(organisationSchemeDto.getLifeCycle().getProcStatus(), organisationSchemeDto.getVersionLogic())
                && (!CommonUtils.isAgencyScheme(organisationSchemeDto.getType()) && !CommonUtils.isDataProviderScheme(organisationSchemeDto.getType()) && !CommonUtils
                        .isDataConsumerScheme(organisationSchemeDto.getType())) && org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);
    }

    // ---------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canOrganisationCodeBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto, OrganisationMetamacDto organisationDto) {
        if (organisationSchemeDto == null || organisationDto == null) {
            return false;
        }
        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isMaintainableArtefactPublished(organisationSchemeDto.getLifeCycle().getProcStatus())) {
            return false;
        }
        if (!org.siemac.metamac.srm.web.client.utils.CommonUtils.hasDefaultMaintainerOrIsAgencySchemeSdmxResource(organisationSchemeDto)) {
            return false;
        }

        if (CommonUtils.isOrganisationUnitScheme(organisationSchemeDto.getType())) {
            return org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);
        } else {
            if (BooleanUtils.isTrue(organisationDto.getSpecialOrganisationHasBeenPublished())) {
                return false;
            } else {
                return true;
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // CONTACT
    // ---------------------------------------------------------------------------------------------

    public static boolean canContactUrlBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto, ContactDto contactDto) {
        return canContactMetadataBeEdited(organisationSchemeDto, contactDto);
    }

    public static boolean canContactTelephoneBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto, ContactDto contactDto) {
        return canContactMetadataBeEdited(organisationSchemeDto, contactDto);
    }

    public static boolean canContactEmailBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto, ContactDto contactDto) {
        return canContactMetadataBeEdited(organisationSchemeDto, contactDto);
    }

    public static boolean canContactFaxBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto, ContactDto contactDto) {
        return canContactMetadataBeEdited(organisationSchemeDto, contactDto);
    }

    private static boolean canContactMetadataBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto, ContactDto contactDto) {
        if (organisationSchemeDto == null) {
            return false;
        }

        if (org.siemac.metamac.srm.web.client.utils.CommonUtils.isMaintainableArtefactPublished(organisationSchemeDto.getLifeCycle().getProcStatus())) {
            return false;
        }

        if (org.siemac.metamac.srm.web.organisation.utils.CommonUtils.isOrganisationUnitScheme(organisationSchemeDto)) {

            // ORGANISATION UNIT

            return org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);

        } else {

            // DATA PROVIDER, DATA CONSUMER and AGENCY SCHEME

            if (BooleanUtils.isTrue(contactDto.getIsImported())) {
                // if the contact was imported, cannot be modified
                return false;
            } else {
                return true;
            }
        }
    }
}
