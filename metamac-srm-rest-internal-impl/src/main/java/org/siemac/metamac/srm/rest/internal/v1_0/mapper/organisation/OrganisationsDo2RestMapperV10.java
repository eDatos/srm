package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

public interface OrganisationsDo2RestMapperV10 {

    // Agencies
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes toAgencySchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme toAgencyScheme(OrganisationSchemeVersionMetamac source);
    public void toAgencyScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme target);

    public Agencies toAgencies(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Agency toAgency(OrganisationMetamac source);
    
    
    // Organisation Units
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitSchemes toOrganisationUnitSchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source);
    public void toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme target);

    public OrganisationUnits toOrganisationUnits(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public OrganisationUnit toOrganisationUnit(OrganisationMetamac source);

}