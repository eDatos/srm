package org.siemac.metamac.srm.core.dsd.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.MetamacExceptionItemBuilder;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.common.LifeCycleImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacRepository;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParametersInternal;
import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension;
import com.arte.statistic.sdmx.srm.core.structure.serviceapi.DataStructureDefinitionService;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;

@Service("dsdLifeCycle")
public class DsdLifeCycleImpl extends LifeCycleImpl {

    @Autowired
    private StructureVersionRepository                      structureVersionRepository;

    @Autowired
    private DataStructureDefinitionVersionMetamacRepository dataStructureDefinitionVersionMetamacRepository;

    @Autowired
    private DataStructureDefinitionService                  dataStructureDefinitionService;

    public DsdLifeCycleImpl() {
        this.callback = new DataStructureDefinitionLifeCycleCallback();
    }

    private class DataStructureDefinitionLifeCycleCallback implements LifeCycleCallback {

        @Override
        public SrmLifeCycleMetadata getLifeCycleMetadata(Object srmResourceVersion) {
            return getDataStructureDefinitionVersionMetamac(srmResourceVersion).getLifeCycleMetadata();
        }

        @Override
        public MaintainableArtefact getMaintainableArtefact(Object srmResourceVersion) {
            return getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact();
        }

        @Override
        public Object updateSrmResource(Object srmResourceVersion) {
            return structureVersionRepository.save(getDataStructureDefinitionVersionMetamac(srmResourceVersion));
        }

        @Override
        public Object retrieveSrmResourceByProcStatus(String urn, ProcStatusEnum[] procStatus) throws MetamacException {
            return dataStructureDefinitionVersionMetamacRepository.retrieveDataStructureDefinitionVersionByProcStatus(urn, procStatus);
        }

        @Override
        public void checkConcreteResourceInProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = (DataStructureDefinitionVersionMetamac) srmResourceVersion;

            // Auxiliary data structures
            Set<DimensionComponent> stubDimensionSet = new HashSet<DimensionComponent>();
            for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamac.getStubDimensions()) {
                stubDimensionSet.add(dimensionOrder.getDimension());
            }
            Set<DimensionComponent> headingDimensionSet = new HashSet<DimensionComponent>();
            for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamac.getHeadingDimensions()) {
                headingDimensionSet.add(dimensionOrder.getDimension());
            }

            // Check mandatory attributes
            ValidationUtils.checkMetadataRequired(dataStructureDefinitionVersionMetamac.getAutoOpen(), ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION_AUTOPEN, exceptions);

            // Check: Grouping validation, the DSD must have a DimensionDescriptor (with almost one dimension) and MeasureDescriptor (with primary measure)
            for (ComponentList componentList : dataStructureDefinitionVersionMetamac.getGrouping()) {
                if (componentList instanceof MeasureDescriptor) {
                    if (componentList.getComponents().size() != 1) {
                        exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_VALIDATION_CONSTRAINT_CARDINALITY_MIN,
                                ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION + ServiceExceptionParametersInternal.COMPONENT_PRIMARY_MEASURE));
                    }
                } else if (componentList instanceof DimensionDescriptor) {
                    if (componentList.getComponents().size() == 0) {
                        exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_VALIDATION_CONSTRAINT_CARDINALITY_MIN,
                                ServiceExceptionParameters.DATA_STRUCTURE_DEFINITION + ServiceExceptionParametersInternal.COMPONENT_DIMENSION));
                    }
                }
            }

            // Check mutual exclusivity between dimensions in the heading and stub.
            for (DimensionOrder dimensionOrder : dataStructureDefinitionVersionMetamac.getHeadingDimensions()) {
                if (stubDimensionSet.contains(dimensionOrder.getDimension())) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_STUB_AND_HEADING_OCCURRENCE, dimensionOrder.getDimension().getCode()));
                }
            }

            boolean isDsdWithMeasureDimension = false;
            boolean isDsdWithSpatialDimension = false;
            boolean isDsdWithTimeDimension = false;
            // Check all dimensions must be appears in stub or heading
            for (ComponentList componentList : dataStructureDefinitionVersionMetamac.getGrouping()) {
                if (componentList instanceof DimensionDescriptor) {
                    for (Component component : componentList.getComponents()) {
                        if (!stubDimensionSet.contains(component) && !headingDimensionSet.contains(component)) {
                            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_STUB_AND_HEADING_INCOMPLETE));
                        }
                        // Initialize auxiliary data for other constraints
                        if (component instanceof MeasureDimension) {
                            isDsdWithMeasureDimension = true;
                        } else if (component instanceof TimeDimension) {
                            isDsdWithTimeDimension = true;
                        } else if (SpecialDimensionTypeEnum.SPATIAL.equals(((DimensionComponent) component).getSpecialDimensionType())) {
                            isDsdWithSpatialDimension = true;
                        }
                    }
                    break;
                }
            }

            // Constraints only valid for non imported artifacts
            if (!dataStructureDefinitionVersionMetamac.getMaintainableArtefact().getIsImported()) {
                // Check: If the DSD not contain a measure-dimension, then it must have a special attribute type
                // Check: If the DSD not contain a spatial-dimension, then it must have a special attribute type
                boolean foundSpecialAttributeOfMeasure = false;
                boolean foundSpecialAttributeOfSpatial = false;
                boolean foundSpecialAttributeOfTime = false;
                for (ComponentList componentList : dataStructureDefinitionVersionMetamac.getGrouping()) {
                    if (componentList instanceof AttributeDescriptor) {
                        for (Component component : componentList.getComponents()) {
                            SpecialAttributeTypeEnum specialAttributeType = ((DataAttribute) component).getSpecialAttributeType();
                            if (specialAttributeType != null) {
                                switch (specialAttributeType) {
                                    case MEASURE_EXTENDS:
                                        foundSpecialAttributeOfMeasure = true;
                                        break;
                                    case SPATIAL_EXTENDS:
                                        foundSpecialAttributeOfSpatial = true;
                                        break;
                                    case TIME_EXTENDS:
                                        foundSpecialAttributeOfTime = true;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        break;
                    }
                }

                if (!isDsdWithMeasureDimension && !foundSpecialAttributeOfMeasure) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_WITHOUT_MEASUREDIM_SPECIAL_ATTR));
                }

                if (!isDsdWithSpatialDimension && !foundSpecialAttributeOfSpatial) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_WITHOUT_SPATIALDIM_SPECIAL_ATTR));
                }

                if (!isDsdWithTimeDimension && !foundSpecialAttributeOfTime) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.DATA_STRUCTURE_DEFINITION_WITHOUT_TIMEDIM_SPECIAL_ATTR));
                }
            }
        }

        @Override
        public void checkConcreteResourceInDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectProductionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInRejectDiffusionValidation(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public void checkConcreteResourceInInternallyPublished(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public List<MetamacExceptionItem> checkConcreteResourceTranslations(ServiceContext ctx, Object srmResourceVersion, String locale) {
            // TODO comprobar traducciones
            return new ArrayList<MetamacExceptionItem>();
        }

        @Override
        public Object publishInternallyConcreteResource(ServiceContext ctx, Object srmResourceVersion) {
            // nothing
            return srmResourceVersion;
        }

        @Override
        public void checkConcreteResourceInExternallyPublished(Object srmResourceVersion, ProcStatusEnum targetStatus, List<MetamacExceptionItem> exceptions) {
            // nothing
        }

        @Override
        public Object markSrmResourceAsFinal(ServiceContext ctx, Object srmResourceVersion, Boolean forceLastestFinal) throws MetamacException {
            return dataStructureDefinitionService.markDataStructureAsFinal(ctx, getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), forceLastestFinal);
        }

        @Override
        public Object markSrmResourceAsPublic(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return dataStructureDefinitionService.markDataStructureAsPublic(ctx, getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn());
        }

        @Override
        public Object startSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return dataStructureDefinitionService.startDataStructureDefinitionValidity(ctx, getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), null);
        }

        @Override
        public Object endSrmResourceValidity(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            return dataStructureDefinitionService.endDataStructureDefinitionValidity(ctx, getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), null);
        }

        @Override
        public List<Object> findSrmResourceVersionsOfSrmResourceInProcStatus(ServiceContext ctx, Object srmResourceVersion, ProcStatusEnum... procStatus) {

            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(DataStructureDefinitionVersionMetamac.class)
                    .withProperty(DataStructureDefinitionVersionMetamacProperties.structure().id()).eq(getDataStructureDefinitionVersionMetamac(srmResourceVersion).getStructure().getId())
                    .withProperty(DataStructureDefinitionVersionMetamacProperties.lifeCycleMetadata().procStatus()).in((Object[]) procStatus).distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.noLimits();
            PagedResult<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersionMetamac = dataStructureDefinitionVersionMetamacRepository.findByCondition(conditions, pagingParameter);
            return dataStructureDefinitionMetamacVersionsToObject(dataStructureDefinitionVersionMetamac.getValues());
        }

        @Override
        public MetamacExceptionItem buildExceptionItemWrongProcStatus(Object srmResourceVersion, String[] procStatusExpecteds) {
            return MetamacExceptionItemBuilder.metamacExceptionItem().withCommonServiceExceptionType(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS)
                    .withMessageParameters(getDataStructureDefinitionVersionMetamac(srmResourceVersion).getMaintainableArtefact().getUrn(), procStatusExpecteds).build();
        }

        @Override
        public Boolean canHaveCategorisations() {
            return Boolean.TRUE;
        }

        @Override
        public void mergeTemporal(ServiceContext ctx, Object srmResourceVersion) throws MetamacException {
            // TODO completar cuando se haga metgeTemporal de dsd
            throw new UnsupportedOperationException("completar cuando se haga metgeTemporal de dsd");
        }

        /**********************************************************************
         * PRIVATES
         **********************************************************************/
        private DataStructureDefinitionVersionMetamac getDataStructureDefinitionVersionMetamac(Object srmResourceVersion) {
            return (DataStructureDefinitionVersionMetamac) srmResourceVersion;
        }

        private List<Object> dataStructureDefinitionMetamacVersionsToObject(List<DataStructureDefinitionVersionMetamac> dataStructureDefinitionVersions) {
            List<Object> structureVersions = new ArrayList<Object>(dataStructureDefinitionVersions.size());
            for (DataStructureDefinitionVersionMetamac dataStructureVersion : dataStructureDefinitionVersions) {
                structureVersions.add(dataStructureVersion);
            }
            return structureVersions;
        }

    }
}
