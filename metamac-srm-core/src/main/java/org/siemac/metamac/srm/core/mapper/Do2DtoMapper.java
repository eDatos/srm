package org.siemac.metamac.srm.core.mapper;

import org.dozer.DozerBeanMapper;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionExtendDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;

public interface Do2DtoMapper {

    public DozerBeanMapper getMapperCore(TypeDozerCopyMode typeDozerCopyMode);

    public <T extends ComponentDto> T componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component);

    public <T extends ComponentListDto> T componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList);

    public DataStructureDefinitionDto dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition);
    
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition);

    public ConceptSchemeDto conceptSchemeDoToDto(ConceptSchemeVersion conceptSchemeVersion);

}
