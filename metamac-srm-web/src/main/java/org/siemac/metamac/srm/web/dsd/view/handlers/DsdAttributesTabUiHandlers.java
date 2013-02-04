package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdAttributesTabUiHandlers extends UiHandlers {

    void retrieveDsd(String urn);
    void saveAttribute(DataAttributeDto dataAttributeDto);
    void deleteAttributes(List<DataAttributeDto> dataAttributeDtos);

    void retrieveConceptSchemes(int firstResult, int maxResults);
    void retrieveConcepts(int firstResult, int maxResults, String criteria, String conceptSchemeUrn);

    void retrieveConceptSchemesForAttributeRole(int firstResult, int maxResults);
    void retrieveConceptsForAttributeRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn);
}
