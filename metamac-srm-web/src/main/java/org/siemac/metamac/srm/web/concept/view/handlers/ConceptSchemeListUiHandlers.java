package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeListUiHandlers extends UiHandlers {

    void goToConceptScheme(String code);
    void createConceptScheme(MetamacConceptSchemeDto conceptSchemeDto);
    void deleteConceptSchemes(List<Long> idsFromSelected);
    void retrieveConceptSchemes(int firstResult, int maxResults, String conceptScheme);

    void retrieveStatisticalOperations(int firstResult, int maxResults, String operation);
}
