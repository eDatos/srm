package org.siemac.metamac.srm.core.mapper;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

public interface Do2DtoMapper {

    //TODO cambiar a tipos metamac
    public <U extends ComponentDto> U componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component);
    public <U extends ComponentListDto> U componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList);

    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac);
    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac);
    
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersion dataStructureDefinitionVersion);

    
    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac conceptSchemeVersion);
    public List<ConceptSchemeMetamacDto> conceptSchemeMetamacDoListToDtoList(List<ConceptSchemeVersionMetamac> conceptSchemeVersions);

    public ConceptMetamacDto conceptMetamacDoToDto(ConceptMetamac conceptMetamac);
    public List<ConceptMetamacDto> conceptMetamacDoListToDtoList(List<ConceptMetamac> conceptMetamacs);
    public List<ItemHierarchyDto> conceptMetamacDoListToItemHierarchyDtoList(List<ConceptMetamac> conceptMetamacs);

    public ConceptTypeDto conceptTypeDoToDto(ConceptType conceptType);
    public List<ConceptTypeDto> conceptTypeDoListToConceptTypeDtoList(List<ConceptType> conceptTypes);
}
