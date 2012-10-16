package org.siemac.metamac.srm.web.organisation.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface OrganisationUiHandlers extends UiHandlers {

    void retrieveOrganisation(String organisationUrn);
    void updateOrganisation(OrganisationMetamacDto organisationDto);
    void updateContacts(List<ContactDto> contacts);

}
