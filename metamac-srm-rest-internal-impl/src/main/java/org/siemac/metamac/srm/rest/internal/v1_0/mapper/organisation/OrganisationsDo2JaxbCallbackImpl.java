package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AgencySchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.AgencyType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataConsumerSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataConsumerType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataProviderSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.DataProviderType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationSchemesType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationUnitSchemeType;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.OrganisationUnitType;

@org.springframework.stereotype.Component("organisationsDo2JaxbCallbackMetamac")
public class OrganisationsDo2JaxbCallbackImpl implements OrganisationsDo2JaxbCallback {

    @Autowired
    private OrganisationsDo2RestMapperV10 organisationsDo2RestMapperV10;

    @Override
    public AgencySchemeType createAgencySchemeJaxb(OrganisationSchemeVersion sourceAgencyScheme) {
        // TODO
        // org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme target = new org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme();
        // organisationsDo2RestMapperV10.toCategoryScheme((CategorySchemeVersionMetamac) source, target);
        // return target;
        return null;
    }

    @Override
    public DataConsumerSchemeType createDataConsumerSchemeJaxb(OrganisationSchemeVersion sourceDataConsumerScheme) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DataProviderSchemeType createDataProviderSchemeJaxb(OrganisationSchemeVersion sourceDataProviderScheme) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrganisationUnitSchemeType createOrganisationUnitSchemeJaxb(OrganisationSchemeVersion sourceDataProviderScheme) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AgencyType createAgencyJaxb(Organisation sourceAgency) {
        return new AgencyType();
    }

    @Override
    public DataConsumerType createDataConsumerJaxb(Organisation sourceDataConsumer) {
        return new DataConsumerType();
    }

    @Override
    public DataProviderType createDataProviderJaxb(Organisation sourceDataProvider) {
        return new DataProviderType();
    }

    @Override
    public OrganisationUnitType createOrganisationUnitJaxb(Organisation sourceOrganisationUnit) {
        return new OrganisationUnitType();
    }

    @Override
    public OrganisationSchemesType createOrganisationSchemesJaxb(List<OrganisationSchemeVersion> sourceList) {
        throw new IllegalArgumentException("createOrganisationSchemesJaxb not supported");
    }
}