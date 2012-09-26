package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationsMetamacDoMocks extends OrganisationsDoMocks {

    public static OrganisationSchemeVersionMetamac mockOrganisationScheme(OrganisationSchemeTypeEnum type) {

        OrganisationSchemeVersionMetamac organisationSchemeVersion = new OrganisationSchemeVersionMetamac(type);
        mockOrganisationScheme(organisationSchemeVersion);
        return organisationSchemeVersion;
    }

    // public static OrganisationMetamac mockOrganisation(OrganisationType organisationType) {
    //
    // OrganisationMetamac organisation = new OrganisationMetamac();
    // mockOrganisation(organisation);
    // return organisation;
    // }

    public static ExternalItem mockOperationExternalItem(String code) {
        return new ExternalItem(code, "http://" + code, "urn:" + code, TypeExternalArtefactsEnum.STATISTICAL_OPERATION);
    }

}
