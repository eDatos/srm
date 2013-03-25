package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.gwtplatform.mvp.client.UiHandlers;

public interface DsdDimensionsTabUiHandlers extends UiHandlers {

    void saveDimension(DimensionComponentDto dimensionComponentDto);
    void deleteDimensions(List<DimensionComponentDto> dimensionComponentDtos);

    void retrieveConceptSchemes(TypeDimensionComponent dimensionType, int firstResult, int maxResults);
    void retrieveConcepts(TypeDimensionComponent dimensionType, int firstResult, int maxResults, String criteria, String conceptSchemeUrn);

    void retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(int firstResult, int maxResults, String criteria);
    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria, String conceptUrn);

    void retrieveConceptSchemesForDimensionRole(int firstResult, int maxResults);
    void retrieveConceptsForDimensionRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn);
}
