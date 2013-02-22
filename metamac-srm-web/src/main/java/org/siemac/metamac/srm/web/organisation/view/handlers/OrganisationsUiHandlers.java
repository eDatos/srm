package org.siemac.metamac.srm.web.organisation.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.OrganisationWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.gwtplatform.mvp.client.UiHandlers;

public interface OrganisationsUiHandlers extends UiHandlers {

    void goToOrganisation(String organisationSchemeUrn, String organisationUrn, OrganisationTypeEnum organisationTypeEnum);
    void retrieveOrganisations(int firstResult, int maxResults, OrganisationWebCriteria criteria);
}
