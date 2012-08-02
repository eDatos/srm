package org.siemac.metamac.srm.web.concept.view.handlers;

import org.siemac.metamac.srm.core.concept.dto.MetamacConceptDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptUiHandlers extends UiHandlers {

    void retrieveConcept(String conceptUrn);
    void saveConcept(MetamacConceptDto conceptDto);

}
