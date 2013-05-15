package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisationRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisationRepository;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacRepository;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.DimensionVisualisationInfo;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.DimensionVisualisationInfoDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;

@org.springframework.stereotype.Component("dataStructureDefinitionDto2DoMapper")
public class DataStructureDefinitionDto2DoMapperImpl extends BaseDto2DoMapperImpl implements DataStructureDefinitionDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.structure.mapper.DataStructureDefinitionDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private DataStructureDefinitionVersionMetamacRepository                                       dataStructureDefinitionVersionMetamacRepository;

    @Autowired
    private CodelistOrderVisualisationRepository                                                  codelistOrderVisualisationRepository;

    @Autowired
    private CodelistOpennessVisualisationRepository                                               codelistOpennessVisualisationRepository;

    // ------------------------------------------------------------
    // DATA STRUCTURE DEFINITIONS
    // ------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public <U extends Component> U componentDtoToComponent(ComponentDto source) throws MetamacException {

        U target = (U) dto2DoMapperSdmxSrm.componentDtoToComponent(source);
        return target;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends ComponentList> U componentListDtoToComponentList(ComponentListDto componentListDto) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentListDtoToComponentList(componentListDto);
    }

    @Override
    public DataStructureDefinitionVersionMetamac dataStructureDefinitionDtoToDataStructureDefinition(DataStructureDefinitionMetamacDto source) throws MetamacException {
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
        target.setStatisticalOperation(externalItemDtoStatisticalOperationsToExternalItemDo(source.getStatisticalOperation(), target.getStatisticalOperation(),
                ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION));
        headingDto2Do(source.getHeadingDimensions(), target);
        stubDto2Do(source.getStubDimensions(), target);
        showDecimalsPrecisionsDto2Do(source.getShowDecimalsPrecisions(), target);
        dimensionVisualisationInfoDto2Do(source.getDimensionVisualisationInfos(), target);

        // External Item of statistical operation never has title
        if (target.getStatisticalOperation() != null) {
            target.getStatisticalOperation().setTitle(null);
        }

        dto2DoMapperSdmxSrm.dataStructureDefinitionDtoToDataStructureDefinition(source, target);

        return target;
    }
    /**************************************************************************
     * PRIVATE
     **************************************************************************/

    private DataStructureDefinitionVersionMetamac headingDto2Do(List<RelatedResourceDto> sourceList, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac)
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

    private DataStructureDefinitionVersionMetamac stubDto2Do(List<RelatedResourceDto> sourceList, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

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

    private DataStructureDefinitionVersionMetamac showDecimalsPrecisionsDto2Do(List<MeasureDimensionPrecisionDto> sourceList,
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

    private DataStructureDefinitionVersionMetamac dimensionVisualisationInfoDto2Do(List<DimensionVisualisationInfoDto> sourceList,
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        // HashMap of actuals dimensions order
        Map<String, DimensionVisualisationInfo> dimensionVisualisationMap = new HashMap<String, DimensionVisualisationInfo>();
        for (DimensionVisualisationInfo dimensionVisualizationInfo : dataStructureDefinitionVersionMetamac.getDimensionVisualisationInfos()) {
            dimensionVisualisationMap.put(dimensionVisualizationInfo.getDimension().getUrn(), dimensionVisualizationInfo);
        }

        dataStructureDefinitionVersionMetamac.getDimensionVisualisationInfos().clear();

        for (DimensionVisualisationInfoDto dimensionVisualisationInfoDto : sourceList) {
            DimensionVisualisationInfo dimensionVisualizationInfoTarget = dimensionVisualizationInfoDtoToDimensionVisualizationInfo(dimensionVisualisationInfoDto,
                    dimensionVisualisationMap.get(dimensionVisualisationInfoDto.getUrn()), dataStructureDefinitionVersionMetamac);
            if (dimensionVisualizationInfoTarget != null) {
                dataStructureDefinitionVersionMetamac.addDimensionVisualisationInfo(dimensionVisualizationInfoTarget);
            }
        }

        return dataStructureDefinitionVersionMetamac;
    }

    private DimensionVisualisationInfo dimensionVisualizationInfoDtoToDimensionVisualizationInfo(DimensionVisualisationInfoDto source, DimensionVisualisationInfo target,
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {

        if (target == null) {
            if (source.getDisplayOrder() == null && source.getHierarchyLevelsOpen() == null) {
                return null;
            } else {
                target = new DimensionVisualisationInfo();
            }
        }

        DimensionComponent dimension = (DimensionComponent) dto2DoMapperSdmxSrm.relatedResourceDtoToEntity(source, ServiceExceptionParameters.DIMENSION);
        target.setDimension(dimension);

        if (source.getDisplayOrder() != null) {
            target.setDisplayOrder(retrieveCodelistOrderVisualisation(source.getDisplayOrder().getUrn()));
        } else {
            target.setDisplayOrder(null);
        }

        if (source.getHierarchyLevelsOpen() != null) {
            target.setHierarchyLevelsOpen(retrieveCodelistOpennessVisualisation(source.getHierarchyLevelsOpen().getUrn()));
        } else {
            target.setHierarchyLevelsOpen(null);
        }

        target.setDsdVersion(dataStructureDefinitionVersionMetamac);

        return target;
    }

    private CodelistOrderVisualisation retrieveCodelistOrderVisualisation(String urn) throws MetamacException {
        CodelistOrderVisualisation target = codelistOrderVisualisationRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private CodelistOpennessVisualisation retrieveCodelistOpennessVisualisation(String urn) throws MetamacException {
        CodelistOpennessVisualisation target = codelistOpennessVisualisationRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }
}