package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.sdmx.resources.sdmxml.schemas.v2_1.common.URNReferenceType;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ShowDecimalPrecision;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ShowDecimalPrecisions;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DimensionOrder;
import org.siemac.metamac.srm.core.dsd.domain.MeasureDimensionPrecision;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.StructureBaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.mapper.StructureDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialAttributeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;

@Component
public class DataStructuresDo2RestMapperV10Impl extends StructureBaseDo2RestMapperV10Impl implements DataStructuresDo2RestMapperV10 {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.structure.mapper.StructureDo2JaxbMapper dataStructuresDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("dataStructuresDo2JaxbRestInternalCallbackMetamac")
    private StructureDo2JaxbCallback                                                 dataStructuresDo2JaxbCallback;

    @Override
    public DataStructures toDataStructures(PagedResult<DataStructureDefinitionVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        DataStructures targets = new DataStructures();
        targets.setKind(RestInternalConstants.KIND_DATA_STRUCTURES);

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
        // following method will call toDataStructure(DataStructureDefinitionVersionMetamac source, DataStructure target) method, thank to callback
        return (DataStructure) dataStructuresDo2JaxbSdmxMapper.dataStructureDefinitionDoToJaxb(source, dataStructuresDo2JaxbCallback);
    }

    @Override
    public void toDataStructure(DataStructureDefinitionVersionMetamac source, DataStructure target) {
        if (source == null) {
            return;
        }
        target.setKind(RestInternalConstants.KIND_DATA_STRUCTURE);
        target.setSelfLink(toDataStructureSelfLink(source));
        target.setParentLink(toDataStructureParentLink(source));
        target.setChildLinks(toDataStructureChildLinks(source));
        target.setManagementAppLink(toDataStructureManagementApplicationLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setReplaceToVersion(toStructureReplaceToVersion(source));
        target.setReplacedByVersion(toStructureReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));

        target.setStatisticalOperation(toResourceExternalItemStatisticalOperation(source.getStatisticalOperation()));
        target.setAutoOpen(source.getAutoOpen());
        target.setHeading(toDimensions(source.getHeadingDimensions()));
        target.setStub(toDimensions(source.getStubDimensions()));
        target.setShowDecimals(source.getShowDecimals() != null ? BigInteger.valueOf(source.getShowDecimals()) : null);
        target.setShowDecimalsPrecisions(toShowDecimalPrecisions(source.getShowDecimalsPrecisions()));

        // TODO metadatos de visualización de codelist en dimensión (pte Core)
    }

    @Override
    public void toDimension(Dimension source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Dimension target) {
        if (source == null) {
            return;
        }
        if (SpecialDimensionTypeEnum.SPATIAL.equals(source.getSpecialDimensionType())) {
            target.setIsSpatial(Boolean.TRUE);
        }
    }

    @Override
    public void toAttribute(DataAttribute source, Attribute target) {
        if (source == null) {
            return;
        }
        target.setType(toAttributeQualifierType(source.getSpecialAttributeType()));
    }

    @Override
    protected boolean canStructureVersionBeProvidedByApi(StructureVersion source) {
        return true; // no additional conditions
    }

    private Dimensions toDimensions(List<DimensionOrder> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        Dimensions targets = new Dimensions();
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (DimensionOrder source : sources) {
            URNReferenceType target = new URNReferenceType();
            target.setURN(getUrn(source.getDimension()));
            targets.getDimensions().add(target);
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
            target.setShowDecimals(BigInteger.valueOf(source.getShowDecimalPrecision()));

            URNReferenceType concept = new URNReferenceType();
            concept.setURN(getUrn(source.getConcept().getNameableArtefact()));
            target.setConcept(concept);

            targets.getShowDecimalPrecisions().add(target);
        }
        return targets;
    }

    private ResourceLink toDataStructureSelfLink(DataStructureDefinitionVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_DATA_STRUCTURE, toDataStructureLink(source));
    }

    private ResourceLink toDataStructureParentLink(DataStructureDefinitionVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_DATA_STRUCTURES, toDataStructuresLink(null, null, null));
    }

    private ChildLinks toDataStructureChildLinks(DataStructureDefinitionVersionMetamac source) {
        return null;
    }

    private ResourceInternal toResource(DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_DATA_STRUCTURE, toDataStructureSelfLink(source), toDataStructureManagementApplicationLink(source));
    }

    private String toDataStructuresLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathDataStructures(), agencyID, resourceID, version);
    }
    private String toDataStructureLink(StructureVersion structureVersion) {
        return toMaintainableArtefactLink(toSubpathDataStructures(), structureVersion.getMaintainableArtefact());
    }
    private String toSubpathDataStructures() {
        return RestInternalConstants.LINK_SUBPATH_DATA_STRUCTURES;
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

}