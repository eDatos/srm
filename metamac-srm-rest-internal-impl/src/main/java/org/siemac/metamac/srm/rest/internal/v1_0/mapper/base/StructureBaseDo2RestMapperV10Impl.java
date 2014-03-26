package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;

public abstract class StructureBaseDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl {

    @Autowired
    protected CommonDo2RestMapperV10   commonDo2RestMapper;

    @Autowired
    private StructureVersionRepository structureVersionRepository;

    protected void toStructure(StructureVersion source, SrmLifeCycleMetadata lifeCycleSource, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure target) {

        toMaintainableArtefact(source.getMaintainableArtefact(), lifeCycleSource, target);

        target.setReplaceToVersion(toStructureReplaceToVersion(source));
        target.setReplacedByVersion(toStructureReplacedByVersion(source));
        target.setResourceCreatedDate(toDate(source.getStructure().getResourceCreatedDate()));
        target.setVersionCreatedDate(toDate(source.getCreatedDate()));

        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
    }

    protected boolean canResourceBeProvidedByApi(StructureVersion source) {
        return canResourceBeProvidedByApi(source.getMaintainableArtefact()) && canStructureVersionBeProvidedByApi(source);
    }

    protected String toStructureReplaceToVersion(StructureVersion source) {
        return toStructureReplaceMetadataIfResourceCanBeProvidedByApi(source.getStructure().getId(), source.getMaintainableArtefact().getReplaceToVersion());
    }

    protected String toStructureReplacedByVersion(StructureVersion source) {
        return toStructureReplaceMetadataIfResourceCanBeProvidedByApi(source.getStructure().getId(), source.getMaintainableArtefact().getReplacedByVersion());
    }

    private String toStructureReplaceMetadataIfResourceCanBeProvidedByApi(Long structureId, String version) {
        if (version == null) {
            return null;
        }
        StructureVersion structureVersion = structureVersionRepository.retrieveByVersion(structureId, version);
        if (structureVersion != null && canResourceBeProvidedByApi(structureVersion)) {
            return version;
        } else {
            return null;
        }
    }

    protected abstract boolean canStructureVersionBeProvidedByApi(StructureVersion source);

}