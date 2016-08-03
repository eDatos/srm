package org.siemac.metamac.srm.soap.external.v1_0.mapper.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;

public abstract class ItemSchemeBaseDo2SoapMapperV10Impl extends BaseDo2SoapMapperV10Impl {

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    protected boolean canResourceBeProvidedByApi(ItemSchemeVersion source) {
        return canResourceBeProvidedByApi(source.getMaintainableArtefact()) && canItemSchemeVersionBeProvidedByApi(source);
    }

    protected String toItemSchemeReplaceToVersion(ItemSchemeVersion source) {
        return toItemSchemeReplaceMetadataIfResourceCanBeProvidedByApi(source.getItemScheme().getId(), source.getMaintainableArtefact().getReplaceToVersion());
    }

    protected String toItemSchemeReplacedByVersion(ItemSchemeVersion source) {
        return toItemSchemeReplaceMetadataIfResourceCanBeProvidedByApi(source.getItemScheme().getId(), source.getMaintainableArtefact().getReplacedByVersion());
    }

    private String toItemSchemeReplaceMetadataIfResourceCanBeProvidedByApi(Long itemSchemeId, String version) {
        if (version == null) {
            return null;
        }
        ItemSchemeVersion itemSchemeVersion = itemSchemeVersionRepository.retrieveByVersion(itemSchemeId, version);
        if (itemSchemeVersion != null && canResourceBeProvidedByApi(itemSchemeVersion)) {
            return version;
        } else {
            return null;
        }
    }

    protected abstract boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source);
}