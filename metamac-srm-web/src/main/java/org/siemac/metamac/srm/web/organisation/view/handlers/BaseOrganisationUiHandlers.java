package org.siemac.metamac.srm.web.organisation.view.handlers;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface BaseOrganisationUiHandlers extends UiHandlers {

    void saveOrganisation(OrganisationMetamacDto organisationDto);
    void deleteOrganisation(ItemDto itemDto);
    void goToOrganisation(String urn);

}
