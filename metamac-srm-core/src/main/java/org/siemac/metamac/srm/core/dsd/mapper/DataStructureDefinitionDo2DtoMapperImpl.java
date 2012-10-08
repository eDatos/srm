package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("dataStructureDefinitionDo2DtoMapper")
public class DataStructureDefinitionDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements DataStructureDefinitionDo2DtoMapper {

    // ------------------------------------------------------------
    // DSDs
    // ------------------------------------------------------------

    @Autowired
    private com.arte.statistic.sdmx.srm.core.structure.mapper.DataStructureDefinitionDo2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public <U extends ComponentDto> U componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component) {
        return (U) do2DtoMapperSdmxSrm.componentToComponentDto(typeDozerCopyMode, component);
    }

    @Override
    public <U extends ComponentListDto> U componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList) {
        return (U) do2DtoMapperSdmxSrm.componentListToComponentListDto(typeDozerCopyMode, componentList);
    }

    @Override
    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }

        DataStructureDefinitionMetamacDto target = dataStructureDefinitionVersionMetamacDoToDto(source, typeDozerCopyMode);

        return target;
    }

    @Override
    public DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDoToDto(DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }

        DataStructureDefinitionMetamacDto target = dataStructureDefinitionVersionMetamacDoToDto(source, TypeDozerCopyMode.COPY_ALL_METADATA);

        return target;
    }
    
    @Override
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersion dataStructureDefinitionVersion) {
        return do2DtoMapperSdmxSrm.dataStructureDefinitionToDataStructureDefinitionExtendDto(typeDozerCopyMode, dataStructureDefinitionVersion);
    }

    @Override
    public List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDoListToDtoList(List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacs) {
        List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos = new ArrayList<DataStructureDefinitionMetamacDto>();
        for (DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac : dataStructureDefinitionVersionMetamacs) {
            dataStructureDefinitionMetamacDtos.add(dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac));
        }
        return dataStructureDefinitionMetamacDtos;
    }
    
    private DataStructureDefinitionMetamacDto dataStructureDefinitionVersionMetamacDoToDto(DataStructureDefinitionVersionMetamac source, TypeDozerCopyMode typeDozerCopyMode) {
        DataStructureDefinitionMetamacDto target = new DataStructureDefinitionMetamacDto();
        target.setLifeCycle(lifeCycleDoToDto(source.getLifecycleMetadata()));
        do2DtoMapperSdmxSrm.dataStructureDefinitionDoToDto(typeDozerCopyMode, source, target);
        return target;
    }
}