package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

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
}
