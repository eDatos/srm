package org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.structure.mapper.StructureDo2JaxbCallback;

@Component
public class DataStructuresDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl implements DataStructuresDo2RestMapperV10 {

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
            Resource target = toResource(source);
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
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setReplaceToVersion(source.getMaintainableArtefact().getReplaceToVersion());
        target.setReplacedByVersion(source.getMaintainableArtefact().getReplacedByVersion());
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));

        // TODO completar metadatos de metamac
    }

    private ResourceLink toDataStructureSelfLink(DataStructureDefinitionVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_DATA_STRUCTURE, toDataStructureLink(source));
    }

    private ResourceLink toDataStructureParentLink(DataStructureDefinitionVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_DATA_STRUCTURES, toDataStructuresLink(null, null, null));
    }

    private ChildLinks toDataStructureChildLinks(DataStructureDefinitionVersionMetamac source) {
        return null; // TODO habrá childlinks?
    }

    private Resource toResource(DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_DATA_STRUCTURE, toDataStructureSelfLink(source));
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
}