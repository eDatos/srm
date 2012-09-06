package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface BaseConceptUiHandlers extends UiHandlers {

    void saveConcept(ConceptMetamacDto conceptDto);
    void saveConcept(ConceptMetamacDto conceptDto, List<String> relatedConcepts);
    void deleteConcept(String urn);
    void goToConcept(String urn);

}
