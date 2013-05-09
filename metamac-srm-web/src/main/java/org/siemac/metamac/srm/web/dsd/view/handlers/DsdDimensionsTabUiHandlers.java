package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;

public interface DsdDimensionsTabUiHandlers extends BaseUiHandlers {

    void createDefaultDimension(String dsdUrn, TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionType);
    void saveDimension(DimensionComponentDto dimensionComponentDto);
    void deleteDimensions(List<DimensionComponentDto> dimensionComponentDtos);

    void retrieveConceptSchemes(TypeDimensionComponent dimensionType, int firstResult, int maxResults);
    void retrieveConcepts(TypeDimensionComponent dimensionType, int firstResult, int maxResults, String criteria, String conceptSchemeUrn);

    void retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(int firstResult, int maxResults, String criteria);
    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, String criteria, String conceptUrn);

    void retrieveConceptSchemesForDimensionRole(int firstResult, int maxResults);
    void retrieveConceptsForDimensionRole(int firstResult, int maxResults, String criteria, String conceptSchemeUrn);
}
