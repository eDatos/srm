package org.siemac.metamac.srm.web.dsd.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;

public interface DsdPrimaryMeasureTabUiHandlers extends BaseUiHandlers {

    void savePrimaryMeasure(ComponentDto componentDto);

    void retrieveConceptSchemes(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConcepts(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);
    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria);
}
