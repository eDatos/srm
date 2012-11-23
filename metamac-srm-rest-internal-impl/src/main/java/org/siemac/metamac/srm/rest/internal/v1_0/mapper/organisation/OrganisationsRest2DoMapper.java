package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;

public interface OrganisationsRest2DoMapper {

    public RestCriteria2SculptorCriteria<OrganisationSchemeVersionMetamac> getOrganisationSchemeCriteriaMapper();
    public RestCriteria2SculptorCriteria<OrganisationMetamac> getOrganisationCriteriaMapper();
}
