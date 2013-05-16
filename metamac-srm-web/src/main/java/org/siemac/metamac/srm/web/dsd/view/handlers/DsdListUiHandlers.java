package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface DsdListUiHandlers extends BaseUiHandlers {

    void retrieveDsdList(int firstResult, int maxResults, DataStructureDefinitionWebCriteria criteria);
    void goToDsd(String urn);
    void saveDsd(DataStructureDefinitionMetamacDto dataStructureDefinitionDto);
    void deleteDsds(List<String> urns);

    void retrieveStatisticalOperations(int firstResult, int maxResults, String criteria);

    void exportDsd(String urn);

    void cancelValidity(List<String> urns);

    // Search
    void retrieveStatisticalOperationsForSearchSection(int firstResult, int maxResults, String criteria);
    void retrieveDimensionConceptsForSearchSection(int firstResult, int maxResults, String criteria);
    void retrieveAttributeConceptsForSearchSection(int firstResult, int maxResults, String criteria);
}
