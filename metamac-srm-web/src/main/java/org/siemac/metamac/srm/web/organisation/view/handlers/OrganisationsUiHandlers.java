package org.siemac.metamac.srm.web.organisation.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public interface OrganisationsUiHandlers extends BaseUiHandlers {

    void goToOrganisation(String organisationSchemeUrn, String organisationUrn, OrganisationTypeEnum organisationTypeEnum);
    void retrieveOrganisations(int firstResult, int maxResults, OrganisationWebCriteria criteria);
}
