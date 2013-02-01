package org.siemac.metamac.srm.web.dsd.view.handlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdPrimaryMeasureTabUiHandlers extends UiHandlers {

    void savePrimaryMeasure(ComponentDto componentDto);

    void retrieveConceptSchemes(int firstResult, int maxResults);
    void retrieveConcepts(int firstResult, int maxResults, String criteria, String conceptSchemeUrn);
    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria);
}
