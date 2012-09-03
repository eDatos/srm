package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface BaseConceptUiHandlers extends UiHandlers {

    void createConcept(ConceptMetamacDto conceptDto);
    void deleteConcept(String urn);
    void goToConcept(String urn);

}
