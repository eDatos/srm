package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
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
    public AgencySchemeType createAgencySchemeJaxb(OrganisationSchemeVersion source) {
        return new org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme();
    }

    @Override
    public void fillAgencySchemeJaxb(OrganisationSchemeVersion source, AgencySchemeType target) {
        organisationsDo2RestMapperV10.toAgencyScheme((OrganisationSchemeVersionMetamac) source, (org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme) target);
    }

    @Override
    public DataConsumerSchemeType createDataConsumerSchemeJaxb(OrganisationSchemeVersion source) {
        return new org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumerScheme();
    }

    @Override
    public void fillDataConsumerSchemeJaxb(OrganisationSchemeVersion source, DataConsumerSchemeType target) {
        organisationsDo2RestMapperV10.toDataConsumerScheme((OrganisationSchemeVersionMetamac) source, (org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumerScheme) target);
    }

    @Override
    public DataProviderSchemeType createDataProviderSchemeJaxb(OrganisationSchemeVersion source) {
        return new org.siemac.metamac.rest.srm_internal.v1_0.domain.DataProviderScheme();
    }

    @Override
    public void fillDataProviderSchemeJaxb(OrganisationSchemeVersion source, DataProviderSchemeType target) {
        organisationsDo2RestMapperV10.toDataProviderScheme((OrganisationSchemeVersionMetamac) source, (org.siemac.metamac.rest.srm_internal.v1_0.domain.DataProviderScheme) target);

    }

    @Override
    public OrganisationUnitSchemeType createOrganisationUnitSchemeJaxb(OrganisationSchemeVersion source) {
        return new org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme();
    }

    @Override
    public void fillOrganisationUnitSchemeJaxb(OrganisationSchemeVersion source, OrganisationUnitSchemeType target) {
        organisationsDo2RestMapperV10.toOrganisationUnitScheme((OrganisationSchemeVersionMetamac) source, (org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme) target);
    }

    @Override
    public AgencyType createAgencyJaxb(Organisation source) {
        return new AgencyType();
    }

    @Override
    public void fillAgencyJaxb(Organisation source, AgencyType target) {
        organisationsDo2RestMapperV10.toAgency(source, target);
    }

    @Override
    public DataConsumerType createDataConsumerJaxb(Organisation source) {
        return new DataConsumerType();
    }

    @Override
    public void fillDataConsumerJaxb(Organisation source, DataConsumerType target) {
        organisationsDo2RestMapperV10.toDataConsumer(source, target);
    }

    @Override
    public DataProviderType createDataProviderJaxb(Organisation source) {
        return new DataProviderType();
    }

    @Override
    public void fillDataProviderJaxb(Organisation source, DataProviderType target) {
        organisationsDo2RestMapperV10.toDataProvider(source, target);
    }

    @Override
    public OrganisationUnitType createOrganisationUnitJaxb(Organisation source) {
        return new OrganisationUnitType();
    }

    @Override
    public void fillOrganisationUnitJaxb(Organisation source, OrganisationUnitType target) {
        organisationsDo2RestMapperV10.toOrganisationUnit(source, target);
    }

    @Override
    public OrganisationSchemesType createOrganisationSchemesJaxb(List<OrganisationSchemeVersion> sourceList) {
        throw new IllegalArgumentException("createOrganisationSchemesJaxb not supported");
    }
}