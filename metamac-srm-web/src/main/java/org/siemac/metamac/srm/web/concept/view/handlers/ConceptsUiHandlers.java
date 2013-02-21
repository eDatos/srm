package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptsUiHandlers extends UiHandlers {

    void goToConcept(String conceptSchemeUrn, String conceptUrn);
    void retrieveConcepts(int firstResult, int maxResults, ConceptWebCriteria criteria);
}
