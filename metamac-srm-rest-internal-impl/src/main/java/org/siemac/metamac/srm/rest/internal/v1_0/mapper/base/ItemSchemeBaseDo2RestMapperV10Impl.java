package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import org.siemac.metamac.rest.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public abstract class ItemSchemeBaseDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl {

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

    protected String toItemSchemeLink(String schemesSubPath, ItemSchemeVersion itemSchemeVersion) {
        MaintainableArtefact maintainableArtefact = itemSchemeVersion.getMaintainableArtefact();
        return toMaintainableArtefactLink(schemesSubPath, maintainableArtefact);
    }

    // API/[ARTEFACT_TYPE]/{agencyID}/{resourceID}/{version}/[SUBARTEFACT_TYPES]
    protected String toItemsLink(String schemesSubPath, String itemsSubPath, String agencyID, String resourceID, String version) {
        String link = toMaintainableArtefactLink(schemesSubPath, agencyID, resourceID, version);
        link = RestUtils.createLink(link, itemsSubPath);
        return link;
    }

    protected String toItemsLink(String schemesSubPath, String itemsSubPath, ItemSchemeVersion itemSchemeVersion) {
        MaintainableArtefact maintainableArtefact = itemSchemeVersion.getMaintainableArtefact();
        return toItemsLink(schemesSubPath, itemsSubPath, getIdAsMaintainer(maintainableArtefact.getMaintainer()), getCode(maintainableArtefact), maintainableArtefact.getVersionLogic());
    }

    // API/[ARTEFACT_TYPE]/{agencyID}/{resourceID}/{version}/[SUBARTEFACT_TYPES]/{itemID}
    protected String toItemLink(String schemesSubPath, String itemsSubPath, Item item) {
        String link = toItemsLink(schemesSubPath, itemsSubPath, item.getItemSchemeVersion());
        link = RestUtils.createLink(link, getCode(item.getNameableArtefact()));
        return link;
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