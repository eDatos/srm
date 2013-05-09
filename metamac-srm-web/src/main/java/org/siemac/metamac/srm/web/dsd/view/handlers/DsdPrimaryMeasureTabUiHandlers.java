package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;

public interface DsdPrimaryMeasureTabUiHandlers extends BaseUiHandlers {

    void savePrimaryMeasure(ComponentDto componentDto);

    void retrieveConceptSchemes(int firstResult, int maxResults);
    void retrieveConcepts(int firstResult, int maxResults, String criteria, String conceptSchemeUrn);
    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria);
}
