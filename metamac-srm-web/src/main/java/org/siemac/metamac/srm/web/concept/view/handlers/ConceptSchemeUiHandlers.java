package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.domain.concept.dto.ConceptDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeUiHandlers extends UiHandlers {

    // Schemes

    void retrieveConceptScheme(String conceptSchemeUrn);
    void retrieveConceptListByScheme(String conceptSchemeUrn);
    void saveConceptScheme(ConceptSchemeDto conceptScheme);

    // Concepts

    void createConcept(ConceptDto conceptDto);
    void deleteConcepts(List<Long> conceptIds);
    void goToConcept(String code);

    // Life cycle

    void sendToPendingPublication(Long id);
    void rejectValidation(Long id);
    void publishInternally(Long id);
    void publishExternally(Long id);
    void versioning(Long id);
}
