package org.siemac.metamac.srm.web.dsd.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;

public interface DsdAttributesTabUiHandlers extends BaseUiHandlers {

    void saveAttribute(DataAttributeDto dataAttributeDto);
    void deleteAttributes(List<DataAttributeDto> dataAttributeDtos);

    void retrieveConceptSchemes(SpecialAttributeTypeEnum specialAttributeTypeEnum, int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConcepts(SpecialAttributeTypeEnum specialAttributeTypeEnum, int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveConceptSchemesForAttributeRole(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);
    void retrieveConceptsForAttributeRole(int firstResult, int maxResults, ConceptWebCriteria conceptWebCriteria);

    void retrieveCodelistsForEnumeratedRepresentation(int firstResult, int maxResults, CodelistWebCriteria codelistWebCriteria, SpecialAttributeTypeEnum attributeType);

    void retrieveConceptSchemesForEnumeratedRepresentation(int firstResult, int maxResults, ConceptSchemeWebCriteria conceptSchemeWebCriteria);

    void retrieveDefaultConcept(SpecialAttributeTypeEnum specialAttributeTypeEnum, String dsdUrn);
    void retrieveConceptSchemeEnumeratedRepresentationFromConcept(String conceptUrn);
}
