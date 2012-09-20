package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface BaseConceptUiHandlers extends UiHandlers {

    void saveConcept(ConceptMetamacDto conceptDto);
    void saveConcept(ConceptMetamacDto conceptDto, List<String> relatedConcepts);
    void deleteConcept(ItemDto itemDto);
    void goToConcept(String urn);

}
