package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumers;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviders;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisations;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AgencyType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataConsumerType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataProviderType;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.OrganisationUnitType;

public interface OrganisationsDo2RestMapperV10 {

    // Organisations (Global search)
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes toOrganisationSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationScheme toOrganisationScheme(OrganisationSchemeVersionMetamac source);
    public void toOrganisationScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationScheme target);

    public Organisations toOrganisations(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Organisation toOrganisation(OrganisationMetamac source);

    // Agencies
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencySchemes toAgencySchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID, String resourceID,
            String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme toAgencyScheme(OrganisationSchemeVersionMetamac source);
    public void toAgencyScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme target);

    public Agencies toAgencies(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Agency toAgency(OrganisationMetamac source);
    public void toAgency(com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation source, AgencyType target);

    // Organisation Units
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitSchemes toOrganisationUnitSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source);
    public void toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme target);

    public OrganisationUnits toOrganisationUnits(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public OrganisationUnit toOrganisationUnit(OrganisationMetamac source);
    public void toOrganisationUnit(com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation source, OrganisationUnitType target);

    // Data providers
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderSchemes toDataProviderSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderScheme toDataProviderScheme(OrganisationSchemeVersionMetamac source);
    public void toDataProviderScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderScheme target);

    public DataProviders toDataProviders(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public DataProvider toDataProvider(OrganisationMetamac source);
    public void toDataProvider(com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation source, DataProviderType target);

    // Data consumers
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerSchemes toDataConsumerSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerScheme toDataConsumerScheme(OrganisationSchemeVersionMetamac source);
    public void toDataConsumerScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerScheme target);

    public DataConsumers toDataConsumers(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public DataConsumer toDataConsumer(OrganisationMetamac source);
    public void toDataConsumer(com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation source, DataConsumerType target);
}