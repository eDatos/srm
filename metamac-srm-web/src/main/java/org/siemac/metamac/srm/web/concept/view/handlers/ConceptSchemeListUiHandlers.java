package org.siemac.metamac.srm.web.concept.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface ConceptSchemeListUiHandlers extends BaseUiHandlers {

    void goToConceptScheme(String urn);
    void createConceptScheme(ConceptSchemeMetamacDto conceptSchemeDto);
    void deleteConceptSchemes(List<String> urns);
    void retrieveConceptSchemes(int firstResult, int maxResults, ConceptSchemeWebCriteria criteria);
    void exportConceptSchemes(List<String> urns, ExportDetailEnum detail, ExportReferencesEnum references);
    void cancelValidity(List<String> urn);

    void retrieveStatisticalOperations(int firstResult, int maxResults, String operation);

    // Search
    void retrieveStatisticalOperationsForSearchSection(int firstResult, int maxResults, String criteria);
}
