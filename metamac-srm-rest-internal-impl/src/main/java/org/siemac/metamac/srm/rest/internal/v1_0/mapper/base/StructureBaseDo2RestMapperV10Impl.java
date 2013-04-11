package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;

public abstract class StructureBaseDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl {

    @Autowired
    private StructureVersionRepository structureVersionRepository;

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