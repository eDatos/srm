package org.siemac.metamac.srm.web.organisation.view.handlers;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface OrganisationUiHandlers extends UiHandlers {

    void retrieveOrganisation(String organisationUrn);
    void saveOrganisation(OrganisationMetamacDto organisationDto);

}
