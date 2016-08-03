package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;

public interface DsdDimensionsTabUiHandlers extends BaseUiHandlers {

    void createDefaultDimension(String dsdUrn, TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionType);
    void saveDimension(DimensionComponentDto dimensionComponentDto);
    void deleteDimensions(List<DimensionComponentDto> dimensionComponentDtos);

    void retrieveConceptSchemes(TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionTypeEnum, int firstResult, int maxResults,
            ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConcepts(TypeDimensionComponent dimensionType, SpecialDimensionTypeEnum specialDimensionType, int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveConceptSchemesForMeasureDimensionEnumeratedRepresentation(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);

    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria, boolean isSpatialDimension);

    void retrieveConceptSchemesForDimensionRole(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConceptsForDimensionRole(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveConceptSchemeEnumeratedRepresentationFromConcept(String conceptUrn);
}
