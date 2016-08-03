package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

public interface BaseConceptUiHandlers extends CategorisationUiHandlers {

    void saveConcept(ConceptMetamacDto conceptDto);
    void saveConcept(ConceptMetamacDto conceptDto, List<String> roles, List<String> relatedConcepts);
    void deleteConcept(ItemVisualisationResult itemVisualisationResult);
    void goToConceptScheme(String urn);
    void goToConcept(String urn);
}
