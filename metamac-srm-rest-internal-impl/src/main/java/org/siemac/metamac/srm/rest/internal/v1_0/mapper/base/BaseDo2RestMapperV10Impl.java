package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Annotation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Annotations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Representation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.utils.RestCommonUtil;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.InternalWebApplicationNavigation;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.common.domain.IdentifiableArtefactResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.constants.SdmxAlias;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.structure.mapper.StructureDo2JaxbMapper;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

public abstract class BaseDo2RestMapperV10Impl {

    @Autowired
    private ConfigurationService             configurationService;

    private String                           srmInternalWebApplication;
    private String                           srmApiInternalEndpointV10;
    private String                           statisticalOperationsInternalWebApplication;
    private String                           statisticalOperationsApiInternalEndpoint;

    private InternalWebApplicationNavigation internalWebApplicationNavigation;

    @Autowired
    private StructureDo2JaxbMapper           structureDo2JaxbMapper;                     // TODO QUITAR

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

    public void toAnnotableArtefact(AnnotableArtefact source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AnnotableArtefact target) {
        if (source == null) {
            return;
        }
        target.setAnnotations(toAnnotations(source.getAnnotations()));
    }

    public void toIdentifiableArtefact(IdentifiableArtefact source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.IdentifiableArtefact target) {
        if (source == null) {
            return;
        }
        toAnnotableArtefact(source, target);

        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
        target.setUri(source.getUriProvider());
    }

    public void toNameableArtefact(NameableArtefact source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.NameableArtefact target) {
        if (source == null) {
            return;
        }
        toIdentifiableArtefact(source, target);

        target.setName(toInternationalString(source.getName()));
        target.setDescription(toInternationalString(source.getDescription()));
        target.setComment(toInternationalString(source.getComment()));
    }

    public void toVersionableArtefact(MaintainableArtefact source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VersionableArtefact target) {
        if (source == null) {
            return;
        }
        toNameableArtefact(source, target);

        target.setVersion(source.getVersionLogic());
    }

    public void toMaintainableArtefact(MaintainableArtefact source, SrmLifeCycleMetadata lifeCycleSource, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.MaintainableArtefact target) {
        if (source == null) {
            return;
        }

        toVersionableArtefact(source, target);

        target.setLifeCycle(toLifeCycle(lifeCycleSource));
        target.setAgencyID(source.getMaintainer().getIdAsMaintainer());
        target.setIsFinal(source.getFinalLogic());
        target.setIsExternalReference(source.getIsExternalReference());
        target.setValidFrom(toDate(source.getValidFrom()));
        target.setValidTo(toDate(source.getValidTo()));
        target.setUri(source.getUriProvider()); // can be overrided by selfLink if resource is not imported
        target.setServiceUrl(source.getServiceURL());
        target.setStructureUrl(source.getStructureURL());
    }

    protected Representation toRepresentation(com.arte.statistic.sdmx.srm.core.base.domain.Representation source) {
        if (source == null) {
            return null;
        }
        Representation target = new Representation();

        if (RepresentationTypeEnum.TEXT_FORMAT.equals(source.getRepresentationType())) {
            // target.setTextFormat(toRepresentationTextFormat(source.getTextFormat())); // TODO
            target.setTextFormat(structureDo2JaxbMapper.facetBasicComponentTextFormatDoToJaxb(source.getTextFormat()));
        } else if (RepresentationTypeEnum.ENUMERATION.equals(source.getRepresentationType())) {
            if (source.getEnumerationCodelist() != null) {
                target.setEnumerationCodelist(source.getEnumerationCodelist().getMaintainableArtefact().getUrn());
            } else if (source.getEnumerationConceptScheme() != null) {
                target.setEnumerationConceptScheme(source.getEnumerationConceptScheme().getMaintainableArtefact().getUrn());
            }
        }
        return target;
    }

    // TODO toRepresentationTextFormat
    // private TextFormat toRepresentationTextFormat(Facet source) {
    // if (source == null) {
    // return null;
    // }
    //
    // TextFormat target = new TextFormat();
    // target.setTextType(toDataType(source.getFacetValue()));
    // target.setIsSequence(CoreCommonUtil.transformBooleanLexicalRepresentationToBoolean(source.getIsSequenceFT()));
    // target.setInterval(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getIntervalFT()));
    // target.setStartValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getStartValueFT()));
    // target.setEndValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getEndValueFT()));
    // target.setTimeInterval(CoreCommonUtil.transformLexicalRepresentationToDuration(source.getTimeIntervalFT()));
    // target.setStartTime(source.getStartTimeFT());
    // target.setEndTime(source.getEndTimeFT());
    // target.setMinLength(CoreCommonUtil.integerLexicalRepresentation2BigInteger(source.getMinLengthFT()));
    // target.setMaxLength(CoreCommonUtil.integerLexicalRepresentation2BigInteger(source.getMaxLengthFT()));
    // target.setMinValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getMinValueFT()));
    // target.setMaxValue(CoreCommonUtil.doubleLexicalRepresentation2BigDecimal(source.getMaxValueFT()));
    // target.setDecimals(CoreCommonUtil.integerLexicalRepresentation2BigInteger(source.getDecimalsFT()));
    // target.setPattern(source.getPatternFT());
    // target.setIsMultiLingual(CoreCommonUtil.transformBooleanLexicalRepresentationToBoolean(source.getIsMultiLingual()));
    // return target;
    // }
    // TODO toDataType
    // private DataType toDataType(FacetValueTypeEnum source) {
    // if (source == null) {
    // return null;
    // }
    // return DataType.fromValue(source.getValue());
    // }

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

    protected org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.LifeCycle toLifeCycle(org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata source) {
        if (source == null) {
            return null;
        }
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

    protected void toResource(NameableArtefact source, String kind, ResourceLink selfLink, String managementAppUrl, ResourceInternal target) {
        if (source == null) {
            return;
        }
        target.setId(source.getCode());
        // nestedId: only filled to some resource
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
        target.setKind(kind);
        target.setSelfLink(selfLink);
        target.setName(toInternationalString(source.getName()));
        target.setManagementAppLink(managementAppUrl);
    }

    protected void toResource(Item source, String kind, ResourceLink selfLink, String managementAppUrl, ItemResourceInternal target) {
        if (source == null) {
            return;
        }
        toResource(source.getNameableArtefact(), kind, selfLink, managementAppUrl, target);
        if (source.getParent() != null) {
            target.setParent(source.getParent().getNameableArtefact().getUrn());
        }
    }

    protected void toResource(ItemResult source, String kind, ResourceLink selfLink, String managementAppUrl, ItemResourceInternal target) {
        if (source == null) {
            return;
        }
        target.setId(source.getCode());
        // nestedId: only filled to some resource
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
        target.setKind(kind);
        target.setSelfLink(selfLink);
        target.setName(toInternationalString(source.getName()));
        target.setManagementAppLink(managementAppUrl);
        if (source.getParent() != null) {
            target.setParent(source.getParent().getUrn());
        }
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
        target.setNestedId(source.getCodeNested());
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
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

    // API/[schemesSubPath]
    // API/[schemesSubPath]/{agencyID}
    // API/[schemesSubPath]/{agencyID}/{resourceID}
    // API/[schemesSubPath]/{agencyID}/{resourceID}/{version}
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

        // Statistical operations Internal Api (do not add api version! it is already stored in database (~latest))
        statisticalOperationsApiInternalEndpoint = readProperty(ConfigurationConstants.ENDPOINT_STATISTICAL_OPERATIONS_INTERNAL_API);
        statisticalOperationsApiInternalEndpoint = StringUtils.removeEnd(statisticalOperationsApiInternalEndpoint, "/");

        // Statistical operations internal application
        statisticalOperationsInternalWebApplication = readProperty(ConfigurationConstants.WEB_APPLICATION_STATISTICAL_OPERATIONS_INTERNAL_WEB);
        statisticalOperationsInternalWebApplication = StringUtils.removeEnd(statisticalOperationsInternalWebApplication, "/");
    }
}