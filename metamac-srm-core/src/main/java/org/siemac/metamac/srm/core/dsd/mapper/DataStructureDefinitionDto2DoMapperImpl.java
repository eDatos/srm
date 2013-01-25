package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacRepository;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.EnumeratedRepresentation;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;

@org.springframework.stereotype.Component("dataStructureDefinitionDto2DoMapper")
public class DataStructureDefinitionDto2DoMapperImpl implements DataStructureDefinitionDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.structure.mapper.DataStructureDefinitionDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private DataStructureDefinitionVersionMetamacRepository                                       dataStructureDefinitionVersionMetamacRepository;

    // ------------------------------------------------------------
    // DATA STRUCTURE DEFINITIONS
    // ------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public <U extends Component> U componentDtoToComponent(ComponentDto source) throws MetamacException {

        U target = (U) dto2DoMapperSdmxSrm.componentDtoToComponent(source);

        // Flag for force to perform a clean up of the Show Decimals Precision of DSD
        if (target instanceof MeasureDimension) {
            if (target.getId() == null || target.getLocalRepresentation() == null) {
                ((MeasureDimension) target).setIsRepresentationUpdated(Boolean.TRUE);
            } else {
                EnumeratedRepresentation targetRepresentation = (EnumeratedRepresentation) target.getLocalRepresentation();
                if (!source.getLocalRepresentation().getEnumerated().getUrn().equals(targetRepresentation.getEnumerated().getUrn())) {
                    ((MeasureDimension) target).setIsRepresentationUpdated(Boolean.TRUE);
                }
            }
        }

        return target;
    }
    @SuppressWarnings("unchecked")
    @Override
    public <U extends ComponentList> U componentListDtoToComponentList(ComponentListDto componentListDto) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentListDtoToComponentList(componentListDto);
    }

    @Override
    public DataStructureDefinitionVersionMetamac dataStructureDefinitionDtoToDataStructureDefinition(DataStructureDefinitionMetamacDto source) throws MetamacException {
        return dataStructureDefinitionDtoToDataStructureDefinitionPrivate(source);
    }

    /**************************************************************************
     * PRIVATE
     **************************************************************************/

    private DataStructureDefinitionVersionMetamac dataStructureDefinitionDtoToDataStructureDefinitionPrivate(DataStructureDefinitionMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        DataStructureDefinitionVersionMetamac target = null;
        if (source.getUrn() == null) {
            target = new DataStructureDefinitionVersionMetamac();
        } else {
            target = dataStructureDefinitionVersionMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        target.setAutoOpen(source.getAutoOpen());
        target.setShowDecimals(source.getShowDecimals());
        headingProcess(source.getHeadingDimensions(), target);
        stubProcess(source.getStubDimensions(), target);
        showDecimalsPrecisionsProcess(source.getShowDecimalsPrecisions(), target);

        dto2DoMapperSdmxSrm.dataStructureDefinitionDtoToDataStructureDefinition(source, target);

        return target;
    }

    private DataStructureDefinitionVersionMetamac headingProcess(List<RelatedResourceDto> sourceList, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac)
            throws MetamacException {

        // HashMap of actuals dimensions order
        Map<String, DimensionOrder> dimensionOrderMap = new HashMap<String, DimensionOrder>();
        for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamac.getHeadingDimensions()) {
            dimensionOrderMap.put(dimensionOrder.getDimension().getUrn(), dimensionOrder);
        }

        dataStructureDefinitionVersionMetamac.getHeadingDimensions().clear();

        for (int i = 0; i < sourceList.size(); i++) {
            dataStructureDefinitionVersionMetamac
                    .addHeadingDimension(relatedResourceDtoToDimensionOrder(sourceList, ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_HEADING, dimensionOrderMap, i));
        }

        return dataStructureDefinitionVersionMetamac;
    }

    private DataStructureDefinitionVersionMetamac stubProcess(List<RelatedResourceDto> sourceList, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        // HashMap of actuals dimensions order
        Map<String, DimensionOrder> dimensionOrderMap = new HashMap<String, DimensionOrder>();
        for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamac.getStubDimensions()) {
            dimensionOrderMap.put(dimensionOrder.getDimension().getUrn(), dimensionOrder);
        }

        dataStructureDefinitionVersionMetamac.getStubDimensions().clear();

        for (int i = 0; i < sourceList.size(); i++) {
            dataStructureDefinitionVersionMetamac.addStubDimension(relatedResourceDtoToDimensionOrder(sourceList, ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_STUB, dimensionOrderMap, i));
        }

        return dataStructureDefinitionVersionMetamac;
    }

    private DimensionOrder relatedResourceDtoToDimensionOrder(List<RelatedResourceDto> sourceList, String metadataName, Map<String, DimensionOrder> dimensionOrderMap, int indexOrder)
            throws MetamacException {
        DimensionOrder dimensionOrder;
        if (dimensionOrderMap.containsKey(sourceList.get(indexOrder).getUrn())) {
            dimensionOrder = dimensionOrderMap.get(sourceList.get(indexOrder).getUrn());
            if (indexOrder + 1 != dimensionOrder.getDimOrder()) {
                dimensionOrder.setDimOrder(indexOrder + 1);
            }
        } else {
            DimensionComponent dimension = (DimensionComponent) dto2DoMapperSdmxSrm.relatedResourceDtoToEntity(sourceList.get(indexOrder), metadataName);
            dimensionOrder = new DimensionOrder();
            dimensionOrder.setDimension(dimension);
            dimensionOrder.setDimOrder(indexOrder + 1);
        }
        return dimensionOrder;
    }

    private DataStructureDefinitionVersionMetamac showDecimalsPrecisionsProcess(List<MeasureDimensionPrecisionDto> sourceList,
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        // HashMap of actuals measure dimensions precision
        Map<String, MeasureDimensionPrecision> measureDimPrecisionMap = new HashMap<String, MeasureDimensionPrecision>();
        for (MeasureDimensionPrecision measureDimensionPrecision : dataStructureDefinitionVersionMetamac.getShowDecimalsPrecisions()) {
            measureDimPrecisionMap.put(measureDimensionPrecision.getConcept().getNameableArtefact().getUrn(), measureDimensionPrecision);
        }

        dataStructureDefinitionVersionMetamac.getShowDecimalsPrecisions().clear();

        for (int i = 0; i < sourceList.size(); i++) {
            MeasureDimensionPrecision measureDimensionPrecision;
            if (measureDimPrecisionMap.containsKey(sourceList.get(i).getUrn())) {
                measureDimensionPrecision = measureDimPrecisionMap.get(sourceList.get(i).getUrn());
                measureDimensionPrecision.setShowDecimalPrecision(sourceList.get(i).getShowDecimalPrecision());
            } else {
                ConceptMetamac concept = (ConceptMetamac) dto2DoMapperSdmxSrm.relatedResourceDtoToEntity(sourceList.get(i), ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_SHOW_DEC_PREC);
                measureDimensionPrecision = new MeasureDimensionPrecision();
                measureDimensionPrecision.setConcept(concept);
                measureDimensionPrecision.setShowDecimalPrecision(sourceList.get(i).getShowDecimalPrecision());
            }
            dataStructureDefinitionVersionMetamac.addShowDecimalsPrecision(measureDimensionPrecision);
        }

        return dataStructureDefinitionVersionMetamac;
    }
}