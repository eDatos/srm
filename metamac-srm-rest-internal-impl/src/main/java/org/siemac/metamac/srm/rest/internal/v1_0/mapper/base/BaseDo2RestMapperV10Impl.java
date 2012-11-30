package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.constants.RestEndpointsConstants;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Urns;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.common.constants.SdmxSrmConstants;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

@Component
public class BaseDo2RestMapperV10Impl implements BaseDo2RestMapperV10 {

    @Autowired
    private ConfigurationService configurationService;

    private String               srmApiInternalEndpointV10;
    private String               statisticalOperationsApiInternalEndpoint;

    @PostConstruct
    public void init() throws Exception {
        // Srm Internal Api V1.0
        String srmApiInternalEndpoint = readProperty(RestEndpointsConstants.SRM_INTERNAL_API);
        srmApiInternalEndpointV10 = RestUtils.createLink(srmApiInternalEndpoint, RestInternalConstants.API_VERSION_1_0);

        // Statistical operations Internal Api
        statisticalOperationsApiInternalEndpoint = readProperty(RestEndpointsConstants.STATISTICAL_OPERATIONS_INTERNAL_API);
    }

    public String getSrmApiInternalEndpointV10() {
        return srmApiInternalEndpointV10;
    }

    public String getStatisticalOperationsApiInternalEndpoint() {
        return statisticalOperationsApiInternalEndpoint;
    }

    protected Resource toResource(NameableArtefact source, String kind, ResourceLink selfLink) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setKind(kind);
        target.setSelfLink(selfLink);
        target.setTitle(toInternationalString(source.getName()));
        return target;
    }

    @SuppressWarnings("rawtypes")
    protected Urns itemsToUrns(List sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        Urns target = new Urns();
        target.setKind("TODO"); // TODO kind
        for (Object sourceObject : sources) {
            com.arte.statistic.sdmx.srm.core.base.domain.Item source = (Item) sourceObject;
            // TODO utilidad para obtener urn
            if (source.getNameableArtefact().getUrnProvider() != null) {
                target.getUrns().add(source.getNameableArtefact().getUrnProvider());
            } else {
                target.getUrns().add(source.getNameableArtefact().getUrn());
            }
        }
        target.setTotal(BigInteger.valueOf(target.getUrns().size()));
        return target;
    }

    protected Resource toResourceExternalItemStatisticalOperation(ExternalItem source) {
        if (source == null) {
            return null;
        }
        return toResourceExternalItem(source, statisticalOperationsApiInternalEndpoint);
    }

    protected Resource toResourceExternalItem(ExternalItem source, String apiExternalItem) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setKind(source.getType().name());
        target.setSelfLink(toResourceLink(target.getKind(), RestUtils.createLink(apiExternalItem, source.getUri())));
        target.setTitle(toInternationalString(source.getTitle()));
        return target;
    }

    protected InternationalString toInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString sources) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString source : sources.getTexts()) {
            LocalisedString target = new LocalisedString();
            target.setValue(source.getLabel());
            target.setLang(source.getLocale());
            targets.getTexts().add(target);
        }
        return targets;
    }

    protected ResourceLink toResourceLink(String kind, String href) {
        ResourceLink target = new ResourceLink();
        target.setKind(kind);
        target.setHref(href);
        return target;
    }

    // API/[SCHEMES]
    // API/[SCHEMES]/{agencyID}
    // API/[SCHEMES]/{agencyID}/{resourceID}
    // API/[SCHEMES]/{agencyID}/{resourceID}/{version}
    protected String toItemSchemesLink(String schemesSubPath, String agencyID, String resourceID, String version) {
        String link = RestUtils.createLink(getSrmApiInternalEndpointV10(), schemesSubPath);
        if (agencyID != null) {
            link = RestUtils.createLink(link, agencyID);
            if (resourceID != null) {
                link = RestUtils.createLink(link, resourceID);
                if (version != null) {
                    link = RestUtils.createLink(link, version);
                }
            }
        }
        return link;
    }
    protected String toItemSchemeLink(String schemesSubPath, ItemSchemeVersion itemSchemeVersion) {
        MaintainableArtefact maintainableArtefact = itemSchemeVersion.getMaintainableArtefact();
        return toItemSchemeLink(schemesSubPath, maintainableArtefact);
    }
    protected String toItemSchemeLink(String schemesSubPath, MaintainableArtefact maintainableArtefact) {
        return toItemSchemesLink(schemesSubPath, getIdAsMaintainer(maintainableArtefact.getMaintainer()), maintainableArtefact.getCode(), maintainableArtefact.getVersionLogic());
    }

    // API/[SCHEMES]/{agencyID}/{resourceID}/{version}/[ITEMS]
    protected String toItemsLink(String schemesSubPath, String itemsSubPath, String agencyID, String resourceID, String version) {
        String link = toItemSchemesLink(schemesSubPath, agencyID, resourceID, version);
        link = RestUtils.createLink(link, itemsSubPath);
        return link;
    }
    protected String toItemsLink(String schemesSubPath, String itemsSubPath, ItemSchemeVersion itemSchemeVersion) {
        MaintainableArtefact maintainableArtefact = itemSchemeVersion.getMaintainableArtefact();
        return toItemsLink(schemesSubPath, itemsSubPath, getIdAsMaintainer(maintainableArtefact.getMaintainer()), maintainableArtefact.getCode(), maintainableArtefact.getVersionLogic());
    }

    // API/[SCHEMES]/{agencyID}/{resourceID}/{version}/[ITEMS]/{itemID}
    protected String toItemLink(String schemesSubPath, String itemsSubPath, Item item) {
        String link = toItemsLink(schemesSubPath, itemsSubPath, item.getItemSchemeVersion());
        link = RestUtils.createLink(link, item.getNameableArtefact().getCode());
        return link;
    }

    private String readProperty(String property) {
        String propertyValue = configurationService.getProperty(property);
        if (propertyValue == null) {
            throw new BeanCreationException("Property not found: " + property);
        }
        return propertyValue;
    }

    private String getIdAsMaintainer(Organisation mantainer) {
        if (mantainer != null) {
            return mantainer.getIdAsMaintainer();
        } else {
            // default SDMX
            return SdmxSrmConstants.SDMX_MAINTAINER;
        }
    }
}