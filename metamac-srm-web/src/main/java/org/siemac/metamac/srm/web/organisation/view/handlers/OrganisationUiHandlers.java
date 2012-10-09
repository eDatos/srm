package org.siemac.metamac.srm.web.organisation.view.handlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface OrganisationUiHandlers extends UiHandlers {

    void retrieveOrganisation(String organisationUrn);
    void saveOrganisation(OrganisationDto organisationDto);

}
