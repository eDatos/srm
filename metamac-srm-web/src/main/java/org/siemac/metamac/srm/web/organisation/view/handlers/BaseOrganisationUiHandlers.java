package org.siemac.metamac.srm.web.organisation.view.handlers;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

public interface BaseOrganisationUiHandlers extends CategorisationUiHandlers {

    void createOrganisation(OrganisationMetamacDto organisationDto);
    void deleteOrganisation(ItemVisualisationResult itemVisualisationResult);
    void goToOrganisationScheme(String urn);
    void goToOrganisation(String urn);
}
