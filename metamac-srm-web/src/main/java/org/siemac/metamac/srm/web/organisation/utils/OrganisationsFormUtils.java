package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

/**
 * The methods of this class check if a SDMX metadata can me edited or not. The "editability" of a SDMX metadata usually depends on the maintainer of the resource.
 * The metadata of type {@link InternationalStringDto} are always editable (that's why are not specified in this class), but only to specify new translations.
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
                        .isDataConsumerScheme(organisationSchemeDto.getType())) && org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer());
    }

    // ---------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------------

    // CODE

    public static boolean canOrganisationCodeBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null || !org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer())) {
            return false;
        }
        // If organisation type is AGENCY, code can only be edited when organisation is not published
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeDto.getType())) {
            return !org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(organisationSchemeDto.getLifeCycle().getProcStatus());
        }
        return true;
    }

    // ---------------------------------------------------------------------------------------------
    // CONTACT
    // ---------------------------------------------------------------------------------------------

    public static boolean canContactUrlBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer());
    }

    public static boolean canContactTelephoneBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer());
    }

    public static boolean canContactEmailBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer());
    }

    public static boolean canContactFaxBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer());
    }
}
