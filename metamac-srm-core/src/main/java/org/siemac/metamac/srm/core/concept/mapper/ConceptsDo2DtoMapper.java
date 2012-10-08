package org.siemac.metamac.srm.core.concept.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

public interface ConceptsDo2DtoMapper {

    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac conceptSchemeVersion);
    public List<ConceptSchemeMetamacDto> conceptSchemeMetamacDoListToDtoList(List<ConceptSchemeVersionMetamac> conceptSchemeVersions);

    public ConceptMetamacDto conceptMetamacDoToDto(ConceptMetamac conceptMetamac);
    public List<ConceptMetamacDto> conceptMetamacDoListToDtoList(List<ConceptMetamac> conceptMetamacs);
    public List<ItemHierarchyDto> conceptMetamacDoListToItemHierarchyDtoList(List<ConceptMetamac> conceptMetamacs);

    public ConceptTypeDto conceptTypeDoToDto(ConceptType conceptType);
    public List<ConceptTypeDto> conceptTypeDoListToConceptTypeDtoList(List<ConceptType> conceptTypes);
}
