package org.siemac.metamac.srm.core.concept.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

public interface ConceptsDo2DtoMapper {

    // Concept schemes
    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac source);
    public List<ConceptSchemeMetamacDto> conceptSchemeMetamacDoListToDtoList(List<ConceptSchemeVersionMetamac> sources);

    // Concepts
    public ConceptMetamacDto conceptMetamacDoToDto(ConceptMetamac source);
    public RelatedResourceDto conceptMetamacDoToRelatedResourceDto(ConceptMetamac source);
    public List<ConceptMetamacDto> conceptMetamacDoListToDtoList(List<ConceptMetamac> sources);
    public List<ItemHierarchyDto> conceptMetamacDoListToItemHierarchyDtoList(List<ConceptMetamac> sources);

    // Concept type
    public ConceptTypeDto conceptTypeDoToDto(ConceptType source);
    public List<ConceptTypeDto> conceptTypeDoListToConceptTypeDtoList(List<ConceptType> sources);
}
