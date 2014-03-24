package org.siemac.metamac.srm.rest.external.v1_0.mapper.base;

import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public abstract class ItemSchemeBaseDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl {

    @Autowired
    protected CommonDo2RestMapperV10    commonDo2RestMapper;

    @Autowired
    private ItemSchemeVersionRepository itemSchemeVersionRepository;

    protected void toItemScheme(ItemSchemeVersion source, SrmLifeCycleMetadata lifeCycleSource, org.siemac.metamac.rest.structural_resources.v1_0.domain.ItemScheme target) {

        toMaintainableArtefact(source.getMaintainableArtefact(), lifeCycleSource, target);

        target.setIsPartial(source.getIsPartial());
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setCreatedDate(toDate(source.getItemScheme().getResourceCreatedDate()));

        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
    }

    protected void toItem(Item source, org.siemac.metamac.rest.structural_resources.v1_0.domain.Item target) {

        toNameableArtefact(source.getNameableArtefact(), target, source.getItemSchemeVersion().getMaintainableArtefact().getIsImported());

        if (source.getParent() != null) {
            target.setParent(source.getParent().getNameableArtefact().getUrn());
        }

        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
    }

    protected void toResource(Item source, String kind, ResourceLink selfLink, String managementAppUrl, ItemResourceInternal target) {
        if (source == null) {
            return;
        }
        toResource(source.getNameableArtefact(), kind, selfLink, managementAppUrl, target, source.getItemSchemeVersion().getMaintainableArtefact().getIsImported());
        if (source.getParent() != null) {
            target.setParent(source.getParent().getNameableArtefact().getUrn());
        }
    }

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
    protected String toItemLink(String schemesSubPath, String itemsSubPath, ItemResult item, ItemSchemeVersion itemSchemeVersion) {
        String link = toItemsLink(schemesSubPath, itemsSubPath, itemSchemeVersion);
        link = RestUtils.createLink(link, getCode(item));
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