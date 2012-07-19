package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.domain.concept.dto.ConceptDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeUiHandlers extends UiHandlers {

    // Schemes

    void retrieveConceptScheme(String conceptSchemeCode);
    void saveConceptScheme(ConceptSchemeDto conceptScheme);

    // Concepts

    void createConcept(ConceptDto conceptDto);

    // Life cycle

    void sendToPendingPublication(Long id);
    void rejectValidation(Long id);
    void publishInternally(Long id);
    void publishExternally(Long id);
    void versioning(Long id);
}
