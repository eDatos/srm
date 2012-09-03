package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

public interface ConceptUiHandlers extends BaseConceptUiHandlers {

    void retrieveConcept(String conceptUrn);
    void saveConcept(ConceptMetamacDto conceptDto);

    void retrieveConceptListByScheme(String conceptSchemeUrn);

}
