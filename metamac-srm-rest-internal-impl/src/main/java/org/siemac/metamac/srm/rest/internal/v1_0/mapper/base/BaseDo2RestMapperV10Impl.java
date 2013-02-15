package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.constants.RestEndpointsConstants;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
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

    @Override
    public InternationalString toInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString sources) {
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

    @Override
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle toLifeCycle(org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata source) {
        LifeCycle target = new LifeCycle();
        target.setProcStatus(toProcStatus(source.getProcStatus()));
        target.setProductionValidationDate(toDate(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(toDate(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setInternalPublicationDate(toDate(source.getInternalPublicationDate()));
        target.setInternalPublicationUser(source.getInternalPublicationUser());
        target.setExternalPublicationDate(toDate(source.getExternalPublicationDate()));
        target.setExternalPublicationUser(source.getExternalPublicationUser());
        target.setIsExternalPublicationFailed(source.getIsExternalPublicationFailed());
        target.setExternalPublicationFailedDate(toDate(source.getExternalPublicationFailedDate()));
        return target;
    }

    @Override
    public ProcStatus toProcStatus(ProcStatusEnum source) {
        switch (source) {
            case INTERNALLY_PUBLISHED:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus.INTERNALLY_PUBLISHED;
            case EXTERNALLY_PUBLISHED:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus.EXTERNALLY_PUBLISHED;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Date toDate(DateTime source) {
        return RestCommonUtil.transformDateTimeToDate(source);
    }

    protected Resource toResource(NameableArtefact source, String kind, ResourceLink selfLink) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(source.getCode());
        target.setUrn(source.getUrnProvider());
        target.setKind(kind);
        target.setSelfLink(selfLink);
        target.setTitle(toInternationalString(source.getName()));
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

    protected ResourceLink toResourceLink(String kind, String href) {
        ResourceLink target = new ResourceLink();
        target.setKind(kind);
        target.setHref(href);
        return target;
    }

    // API/[ARTEFACT_TYPE]
    // API/[ARTEFACT_TYPE]/{agencyID}
    // API/[ARTEFACT_TYPE]/{agencyID}/{resourceID}
    // API/[ARTEFACT_TYPE]/{agencyID}/{resourceID}/{version}
    protected String toMaintainableArtefactLink(String schemesSubPath, String agencyID, String resourceID, String version) {
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
        return toMaintainableArtefactLink(schemesSubPath, maintainableArtefact);
    }

    protected String toMaintainableArtefactLink(String subPath, MaintainableArtefact maintainableArtefact) {
        return toMaintainableArtefactLink(subPath, getIdAsMaintainer(maintainableArtefact.getMaintainer()), getCode(maintainableArtefact), maintainableArtefact.getVersionLogic());
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
            return SdmxAlias.SDMX_MAINTAINER;
        }
    }

    private String getCode(NameableArtefact nameableArtefact) {
        if (nameableArtefact.getCodeFull() != null) {
            return nameableArtefact.getCodeFull();
        } else {
            return nameableArtefact.getCode();
        }
    }
}