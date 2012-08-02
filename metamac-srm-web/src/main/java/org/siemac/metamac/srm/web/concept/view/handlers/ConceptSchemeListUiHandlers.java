package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ConceptSchemeListUiHandlers extends UiHandlers {

    void goToConceptScheme(String code);

    void createConceptScheme(ConceptSchemeDto conceptSchemeDto);

    void deleteConceptSchemes(List<Long> idsFromSelected);

    void retrieveConceptSchemes(int firstResult, int maxResults, String conceptScheme);

}
