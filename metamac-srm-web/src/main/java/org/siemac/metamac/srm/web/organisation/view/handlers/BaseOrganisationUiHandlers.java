package org.siemac.metamac.srm.web.organisation.view.handlers;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;

public interface BaseOrganisationUiHandlers extends CategorisationUiHandlers {

    void createOrganisation(OrganisationMetamacDto organisationDto);
    void deleteOrganisation(ItemDto itemDto);
    void goToOrganisation(String urn);
}
