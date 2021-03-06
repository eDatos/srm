package org.siemac.metamac.srm.rest.external.v1_0.mapper.base;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Annotation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Annotations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ItemResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.LifeCycle;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.external.invocation.StatisticalOperationsRestExternalFacade;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;
import com.arte.statistic.sdmx.srm.core.common.domain.IdentifiableArtefactResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

public abstract class BaseDo2RestMapperV10Impl {

    @Autowired
    private ConfigurationService                    configurationService;

    @Autowired
    private StatisticalOperationsRestExternalFacade statisticalOperationsRestExternalFacade;

    private String                                  srmApiExternalEndpointV10;
    private String                                  statisticalOperationsApiExternalEndpoint;
    private String                                  statisticalResourcesApiExternalEndpoint;

    private String                                  maintainerUrnDefault;

    @PostConstruct
    public void init() throws Exception {
        initEndpoints();
    }

    public String getSrmApiExternalEndpointV10() {
        return srmApiExternalEndpointV10;
    }

    public String getStatisticalOperationsApiExternalEndpoint() {
        return statisticalOperationsApiExternalEndpoint;
    }

    public String getStatisticalResourcesApiExternalEndpoint() {
        return statisticalResourcesApiExternalEndpoint;
    }

    public void toAnnotableArtefact(AnnotableArtefact source, org.siemac.metamac.rest.structural_resources.v1_0.domain.AnnotableArtefact target) {
        if (source == null) {
            return;
        }
        target.setAnnotations(toAnnotations(source.getAnnotations()));
    }

    public void toIdentifiableArtefact(IdentifiableArtefact source, org.siemac.metamac.rest.structural_resources.v1_0.domain.IdentifiableArtefact target, Boolean isImported) {
        if (source == null) {
            return;
        }
        toAnnotableArtefact(source, target);

        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        if (BooleanUtils.isTrue(isImported)) {
            target.setUrnProvider(source.getUrnProvider());
        } else {
            target.setUrnProvider(null);
        }
        target.setUri(source.getUriProvider());
    }

    public void toNameableArtefact(NameableArtefact source, org.siemac.metamac.rest.structural_resources.v1_0.domain.NameableArtefact target, Boolean isImported) {
        if (source == null) {
            return;
        }
        toIdentifiableArtefact(source, target, isImported);

        target.setName(toInternationalString(source.getName()));
        target.setDescription(toInternationalString(source.getDescription()));
    }

    public void toVersionableArtefact(MaintainableArtefact source, org.siemac.metamac.rest.structural_resources.v1_0.domain.VersionableArtefact target, Boolean isImported) {
        if (source == null) {
            return;
        }
        toNameableArtefact(source, target, isImported);

        target.setVersion(source.getVersionLogic());
    }

    public void toMaintainableArtefact(MaintainableArtefact source, SrmLifeCycleMetadata lifeCycleSource, org.siemac.metamac.rest.structural_resources.v1_0.domain.MaintainableArtefact target) {
        if (source == null) {
            return;
        }

        toVersionableArtefact(source, target, source.getIsImported());

        target.setLifeCycle(toLifeCycle(lifeCycleSource));
        if (!SdmxSrmUtils.isAgencySchemeSdmx(source.getUrn())) { // Maintainer is only null for AgencyScheme SDMX
            target.setAgencyID(source.getMaintainer().getIdAsMaintainer());
        } else {
            target.setAgencyID(SdmxConstants.AGENCY_SDMX_CODE);
        }
        target.setIsFinal(source.getFinalLogic());
        target.setIsExternalReference(source.getIsExternalReference());
        target.setValidFrom(toDate(source.getValidFrom()));
        target.setValidTo(toDate(source.getValidTo()));
        target.setUri(source.getUriProvider()); // can be overrided by selfLink if resource is not imported
        target.setServiceUrl(source.getServiceURL());
        target.setStructureUrl(source.getStructureURL());
    }

    protected InternationalString toInternationalString(com.arte.statistic.sdmx.srm.core.common.domain.InternationalString sources) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString source : sources.getTexts()) {
            LocalisedString target = new LocalisedString();
            target.setValue(source.getLabel());
            target.setLang(source.getLocale());
            targets.getTexts().add(target);
        }
        return targets;
    }

    protected InternationalString toInternationalString(Map<String, String> sources) {
        if (MapUtils.isEmpty(sources)) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (String locale : sources.keySet()) {
            LocalisedString target = new LocalisedString();
            target.setValue(sources.get(locale));
            target.setLang(locale);
            targets.getTexts().add(target);
        }
        return targets;
    }

    private Annotations toAnnotations(Set<com.arte.statistic.sdmx.srm.core.base.domain.Annotation> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        Annotations targets = new Annotations();
        targets.setTotal(BigInteger.valueOf(sources.size()));
        for (com.arte.statistic.sdmx.srm.core.base.domain.Annotation source : sources) {
            Annotation target = toAnnotation(source);
            targets.getAnnotations().add(target);
        }
        return targets;
    }

    private Annotation toAnnotation(com.arte.statistic.sdmx.srm.core.base.domain.Annotation source) {
        if (source == null) {
            return null;
        }
        Annotation target = new Annotation();
        target.setId(source.getCode());
        target.setTitle(source.getTitle());
        target.setType(source.getType());
        target.setUrl(source.getUrl());
        target.setText(toInternationalString(source.getText()));
        return target;
    }

    protected org.siemac.metamac.rest.structural_resources.v1_0.domain.LifeCycle toLifeCycle(org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata source) {
        if (source == null) {
            return null;
        }
        LifeCycle target = new LifeCycle();
        target.setLastUpdatedDate(toDate(source.getExternalPublicationDate()));
        return target;
    }

    protected ProcStatus toProcStatus(ProcStatusEnum source) {
        switch (source) {
            case INTERNALLY_PUBLISHED:
                return org.siemac.metamac.rest.structural_resources.v1_0.domain.ProcStatus.INTERNALLY_PUBLISHED;
            case EXTERNALLY_PUBLISHED:
                return org.siemac.metamac.rest.structural_resources.v1_0.domain.ProcStatus.EXTERNALLY_PUBLISHED;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    protected Date toDate(DateTime source) {
        return RestCommonUtil.transformDateTimeToDate(source);
    }

    protected String getCode(IdentifiableArtefact identifiableArtefact) {
        if (identifiableArtefact.getCodeFull() != null) {
            return identifiableArtefact.getCodeFull();
        } else {
            return identifiableArtefact.getCode();
        }
    }

    protected String getCode(IdentifiableArtefactResult identifiableArtefactResult) {
        if (identifiableArtefactResult.getCodeFull() != null) {
            return identifiableArtefactResult.getCodeFull();
        } else {
            return identifiableArtefactResult.getCode();
        }
    }

    protected boolean canResourceBeProvidedByApi(MaintainableArtefact source) {
        return source.getFinalLogicClient();
    }

    protected void toResource(IdentifiableArtefact source, String kind, ResourceLink selfLink, Resource target, Boolean isImported) {
        if (source == null) {
            return;
        }
        target.setId(source.getCode());
        // nestedId: only filled to some resource
        target.setUrn(source.getUrn());
        target.setKind(kind);
        target.setSelfLink(selfLink);
    }

    protected void toResource(NameableArtefact source, String kind, ResourceLink selfLink, Resource target, Boolean isImported, Set<String> fields) {
        if (source == null) {
            return;
        }
        toResource((IdentifiableArtefact) source, kind, selfLink, target, isImported);
        target.setName(toInternationalString(source.getName()));
        if (SrmRestInternalUtils.containsField(fields, SrmRestConstants.FIELD_INCLUDE_DESCRIPTION)) {
            target.setDescription(toInternationalString(source.getDescription()));
        }
    }

    protected void toResource(MaintainableArtefact source, String kind, ResourceLink selfLink, Resource target) {
        toResource(source, kind, selfLink, target, source.getIsImported(), null);
    }

    protected void toResource(IdentifiableArtefactResult source, String kind, ResourceLink selfLink, Resource target, Boolean isImported) {
        if (source == null) {
            return;
        }
        target.setId(source.getCode());
        // nestedId: only filled to some resource
        target.setUrn(source.getUrn());
        target.setKind(kind);
        target.setSelfLink(selfLink);
    }

    protected void toResource(ItemResult source, String kind, ResourceLink selfLink, ItemResource target, Boolean isImported, Set<String> fields) {
        if (source == null) {
            return;
        }
        toResource((IdentifiableArtefactResult) source, kind, selfLink, target, isImported);
        target.setName(toInternationalString(source.getName()));
        if (SrmRestInternalUtils.containsField(fields, SrmRestConstants.FIELD_INCLUDE_DESCRIPTION)) {
            target.setDescription(toInternationalString(source.getDescription()));
        }
        if (source.getParent() != null) {
            target.setParent(source.getParent().getUrn());
        }
    }

    protected Resource toResourceExternalItemStatisticalOperation(ExternalItem source) {
        if (source == null) {
            return null;
        }
        Resource resource = toResourceExternalItem(source, statisticalOperationsApiExternalEndpoint);
        resource.setName(getUpdatedStatisticalOperationName(source.getCode()));
        return resource;
    }

    protected Resource toResourceExternalItem(ExternalItem source, String apiExternalItemBaseUrl) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(source.getCode());
        target.setNestedId(source.getCodeNested());
        target.setUrn(source.getUrn());
        target.setKind(source.getType().getValue());
        target.setSelfLink(toResourceLink(target.getKind(), RestUtils.createLink(apiExternalItemBaseUrl, source.getUri())));
        target.setName(toInternationalString(source.getTitle()));
        return target;
    }

    protected ResourceLink toResourceLink(String kind, String href) {
        ResourceLink target = new ResourceLink();
        target.setKind(kind);
        target.setHref(href);
        return target;
    }

    // API/[schemesSubPath]
    // API/[schemesSubPath]/{agencyID}
    // API/[schemesSubPath]/{agencyID}/{resourceID}
    // API/[schemesSubPath]/{agencyID}/{resourceID}/{version}
    protected String toMaintainableArtefactLink(String schemesSubPath, String agencyID, String resourceID, String version) {
        String link = RestUtils.createLink(getSrmApiExternalEndpointV10(), schemesSubPath);
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

    protected InternationalString getUpdatedStatisticalOperationName(String operationCode) {
        Operation operation = statisticalOperationsRestExternalFacade.retrieveOperation(operationCode);
        return operation != null ? operation.getName() : null;
    }

    public String getMaintainerUrnDefault() {
        return maintainerUrnDefault;
    }

    private void initEndpoints() throws MetamacException {
        // Srm External Api V1.0
        String srmApiExternalEndpoint = configurationService.retrieveSrmExternalApiUrlBase();
        srmApiExternalEndpointV10 = RestUtils.createLink(srmApiExternalEndpoint, SrmRestConstants.API_VERSION_1_0);

        // Statistical operations External Api (do not add api version! it is already stored in database (~latest))
        statisticalOperationsApiExternalEndpoint = configurationService.retrieveStatisticalOperationsExternalApiUrlBase();
        statisticalOperationsApiExternalEndpoint = StringUtils.removeEnd(statisticalOperationsApiExternalEndpoint, "/");

        statisticalResourcesApiExternalEndpoint = configurationService.retrieveStatisticalResourcesExternalApiUrlBase();
        statisticalResourcesApiExternalEndpoint = StringUtils.removeEnd(statisticalResourcesApiExternalEndpoint, "/");

        maintainerUrnDefault = configurationService.retrieveOrganisationUrn();
    }
}