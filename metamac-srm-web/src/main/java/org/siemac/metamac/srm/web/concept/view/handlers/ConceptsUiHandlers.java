package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface ConceptsUiHandlers extends BaseUiHandlers {

    void goToConcept(String conceptSchemeUrn, String conceptUrn);
    void retrieveConcepts(int firstResult, int maxResults, ConceptWebCriteria criteria);
}
