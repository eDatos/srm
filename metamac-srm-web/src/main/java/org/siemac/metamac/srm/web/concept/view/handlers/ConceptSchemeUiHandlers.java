package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptDto;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeUiHandlers extends UiHandlers {

    // Schemes

    void retrieveConceptScheme(String conceptSchemeUrn);
    void retrieveConceptListByScheme(String conceptSchemeUrn);
    void retrieveConceptSchemeHistoryList(String conceptSchemeUrn);
    void saveConceptScheme(MetamacConceptSchemeDto conceptScheme);
    void goToConceptScheme(String urn);
    void retrieveStatisticalOperations(int firstResult, int maxResults, String operation);

    // Concepts

    void createConcept(MetamacConceptDto conceptDto);
    void deleteConcepts(List<Long> conceptIds);
    void goToConcept(String urn);

    // Life cycle

    void sendToProductionValidation(Long id);
    void sendToDiffusionValidation(Long id);
    void rejectValidation(Long id);
    void publishInternally(Long id);
    void publishExternally(Long id);
    void versioning(Long id);
}
