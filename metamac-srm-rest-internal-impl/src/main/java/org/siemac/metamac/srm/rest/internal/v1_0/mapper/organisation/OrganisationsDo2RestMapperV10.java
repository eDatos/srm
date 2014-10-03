package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
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

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public interface OrganisationsDo2RestMapperV10 {

    // Organisations (Global search)
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes toOrganisationSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationScheme toOrganisationScheme(OrganisationSchemeVersionMetamac source) throws MetamacException;

    public Organisations toOrganisations(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Organisations toOrganisations(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion);
    public Organisation toOrganisation(OrganisationMetamac source) throws MetamacException;

    // Agencies
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencySchemes toAgencySchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID, String resourceID,
            String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme toAgencyScheme(OrganisationSchemeVersionMetamac source) throws MetamacException;

    public Agencies toAgencies(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Agencies toAgencies(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion);
    public Agency toAgency(OrganisationMetamac source) throws MetamacException;

    // Organisation Units
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitSchemes toOrganisationUnitSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source);

    public OrganisationUnits toOrganisationUnits(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public OrganisationUnits toOrganisationUnits(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion);
    public OrganisationUnit toOrganisationUnit(OrganisationMetamac source);

    // Data providers
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderSchemes toDataProviderSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderScheme toDataProviderScheme(OrganisationSchemeVersionMetamac source);

    public DataProviders toDataProviders(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public DataProviders toDataProviders(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion);
    public DataProvider toDataProvider(OrganisationMetamac source);

    // Data consumers
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerSchemes toDataConsumerSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID,
            String resourceID, String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerScheme toDataConsumerScheme(OrganisationSchemeVersionMetamac source);

    public DataConsumers toDataConsumers(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public DataConsumers toDataConsumers(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion);
    public DataConsumer toDataConsumer(OrganisationMetamac source);

    public ResourceLink toOrganisationSchemeSelfLink(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum organisationSchemeType);
    public String toOrganisationSchemeManagementApplicationLink(String organisationSchemeUrn, OrganisationSchemeTypeEnum organisationSchemeType);
}