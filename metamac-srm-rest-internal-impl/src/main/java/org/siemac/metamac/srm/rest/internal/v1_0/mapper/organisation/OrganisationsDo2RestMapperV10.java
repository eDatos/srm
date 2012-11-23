package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

public interface OrganisationsDo2RestMapperV10 {

    public org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes toAgencySchemes(PagedResult<OrganisationSchemeVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);

    // TODO
    // public org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme toAgencyScheme(OrganisationSchemeVersionMetamac source);
    // public void toAgencyScheme(OrganisationSchemeVersionMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme target);

    // public Agencies toAgencies(PagedResult<OrganisationMetamac> categoriesEntitiesResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    // public Agency toAgency(OrganisationMetamac source);
}