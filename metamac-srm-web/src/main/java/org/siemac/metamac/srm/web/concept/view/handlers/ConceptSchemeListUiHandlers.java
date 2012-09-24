package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeListUiHandlers extends UiHandlers {

    void goToConceptScheme(String urn);
    void createConceptScheme(ConceptSchemeMetamacDto conceptSchemeDto);
    void deleteConceptSchemes(List<String> urns);
    void retrieveConceptSchemes(int firstResult, int maxResults, String conceptScheme);
    void cancelValidity(List<String> urn);

    void retrieveStatisticalOperations(int firstResult, int maxResults, String operation);
}
