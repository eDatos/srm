package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

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

    // NAME

    public static boolean canOrganisationSchemeNameBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer());
    }

    // DESCRIPTION

    public static boolean canOrganisationSchemeDescriptionBeEdited(OrganisationSchemeMetamacDto organisationSchemeDto) {
        if (organisationSchemeDto == null) {
            return false;
        }
        return org.siemac.metamac.srm.web.client.utils.CommonUtils.isDefaultMaintainer(organisationSchemeDto.getMaintainer());
    }
}
