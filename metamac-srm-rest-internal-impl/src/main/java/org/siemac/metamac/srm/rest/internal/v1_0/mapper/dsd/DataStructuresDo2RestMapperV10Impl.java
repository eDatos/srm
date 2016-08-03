package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeRelationship;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attributes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructureComponents;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionBase;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionReferences;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionVisualisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DimensionVisualisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Empty;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Group;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Groups;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Measure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.MeasureDimension;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.PrimaryMeasure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ShowDecimalPrecision;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ShowDecimalPrecisions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.TimeDimension;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.DimensionVisualisationInfo;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.StructureBaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
import com.arte.statistic.sdmx.srm.core.structure.domain.AttributeDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupDimensionDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.GroupRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDescriptor;
import com.arte.statistic.sdmx.srm.core.structure.domain.NoSpecifiedRelationship;
import com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasureRelationship;
import com.arte.statistic.sdmx.srm.core.structure.serviceimpl.utils.StructuresComparator;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.UsageStatus;

@org.springframework.stereotype.Component
public class DataStructuresDo2RestMapperV10Impl extends StructureBaseDo2RestMapperV10Impl implements DataStructuresDo2RestMapperV10 {

    @Autowired
    private ConceptsDo2RestMapperV10 conceptsDo2RestMapper;

    @Override
    public DataStructures toDataStructures(PagedResult<DataStructureDefinitionVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        DataStructures targets = new DataStructures();
        targets.setKind(SrmRestConstants.KIND_DATA_STRUCTURES);

        // Pagination
        String baseLink = toDataStructuresLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (DataStructureDefinitionVersionMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getDataStructures().add(target);
        }
        return targets;
    }

    @Override
    public DataStructure toDataStructure(DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }

        Boolean isImported = source.getMaintainableArtefact().getIsImported();

        DataStructure target = new DataStructure();
        target.setKind(SrmRestConstants.KIND_DATA_STRUCTURE);
        target.setSelfLink(toDataStructureSelfLink(source));
        target.setParentLink(toDataStructureParentLink(source));
        target.setChildLinks(toDataStructureChildLinks(source));
        target.setManagementAppLink(toDataStructureManagementApplicationLink(source));

        toStructure(source, source.getLifeCycleMetadata(), target);

        target.setDataStructureComponents(toDataStructureComponents(source.getGrouping(), isImported));
        target.setStatisticalOperation(toResourceExternalItemStatisticalOperation(source.getStatisticalOperation()));
        target.setAutoOpen(source.getAutoOpen());
        target.setHeading(toDimensionReferences(source.getHeadingDimensions()));
        target.setStub(toDimensionReferences(source.getStubDimensions()));
        target.setShowDecimals(source.getShowDecimals());
        target.setShowDecimalsPrecisions(toShowDecimalPrecisions(source.getShowDecimalsPrecisions()));
        target.setDimensionVisualisations(toDimensionVisualisations(source.getDimensionVisualisationInfos()));

        return target;
    }

    private DataStructureComponents toDataStructureComponents(Set<ComponentList> sources, Boolean isImported) {
        DataStructureComponents target = new DataStructureComponents();
        for (ComponentList source : sources) {
            if (source instanceof DimensionDescriptor) {
                target.setDimensions(toDimensions((DimensionDescriptor) source, isImported));
            } else if (source instanceof GroupDimensionDescriptor) {
                if (target.getGroups() == null) {
                    target.setGroups(new Groups());
                }
                target.getGroups().getGroups().add(toGroup((GroupDimensionDescriptor) source, isImported));
            } else if (source instanceof MeasureDescriptor) {
                target.setMeasure(toMeasure((MeasureDescriptor) source, isImported));
            } else if (source instanceof AttributeDescriptor) {
                target.setAttributes(toAttributes((AttributeDescriptor) source, isImported));
            } else {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
            }
        }
        return target;
    }

    private Dimensions toDimensions(DimensionDescriptor source, Boolean isImported) {
        if (source == null) {
            return null;
        }
        Dimensions target = new Dimensions();
        toComponentList(source, target, isImported);

        // Dimensions must be returned in order
        List<Component> componentsSorted = new ArrayList<Component>(source.getComponents());
        Collections.sort(componentsSorted, StructuresComparator.STRUCTURE_COMPOENTN_ID_COMPARATOR);
        for (Component component : componentsSorted) {
            if (component instanceof com.arte.statistic.sdmx.srm.core.structure.domain.Dimension) {
                target.getDimensions().add(toDimension((com.arte.statistic.sdmx.srm.core.structure.domain.Dimension) component, isImported));
            } else if (component instanceof com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension) {
                target.getDimensions().add(toTimeDimension((com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension) component, isImported));
            } else if (component instanceof com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension) {
                target.getDimensions().add(toMeasureDimension((com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension) component, isImported));
            } else {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
            }
        }
        return target;
    }

    private Measure toMeasure(MeasureDescriptor source, Boolean isImported) {
        if (source == null) {
            return null;
        }
        Measure target = new Measure();
        toComponentList(source, target, isImported);

        if (!CollectionUtils.isEmpty(source.getComponents())) {
            com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure component = (com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure) source.getComponents().iterator().next(); // only
                                                                                                                                                                                                      // one
            target.setPrimaryMeasure(toPrimaryMeasure(component, isImported));
        }
        return target;
    }

    private PrimaryMeasure toPrimaryMeasure(com.arte.statistic.sdmx.srm.core.structure.domain.PrimaryMeasure source, Boolean isImported) {
        if (source == null) {
            return null;
        }

        PrimaryMeasure target = new PrimaryMeasure();
        toComponent(source, target, isImported);
        return target;
    }

    private Group toGroup(GroupDimensionDescriptor source, Boolean isImported) {
        if (source == null) {
            return null;
        }

        Group target = new Group();
        toComponentList(source, target, isImported);
        target.setDimensions(toDimensionReferences(source.getComponents()));
        return target;
    }

    private Attributes toAttributes(AttributeDescriptor source, Boolean isImported) {
        if (source == null) {
            return null;
        }

        Attributes target = new Attributes();
        toComponentList(source, target, isImported);

        for (Component sourceComponent : source.getComponents()) {
            if (sourceComponent instanceof DataAttribute) {
                target.getAttributes().add(toAttribute((DataAttribute) sourceComponent, isImported));
            } else {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
            }
        }
        return target;
    }

    private void toAttributeCommon(DataAttribute source, Attribute target, Boolean isImported) {
        if (source == null) {
            return;
        }
        toComponent(source, target, isImported);

        target.setAssignmentStatus(toAttributeUsageStatusType(source.getUsageStatus()));
        target.setRoleConcepts(conceptsDo2RestMapper.toRoleConcepts(source.getRole()));
        target.setAttributeRelationship(toAttributeRelationship(source.getRelateTo()));
        target.setType(toAttributeQualifierType(source.getSpecialAttributeType()));
    }

    private Attribute toAttribute(DataAttribute source, Boolean isImported) {
        if (source == null) {
            return null;
        }
        Attribute target = new Attribute();
        toAttributeCommon(source, target, isImported);
        return target;
    }

    private AttributeRelationship toAttributeRelationship(com.arte.statistic.sdmx.srm.core.structure.domain.AttributeRelationship source) {
        if (source == null) {
            return null;
        }
        AttributeRelationship target = new AttributeRelationship();
        if (source instanceof DimensionRelationship) {
            DimensionRelationship dimensionRelationship = (DimensionRelationship) source;
            for (DimensionComponent sourceDimension : dimensionRelationship.getDimension()) {
                target.getDimensions().add(toDimensionReference(sourceDimension));
            }
            for (GroupDimensionDescriptor sourceGroupDimensionDescriptor : dimensionRelationship.getGroupKey()) {
                target.getAttachmentGroups().add(sourceGroupDimensionDescriptor.getCode());
            }
        } else if (source instanceof GroupRelationship) {
            GroupRelationship groupRelationship = (GroupRelationship) source;
            target.setGroup(groupRelationship.getGroupKey().getCode());
        } else if (source instanceof PrimaryMeasureRelationship) {
            target.setPrimaryMeasure(SdmxAlias.COMPONENT_PRIMARY_MEASURE); // fixed
        } else if (source instanceof NoSpecifiedRelationship) {
            target.setNone(new Empty());
        } else {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
        return target;
    }

    private void toDimensionCommon(com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent source, DimensionBase target, Boolean isImported) {
        toComponent(source, target, isImported);
    }

    private Dimension toDimension(com.arte.statistic.sdmx.srm.core.structure.domain.Dimension source, Boolean isImported) {
        Dimension target = new Dimension();
        toDimensionCommon(source, target, isImported);

        target.setType(DimensionType.DIMENSION);
        target.setRoleConcepts(conceptsDo2RestMapper.toRoleConcepts(source.getRole()));
        if (SpecialDimensionTypeEnum.SPATIAL.equals(source.getSpecialDimensionType())) {
            target.setIsSpatial(Boolean.TRUE);
        }
        return target;
    }

    private MeasureDimension toMeasureDimension(com.arte.statistic.sdmx.srm.core.structure.domain.MeasureDimension source, Boolean isImported) {
        MeasureDimension target = new MeasureDimension();
        toDimensionCommon(source, target, isImported);

        target.setType(DimensionType.MEASURE_DIMENSION);
        target.setRoleConcepts(conceptsDo2RestMapper.toRoleConcepts(source.getRole()));
        return target;
    }

    private TimeDimension toTimeDimension(com.arte.statistic.sdmx.srm.core.structure.domain.TimeDimension source, Boolean isImported) {
        TimeDimension target = new TimeDimension();
        toDimensionCommon(source, target, isImported);
        target.setType(DimensionType.TIME_DIMENSION);
        return target;
    }

    @Override
    protected boolean canStructureVersionBeProvidedByApi(StructureVersion source) {
        return true; // no additional conditions
    }

    private DimensionReferences toDimensionReferences(List<DimensionOrder> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        DimensionReferences targets = new DimensionReferences();
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (DimensionOrder source : sources) {
            targets.getDimensions().add(toDimensionReference(source.getDimension()));
        }
        return targets;
    }

    private DimensionReferences toDimensionReferences(Set<Component> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        DimensionReferences targets = new DimensionReferences();
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (Component source : sources) {
            targets.getDimensions().add(toDimensionReference((DimensionComponent) source));
        }
        return targets;
    }

    private ShowDecimalPrecisions toShowDecimalPrecisions(List<MeasureDimensionPrecision> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        ShowDecimalPrecisions targets = new ShowDecimalPrecisions();
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (MeasureDimensionPrecision source : sources) {
            ShowDecimalPrecision target = new ShowDecimalPrecision();
            target.setShowDecimals(source.getShowDecimalPrecision());
            target.setConcept(conceptsDo2RestMapper.toResource(source.getConcept()));
            targets.getShowDecimalPrecisions().add(target);
        }
        return targets;
    }

    private DimensionVisualisations toDimensionVisualisations(List<DimensionVisualisationInfo> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        DimensionVisualisations targets = new DimensionVisualisations();
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (DimensionVisualisationInfo source : sources) {
            DimensionVisualisation target = new DimensionVisualisation();
            target.setDimension(toDimensionReference(source.getDimension()));
            if (source.getDisplayOrder() != null) {
                target.setOrder(source.getDisplayOrder().getNameableArtefact().getCode());
            }
            if (source.getHierarchyLevelsOpen() != null) {
                target.setOpenness(source.getHierarchyLevelsOpen().getNameableArtefact().getCode());
            }
            targets.getDimensionVisualisations().add(target);
        }
        return targets;
    }

    private ResourceLink toDataStructureSelfLink(DataStructureDefinitionVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_DATA_STRUCTURE, toDataStructureLink(source));
    }

    @Override
    public ResourceLink toDataStructureSelfLink(String agencyID, String resourceID, String version) {
        return toResourceLink(SrmRestConstants.KIND_DATA_STRUCTURE, toDataStructureLink(agencyID, resourceID, version));
    }

    private ResourceLink toDataStructureParentLink(DataStructureDefinitionVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_DATA_STRUCTURES, toDataStructuresLink(null, null, null));
    }

    private ChildLinks toDataStructureChildLinks(DataStructureDefinitionVersionMetamac source) {
        return null;
    }

    private ResourceInternal toResource(DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_DATA_STRUCTURE, toDataStructureSelfLink(source), toDataStructureManagementApplicationLink(source), target);
        return target;
    }

    private String toDataStructuresLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathDataStructures(), agencyID, resourceID, version);
    }

    private String toDataStructureLink(StructureVersion structureVersion) {
        return toMaintainableArtefactLink(toSubpathDataStructures(), structureVersion.getMaintainableArtefact());
    }

    private String toDataStructureLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathDataStructures(), agencyID, resourceID, version);
    }

    private String toSubpathDataStructures() {
        return SrmRestConstants.LINK_SUBPATH_DATA_STRUCTURES;
    }

    private AttributeQualifierType toAttributeQualifierType(SpecialAttributeTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case MEASURE_EXTENDS:
                return AttributeQualifierType.MEASURE;
            case SPATIAL_EXTENDS:
                return AttributeQualifierType.SPATIAL;
            case TIME_EXTENDS:
                return AttributeQualifierType.TIME;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private String toDataStructureManagementApplicationLink(DataStructureDefinitionVersionMetamac source) {
        return getInternalWebApplicationNavigation().buildDataStructureDefinitionUrl(source);
    }

    @Override
    public String toDataStructureManagementApplicationLink(String dsdUrn) {
        return getInternalWebApplicationNavigation().buildDataStructureDefinitionUrl(dsdUrn);
    }

    private org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeUsageStatusType toAttributeUsageStatusType(UsageStatus source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case CONDITIONAL:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeUsageStatusType.CONDITIONAL;
            case MANDATORY:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeUsageStatusType.MANDATORY;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private void toComponentList(ComponentList source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Components target, Boolean isImported) {
        if (source == null) {
            return;
        }
        toIdentifiableArtefact(source, target, isImported);
    }

    private void toComponent(Component source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Component target, Boolean isImported) {
        if (source == null) {
            return;
        }
        target.setConceptIdentity(conceptsDo2RestMapper.toResource(source.getCptIdRef()));
        target.setLocalRepresentation(commonDo2RestMapper.toRepresentation(source.getLocalRepresentation()));

        toIdentifiableArtefact(source, target, isImported);
    }

    private String toDimensionReference(DimensionComponent source) {
        return source.getCode();
    }

}