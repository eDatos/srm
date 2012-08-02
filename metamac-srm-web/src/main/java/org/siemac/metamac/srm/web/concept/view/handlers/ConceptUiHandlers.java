package org.siemac.metamac.srm.web.concept.view.handlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptUiHandlers extends UiHandlers {

    void retrieveConcept(String conceptUrn);
    void saveConcept(ConceptDto conceptDto);

}
