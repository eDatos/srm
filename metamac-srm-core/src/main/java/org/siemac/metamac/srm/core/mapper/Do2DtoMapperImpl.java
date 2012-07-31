package org.siemac.metamac.srm.core.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinition;
import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptSchemeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;


@org.springframework.stereotype.Component("do2DtoMapper")
public class Do2DtoMapperImpl implements Do2DtoMapper {

    @Autowired
    @Qualifier("do2DtoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.mapper.Do2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public <T extends ComponentDto> T componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component) {
        return do2DtoMapperSdmxSrm.componentToComponentDto(typeDozerCopyMode, component);
    }

    @Override
    public <T extends ComponentListDto> T componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList) {
        return do2DtoMapperSdmxSrm.componentListToComponentListDto(typeDozerCopyMode, componentList);
    }

    @Override
    public DataStructureDefinitionDto dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition) {
        return do2DtoMapperSdmxSrm.dataStructureDefinitionToDataStructureDefinitionDto(typeDozerCopyMode, dataStructureDefinition);
    }

    @Override
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition) {
        return do2DtoMapperSdmxSrm.dataStructureDefinitionToDataStructureDefinitionExtendDto(typeDozerCopyMode, dataStructureDefinition);
    }

    @Override
    public ConceptSchemeDto conceptSchemeDoToDto(ConceptSchemeVersion conceptSchemeVersion) {
        return do2DtoMapperSdmxSrm.conceptSchemeDoToDto(conceptSchemeVersion);
    }

}
