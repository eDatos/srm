package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

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

    public static boolean canOrganisationCodeBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null || !org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto)) {
            return false;
        }
        // If organisation type is AGENCY, code can never be modified
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeDto.getType())) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);
    }

    // ---------------------------------------------------------------------------------------------
    // CONTACT
    // ---------------------------------------------------------------------------------------------

    public static boolean canContactUrlBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);
    }

    public static boolean canContactTelephoneBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);
    }

    public static boolean canContactEmailBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);
    }

    public static boolean canContactFaxBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.canSdmxMetadataAndStructureBeModified(organisationSchemeDto);
    }
}
