package org.siemac.metamac.srm.core.mapper;

import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinition;
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
    public <U extends ComponentDto> U componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component) {
        return (U) do2DtoMapperSdmxSrm.componentToComponentDto(typeDozerCopyMode, component);
    }

    @Override
    public <U extends ComponentListDto> U componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList) {
        return (U) do2DtoMapperSdmxSrm.componentListToComponentListDto(typeDozerCopyMode, componentList);
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
    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptSchemeMetamacDto target = new ConceptSchemeMetamacDto();
        target.setProcStatus(source.getProcStatus());
        target.setType(source.getType());
        target.setRelatedOperation(do2DtoMapperSdmxSrm.externalItemToExternalItemDto(TypeDozerCopyMode.UPDATE, source.getRelatedOperation()));
        do2DtoMapperSdmxSrm.conceptSchemeDoToDto(source, target);
        return target;
    }

}
