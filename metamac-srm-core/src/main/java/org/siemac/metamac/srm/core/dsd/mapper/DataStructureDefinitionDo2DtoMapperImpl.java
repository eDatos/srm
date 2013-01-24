package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
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
    public List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDoListToDtoList(List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamacs) {
        List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos = new ArrayList<DataStructureDefinitionMetamacDto>(dataStructureDefinitionVersionMetamacs.size());
        for (DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac : dataStructureDefinitionVersionMetamacs) {
            dataStructureDefinitionMetamacDtos.add(dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac));
        }
        return dataStructureDefinitionMetamacDtos;
    }

    private DataStructureDefinitionMetamacDto dataStructureDefinitionVersionMetamacDoToDto(DataStructureDefinitionVersionMetamac source, TypeDozerCopyMode typeDozerCopyMode) {
        DataStructureDefinitionMetamacDto target = new DataStructureDefinitionMetamacDto();
        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));
        do2DtoMapperSdmxSrm.dataStructureDefinitionDoToDto(typeDozerCopyMode, source, target);

        // Metamac
        target.setAutoOpen(source.getAutoOpen());
        target.setShowDecimals(source.getShowDecimals());
        // Heading
        for (DimensionOrder dimensionOrderSource : source.getHeadingDimensions()) {
            target.addHeadingDimension(dimensionOrderDoToRelatedResourceDto(dimensionOrderSource));
        }
        // Stub
        for (DimensionOrder dimensionOrderSource : source.getStubDimensions()) {
            target.addStubDimension(dimensionOrderDoToRelatedResourceDto(dimensionOrderSource));
        }
        // Decimal Precision
        for (MeasureDimensionPrecision measureDimensionPrecision : source.getShowDecimalsPrecisions()) {
            target.addShowDecimalsPrecision(measureDimensionPrecisionDtoDoToMeasureDimensionPrecisionDtoDto(measureDimensionPrecision));
        }

        return target;
    }

    private RelatedResourceDto dimensionOrderDoToRelatedResourceDto(DimensionOrder source) {
        if (source == null) {
            return null;
        }

        RelatedResourceDto target = new RelatedResourceDto();
        target.setCode(source.getDimension().getCode());
        target.setTitle(null);
        target.setType(TypeExternalArtefactsEnum.COMPONENT);
        target.setUrn(source.getDimension().getUrn());
        target.setUrnProvider(source.getDimension().getUrnProvider());

        return target;
    }

    private MeasureDimensionPrecisionDto measureDimensionPrecisionDtoDoToMeasureDimensionPrecisionDtoDto(MeasureDimensionPrecision source) {
        if (source == null) {
            return null;
        }

        MeasureDimensionPrecisionDto target = new MeasureDimensionPrecisionDto();
        target.setCode(source.getConcept().getNameableArtefact().getCode());
        target.setTitle(null);
        target.setType(TypeExternalArtefactsEnum.CODE);
        target.setUrn(source.getConcept().getNameableArtefact().getUrn());
        target.setUrnProvider(source.getConcept().getNameableArtefact().getUrnProvider());
        target.setShowDecimalPrecision(source.getShowDecimalPrecision());

        return target;
    }
}