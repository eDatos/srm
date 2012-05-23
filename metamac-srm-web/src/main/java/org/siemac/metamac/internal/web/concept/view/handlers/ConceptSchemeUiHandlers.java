package org.siemac.metamac.internal.web.concept.view.handlers;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeUiHandlers extends UiHandlers {

    void saveConceptScheme(ConceptSchemeDto conceptScheme);

    /* Life cycle */

    void sendToPendingPublication(String conceptSchemeUuid);

    void rejectValidation(String conceptSchemeUuid);

    void publishInternally(String conceptSchemeUuid);

    void publishExternally(String conceptSchemeUuid);

    void versioning(String conceptSchemeUuid);

}
