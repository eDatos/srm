package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.mvp.client.UiHandlers;


public interface ConceptSchemeUiHandlers extends UiHandlers {
    
    void retrieveConceptScheme(String conceptSchemeCode);
    
    void saveConceptScheme(ConceptSchemeDto conceptScheme);

    /* Life cycle */

    void sendToPendingPublication(Long id);

    void rejectValidation(Long id);

    void publishInternally(Long id);

    void publishExternally(Long id);

    void versioning(Long id);
}
