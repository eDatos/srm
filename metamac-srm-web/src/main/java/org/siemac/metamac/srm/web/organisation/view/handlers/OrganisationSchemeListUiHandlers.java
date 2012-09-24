package org.siemac.metamac.srm.web.organisation.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface OrganisationSchemeListUiHandlers extends UiHandlers {

    void goToOrganisationScheme(String code);
    void createOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto);
    void deleteOrganisationSchemes(List<String> urns);
    void retrieveOrganisationSchemes(int firstResult, int maxResults);
    void retrieveOrganisationSchemes(int firstResult, int maxResults, String organisationScheme);
    void cancelValidity(List<String> urn);

}
