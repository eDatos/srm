package org.siemac.metamac.srm.core.organisation.serviceapi.utils;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationsMetamacDoMocks extends OrganisationsDoMocks {

    public static OrganisationSchemeVersionMetamac mockOrganisationScheme(OrganisationSchemeTypeEnum organisationSchemeType, OrganisationMetamac maintainer) {
        OrganisationSchemeVersionMetamac target = new OrganisationSchemeVersionMetamac();
        target.setOrganisationSchemeType(organisationSchemeType);
        mockOrganisationScheme(target, maintainer);
        return target;
    }

    public static OrganisationSchemeVersionMetamac mockOrganisationSchemeFixedValues(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) throws MetamacException {
        OrganisationSchemeVersionMetamac target = new OrganisationSchemeVersionMetamac();
        target.setOrganisationSchemeType(type);
        target.setLifeCycleMetadata(BaseDoMocks.mockLifeCycleExternallyPublished());
        mockOrganisationSchemeFixedValues(target, agencyID, resourceID, version, type);
        return target;
    }

    public static OrganisationMetamac mockOrganisation(OrganisationTypeEnum organisationType) {
        OrganisationMetamac target = new OrganisationMetamac();
        target.setOrganisationType(organisationType);
        mockOrganisation(target);
        return target;
    }

    public static OrganisationMetamac mockOrganisationFixedValues(String resourceID, OrganisationSchemeVersionMetamac itemSchemeVersion, OrganisationMetamac parent, OrganisationTypeEnum type)
            throws MetamacException {
        OrganisationMetamac target = new OrganisationMetamac();
        target.setOrganisationType(type);
        mockOrganisationFixedValues(target, resourceID, itemSchemeVersion, parent, type);
        return target;
    }
}