package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import java.math.BigInteger;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.constants.shared.ConfigurationConstants;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.InternalWebApplicationNavigation;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

public abstract class BaseDo2RestMapperV10Impl {

    @Autowired
    private ConfigurationService             configurationService;

    private String                           srmInternalWebApplication;
    private String                           srmApiInternalEndpointV10;
    private String                           statisticalOperationsInternalWebApplication;
    private String                           statisticalOperationsApiInternalEndpoint;

    private InternalWebApplicationNavigation internalWebApplicationNavigation;

    @PostConstruct
    public void init() throws Exception {
        initEndpoints();

        internalWebApplicationNavigation = new InternalWebApplicationNavigation(srmInternalWebApplication);
    }

    public String getSrmApiInternalEndpointV10() {
        return srmApiInternalEndpointV10;
    }

    public String getStatisticalOperationsApiInternalEndpoint() {
        return statisticalOperationsApiInternalEndpoint;
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

    protected org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle toLifeCycle(org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata source) {
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
        return target;
    }

    protected ProcStatus toProcStatus(ProcStatusEnum source) {
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

    protected Date toDate(DateTime source) {
        return RestCommonUtil.transformDateTimeToDate(source);
    }

    protected BigInteger toBigInteger(Integer source) {
        if (source == null) {
            return null;
        }
        return BigInteger.valueOf(source.longValue());
    }

    protected String getCode(IdentifiableArtefact identifiableArtefact) {
        if (identifiableArtefact.getCodeFull() != null) {
            return identifiableArtefact.getCodeFull();
        } else {
            return identifiableArtefact.getCode();
        }
    }
    protected String getUrn(IdentifiableArtefact identifiableArtefact) {
        return identifiableArtefact.getUrnProvider();
    }

    protected boolean canResourceBeProvidedByApi(MaintainableArtefact source) {
        return source.getFinalLogicClient();
    }

    protected ResourceInternal toResource(NameableArtefact source, String kind, ResourceLink selfLink, String managementAppUrl) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        toResource(source, kind, selfLink, managementAppUrl, target);
        return target;
    }

    protected void toResource(NameableArtefact source, String kind, ResourceLink selfLink, String managementAppUrl, ResourceInternal target) {
        if (source == null) {
            return;
        }
        target.setId(source.getCode());
        target.setUrn(source.getUrnProvider());
        target.setUrnInternal(source.getUrn());
        target.setKind(kind);
        target.setSelfLink(selfLink);
        target.setName(toInternationalString(source.getName()));
        target.setManagementAppLink(managementAppUrl);
    }

    protected ResourceInternal toResourceExternalItemStatisticalOperation(ExternalItem source) {
        if (source == null) {
            return null;
        }
        return toResourceExternalItem(source, statisticalOperationsApiInternalEndpoint, statisticalOperationsInternalWebApplication);
    }

    protected ResourceInternal toResourceExternalItem(ExternalItem source, String apiExternalItemBaseUrl, String managementAppBaseUrl) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setUrnInternal(source.getUrnInternal());
        target.setKind(source.getType().getValue());
        target.setSelfLink(toResourceLink(target.getKind(), RestUtils.createLink(apiExternalItemBaseUrl, source.getUri())));
        if (source.getManagementAppUrl() != null) {
            target.setManagementAppLink(RestUtils.createLink(managementAppBaseUrl, source.getManagementAppUrl()));
        }
        target.setName(toInternationalString(source.getTitle()));
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

    protected String toMaintainableArtefactLink(String subPath, MaintainableArtefact maintainableArtefact) {
        return toMaintainableArtefactLink(subPath, getIdAsMaintainer(maintainableArtefact.getMaintainer()), getCode(maintainableArtefact), maintainableArtefact.getVersionLogic());
    }

    protected String getIdAsMaintainer(Organisation mantainer) {
        if (mantainer != null) {
            return mantainer.getIdAsMaintainer();
        } else {
            // default SDMX
            return SdmxAlias.SDMX_MAINTAINER;
        }
    }

    protected InternalWebApplicationNavigation getInternalWebApplicationNavigation() {
        return internalWebApplicationNavigation;
    }

    private String readProperty(String property) {
        String propertyValue = configurationService.getProperty(property);
        if (propertyValue == null) {
            throw new BeanCreationException("Property not found: " + property);
        }
        return propertyValue;
    }

    private void initEndpoints() {
        // Srm internal application
        srmInternalWebApplication = readProperty(ConfigurationConstants.WEB_APPLICATION_SRM_INTERNAL_WEB);
        srmInternalWebApplication = StringUtils.removeEnd(srmInternalWebApplication, "/");

        // Srm Internal Api V1.0
        String srmApiInternalEndpoint = readProperty(ConfigurationConstants.ENDPOINT_SRM_INTERNAL_API);
        srmApiInternalEndpointV10 = RestUtils.createLink(srmApiInternalEndpoint, RestInternalConstants.API_VERSION_1_0);

        // Statistical operations Internal Api (do not add api version! it is already stored in database)
        statisticalOperationsApiInternalEndpoint = readProperty(ConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        statisticalOperationsApiInternalEndpoint = StringUtils.removeEnd(statisticalOperationsApiInternalEndpoint, "/");

        // Statistical operations internal application
        statisticalOperationsInternalWebApplication = readProperty(ConfigurationConstants.WEB_APPLICATION_STATISTICAL_OPERATIONS_INTERNAL_WEB);
        statisticalOperationsInternalWebApplication = StringUtils.removeEnd(statisticalOperationsInternalWebApplication, "/");
    }
}