package org.siemac.metamac.srm.rest.internal.v1_0.mapper.base;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.search.criteria.utils.CriteriaUtils;
import org.siemac.metamac.rest.search.criteria.utils.CriteriaUtils.PropertyValueRestToPropertyValueEntityInterface;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Annotations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactProperties.IdentifiableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParameters;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParametersInternal;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;
import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public abstract class BaseRest2DoMapperV10Impl {

    private final Logger                                    logger                                 = LoggerFactory.getLogger(BaseRest2DoMapperV10Impl.class);

    @Autowired
    private SrmConfiguration                                configurationService;

    @Autowired
    private ExternalItemRepository                          externalItemRepository;

    @Autowired
    private AnnotationRepository                            annotationRepository;

    @Autowired
    private InternationalStringRepository                   internationalStringRepository;

    @Autowired
    private OrganisationRepository                          organisationRepository;

    private final Whitelist                                 whitelist                              = Whitelist.none().addTags(new String[]{"b", "i", "br", "a", "p"}).addAttributes("a", "href");

    private PropertyValueRestToPropertyValueEntityInterface propertyValueRestToPropertyValueEntity = null;

    protected enum PropertyTypeEnum {
        STRING, DATE, BOOLEAN, PROC_STATUS, PROC_STATUS_ITEM_SCHEME_FROM_ITEM, ORGANISATION_SCHEME_TYPE, ORGANISATION_TYPE, CONCEPT_SCHEME_TYPE, VARIABLE_TYPE
    }

    public BaseRest2DoMapperV10Impl() {
        propertyValueRestToPropertyValueEntity = new PropertyValueRestToPropertyValueEntity();
    }

    @SuppressWarnings("rawtypes")
    protected SculptorPropertyCriteria buildSculptorPropertyCriteria(Property propertyEntity, PropertyTypeEnum propertyEntityType, MetamacRestQueryPropertyRestriction restPropertyRestriction) {
        return CriteriaUtils.buildSculptorPropertyCriteria(propertyEntity, propertyEntityType.name(), restPropertyRestriction, propertyValueRestToPropertyValueEntity);
    }

    private class PropertyValueRestToPropertyValueEntity implements PropertyValueRestToPropertyValueEntityInterface {

        @Override
        public Object restValueToEntityValue(String propertyName, String value, String propertyType) {
            if (value == null) {
                return null;
            }

            try {
                PropertyTypeEnum propertyTypeEnum = PropertyTypeEnum.valueOf(propertyType);
                switch (propertyTypeEnum) {
                    case STRING:
                        return value;
                    case DATE:
                        return CoreCommonUtil.transformISODateTimeLexicalRepresentationToDateTime(value).toDate();
                    case BOOLEAN:
                        return Boolean.valueOf(value);
                    case PROC_STATUS:
                        return ProcStatusEnum.valueOf(value);
                    case PROC_STATUS_ITEM_SCHEME_FROM_ITEM:
                        // This conversion is necesary due to error in queries with basicType of ItemScheme from Item property path
                        return procStatusItemSchemeToPublicLogic(propertyName, value);
                    case ORGANISATION_SCHEME_TYPE:
                        return OrganisationSchemeTypeEnum.valueOf(value);
                    case ORGANISATION_TYPE:
                        return OrganisationTypeEnum.valueOf(value);
                    case CONCEPT_SCHEME_TYPE:
                        return ConceptSchemeTypeEnum.valueOf(value);
                    case VARIABLE_TYPE:
                        return VariableTypeEnum.valueOf(value);
                    default:
                        throw toRestExceptionParameterIncorrect(propertyName);
                }
            } catch (Exception e) {
                logger.error("Error parsing Rest query", e);
                if (e instanceof RestException) {
                    throw (RestException) e;
                } else {
                    throw toRestExceptionParameterIncorrect(propertyName);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected SculptorPropertyCriteria buildSculptorPropertyCriteriaForDateProperty(MetamacRestQueryPropertyRestriction propertyRestriction, Property propertyEntity, Class entity, boolean embedded) {
        String propertyName = null;
        if (embedded) {
            propertyName = ((LeafProperty) propertyEntity).getEmbeddedName();
        } else {
            propertyName = propertyEntity.getName();
        }
        return buildSculptorPropertyCriteria(new LeafProperty(propertyName, CoreCommonConstants.CRITERIA_DATETIME_COLUMN_DATETIME, true, entity), PropertyTypeEnum.DATE, propertyRestriction);
    }

    @SuppressWarnings({"rawtypes"})
    protected SculptorPropertyCriteriaDisjunction buildSculptorPropertyCriteriaDisjunctionForUrnProperty(MetamacRestQueryPropertyRestriction propertyRestriction,
            MaintainableArtefactProperty maintainableArtefactProperty) {
        SculptorPropertyCriteria propertyCriteria1Urn = buildSculptorPropertyCriteria(maintainableArtefactProperty.urn(), PropertyTypeEnum.STRING, propertyRestriction);
        SculptorPropertyCriteria propertyCriteria2UrnProvider = buildSculptorPropertyCriteria(maintainableArtefactProperty.urnProvider(), PropertyTypeEnum.STRING, propertyRestriction);
        return new SculptorPropertyCriteriaDisjunction(propertyCriteria1Urn, propertyCriteria2UrnProvider);
    }

    @SuppressWarnings({"rawtypes"})
    protected SculptorPropertyCriteriaDisjunction buildSculptorPropertyCriteriaDisjunctionForUrnProperty(MetamacRestQueryPropertyRestriction propertyRestriction,
            NameableArtefactProperty nameableArtefactProperty) {
        SculptorPropertyCriteria propertyCriteria1Urn = buildSculptorPropertyCriteria(nameableArtefactProperty.urn(), PropertyTypeEnum.STRING, propertyRestriction);
        SculptorPropertyCriteria propertyCriteria2UrnProvider = buildSculptorPropertyCriteria(nameableArtefactProperty.urnProvider(), PropertyTypeEnum.STRING, propertyRestriction);
        return new SculptorPropertyCriteriaDisjunction(propertyCriteria1Urn, propertyCriteria2UrnProvider);
    }

    @SuppressWarnings({"rawtypes"})
    protected SculptorPropertyCriteriaDisjunction buildSculptorPropertyCriteriaDisjunctionForUrnProperty(MetamacRestQueryPropertyRestriction propertyRestriction,
            IdentifiableArtefactProperty identifiableArtefactProperty) {
        SculptorPropertyCriteria propertyCriteria1Urn = buildSculptorPropertyCriteria(identifiableArtefactProperty.urn(), PropertyTypeEnum.STRING, propertyRestriction);
        SculptorPropertyCriteria propertyCriteria2UrnProvider = buildSculptorPropertyCriteria(identifiableArtefactProperty.urnProvider(), PropertyTypeEnum.STRING, propertyRestriction);
        return new SculptorPropertyCriteriaDisjunction(propertyCriteria1Urn, propertyCriteria2UrnProvider);
    }

    protected RestException toRestExceptionParameterIncorrect(String parameter) {
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameter);
        throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
    }

    protected RestException toRestExceptionParameterUnexpected(String parameter) {
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_UNEXPECTED, parameter);
        throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
    }

    private Boolean procStatusItemSchemeToPublicLogic(String propertyName, String value) {
        ProcStatus procStatus = ProcStatus.valueOf(value);
        switch (procStatus) {
            case INTERNALLY_PUBLISHED:
                return Boolean.FALSE;
            case EXTERNALLY_PUBLISHED:
                return Boolean.TRUE;
            default:
                throw toRestExceptionParameterIncorrect(propertyName);
        }
    }

    public <T extends org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.MaintainableArtefact, U extends MaintainableArtefact> U maintainableArtefactRestToEntity(T source, U target)
            throws MetamacException {
        if (source == null) {
            return null;
        }

        target.setIsExternalReference(source.isIsExternalReference());
        target.setStructureURL(source.getStructureUrl());
        target.setServiceURL(source.getServiceUrl());

        // Other metatada
        target.setVersionLogic(source.getVersion());
        target.setValidFrom(CoreCommonUtil.transformDateToDateTime(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateToDateTime(source.getValidTo()));

        // target.setFinalLogic(Boolean.FALSE);
        // target.setFinalLogicClient(finalLogicClient);
        // target.setLatestFinal(latestFinal);
        // target.setPublicLogic(publicLogic);
        // target.setLatestPublic(latestPublic);
        // target.setIsLastVersion(isLastVersion);
        target.setReplacedByVersion(source.getReplacedByVersion());
        target.setReplaceToVersion(source.getReplaceToVersion());
        target.setIsImported(Boolean.FALSE);
        target.setIsTemporal(Boolean.FALSE);

        // Related entities

        // Maintainer
        Organisation organisation = organisationRepository.retrieveOrganisationsByIdAsMaintainer(source.getAgencyID());
        if (organisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_MAINTAINER_BY_CODE_NOT_FOUND).withMessageParameters(source.getAgencyID()).build();
        }
        target.setMaintainer(organisation);

        return nameableArtefactRestToEntity(source, target);
    }

    public <T extends org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.NameableArtefact, U extends NameableArtefact> U nameableArtefactRestToEntity(T source, U target)
            throws MetamacException {
        if (source == null) {
            return null;
        }

        // Related entities
        // Name
        target.setName(internationalStringRestToEntity(source.getName(), target.getName(), ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME));

        // Description
        target.setDescription(internationalStringRestToEntity(source.getDescription(), target.getDescription(), ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION));

        // Comment
        target.setComment(internationalStringRestToEntity(source.getComment(), target.getComment(), ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT));

        return identifiableArtefactRestToEntity(source, target);
    }

    public <T extends org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.IdentifiableArtefact, U extends IdentifiableArtefact> U identifiableArtefactRestToEntity(T source, U target)
            throws MetamacException {
        if (source == null) {
            return null;
        }

        // Metadata modifiable
        // If code has been updated, it is necessary to update artefact URN and artefact children URN
        target.setIsCodeUpdated(!StringUtils.equals(source.getId(), target.getCode()));

        target.setCode(source.getId());

        return annotableArtefactRestToEntity(source, target);
    }

    public <T extends org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AnnotableArtefact, U extends AnnotableArtefact> U annotableArtefactRestToEntity(T source, U target)
            throws MetamacException {
        if (source == null) {
            return null;
        }

        // Required target entity because this class is abstract
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.ANNOTABLE_ARTEFACT).build();
        }

        // Related entities: Annotations
        int annotationCount = 1;
        Set<Annotation> targetsBefore = target.getAnnotations();
        Set<Annotation> targets = new HashSet<Annotation>();

        Annotations annotations = source.getAnnotations();
        if (annotations != null) {
            for (org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Annotation sourceAnnotationDto : annotations.getAnnotations()) {
                String code = generateCodeForAnnotation(annotationCount); // Generate sequential CODE for Annotation
                annotationCount++;

                boolean existsBefore = false;
                for (Annotation targetAnnotation : targetsBefore) {
                    if (targetAnnotation.getId().equals(sourceAnnotationDto.getId())) {
                        Annotation annotationUpdate = annotationRestToEntity(sourceAnnotationDto, targetAnnotation, target);
                        annotationUpdate.setCode(code);
                        targets.add(annotationUpdate); // Update
                        existsBefore = true;
                        break;
                    }
                }
                if (!existsBefore) {
                    Annotation annotationNew = annotationRestToEntity(sourceAnnotationDto, null, target);
                    annotationNew.setCode(code);
                    targets.add(annotationNew);
                }
            }

            target.getAnnotations().clear();
            target.getAnnotations().addAll(targets);

        }
        return target;
    }

    // -------------------------------------------------------------------
    // EXTERNAL ITEM: STATISTICAL RESOURCES
    // -------------------------------------------------------------------
    public ExternalItem resourceInternalRestStatisticalResourceToExternalItemDo(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal source, ExternalItem target)
            throws MetamacException {

        target = externalItemRestToExternalItem(source, target);
        if (target != null) {
            target.setUri(CoreCommonUtil.externalItemUrlDtoToUrlDo(getStatisticalResourceInternalApiUrlBase(), source.getSelfLink().getHref()));
            target.setManagementAppUrl(CoreCommonUtil.externalItemUrlDtoToUrlDo(getStatisticalResourceInternalWebApplicationUrlBase(), source.getManagementAppLink()));
        }
        return target;
    }

    public ExternalItem externalItemRestToExternalItem(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal source, ExternalItem target) throws MetamacException {
        if (source == null) {
            if (target != null) {
                // delete previous entity
                externalItemRepository.delete(target);
            }
            return null;
        }

        if (target == null) {
            // New
            target = new ExternalItem();
        }
        target.setCode(source.getId());
        target.setCodeNested(source.getNestedId());
        target.setUrn(source.getUrn());
        target.setUrnProvider(source.getUrnProvider());
        target.setTitle(internationalStringRestToEntity(source.getName(), target.getTitle(), ServiceExceptionParametersInternal.EXTERNAL_ITEM_TITLE));
        target.setType(TypeExternalArtefactsEnum.fromValue(source.getKind()));

        return target;
    }

    protected String getStatisticalResourceInternalWebApplicationUrlBase() throws MetamacException {
        return configurationService.retrieveStatisticalResourcesInternalWebApplicationUrlBase();
    }

    protected String getStatisticalResourceInternalApiUrlBase() throws MetamacException {
        return configurationService.retrieveStatisticalResourcesInternalApiUrlBase();
    }

    private Annotation annotationRestToEntity(org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Annotation source, Annotation target, AnnotableArtefact annotableArtefactTarget)
            throws MetamacException {

        if (source == null) {
            if (target != null) {
                // Delete old entity
                annotationRepository.delete(target);
            }
            return null;
        }

        if (target == null) {
            target = new Annotation();
        }

        // Metadata modifiable
        target.setCode(source.getId());
        target.setTitle(source.getTitle());
        target.setType(source.getType());
        target.setUrl(source.getUrl());
        target.setAnnotableArtefact(annotableArtefactTarget);
        // Related entities
        target.setText(internationalStringRestToEntity(source.getText(), target.getText(), ServiceExceptionParameters.ANNOTATION_TEXT));

        return target;
    }

    protected InternationalString internationalStringRestToEntity(org.siemac.metamac.rest.common.v1_0.domain.InternationalString source, InternationalString target, String metadataName)
            throws MetamacException {

        // Skip html
        internationalStringHtmlToTextPlain(source);

        // TODO Check it is valid

        // Transform
        if (source == null) {
            if (target != null) {
                // Delete old entity
                internationalStringRepository.delete(target);
            }
            return null;
        }

        if (ValidationUtils.isEmpty(source)) {
            // international string is not complete
            throw new MetamacException(ServiceExceptionType.METADATA_REQUIRED, metadataName);
        }

        if (target == null) {
            target = new InternationalString();
        }
        Set<LocalisedString> localisedStringEntities = localisedStringRestToEntity(source.getTexts(), target.getTexts(), target);
        target.getTexts().clear();
        target.getTexts().addAll(localisedStringEntities);
        return target;
    }

    private Set<LocalisedString> localisedStringRestToEntity(List<org.siemac.metamac.rest.common.v1_0.domain.LocalisedString> sources, Set<LocalisedString> targets,
            InternationalString internationalStringTarget) {

        Set<LocalisedString> targetsBefore = targets;
        targets = new HashSet<LocalisedString>();

        for (org.siemac.metamac.rest.common.v1_0.domain.LocalisedString source : sources) {
            boolean existsBefore = false;
            for (LocalisedString target : targetsBefore) {
                if (source.getLang().equals(target.getLocale())) {
                    targets.add(localisedStringRestToEntity(source, target, internationalStringTarget));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                targets.add(localisedStringRestToEntity(source, internationalStringTarget));
            }
        }
        return targets;
    }

    private LocalisedString localisedStringRestToEntity(org.siemac.metamac.rest.common.v1_0.domain.LocalisedString source, InternationalString internationalStringTarget) {
        LocalisedString target = new LocalisedString();
        localisedStringRestToEntity(source, target, internationalStringTarget);
        return target;
    }

    private LocalisedString localisedStringRestToEntity(org.siemac.metamac.rest.common.v1_0.domain.LocalisedString source, LocalisedString target, InternationalString internationalStringTarget) {
        target.setLabel(source.getValue());
        target.setLocale(source.getLang());
        // target.setIsUnmodifiable(source.getIsUnmodifiable());
        target.setInternationalString(internationalStringTarget);
        return target;
    }

    private String generateCodeForAnnotation(int annotationCount) {
        return SdmxConstants.GENERATOR_ANNOTATION_CODE_PREFIX + "_" + StringUtils.leftPad(String.valueOf(annotationCount), 2, '0');
    }

    // --------------------------------------------------------------------------------
    // COMMON
    // --------------------------------------------------------------------------------
    private void internationalStringHtmlToTextPlain(org.siemac.metamac.rest.common.v1_0.domain.InternationalString source) {
        if (source == null) {
            return;
        }
        for (org.siemac.metamac.rest.common.v1_0.domain.LocalisedString localisedString : source.getTexts()) {
            String labelSource = localisedString.getValue();
            if (labelSource != null) {
                String labelTarget = Jsoup.clean(labelSource, whitelist);
                labelTarget = StringEscapeUtils.unescapeHtml(labelTarget);
                localisedString.setValue(labelTarget);
            }
        }
    }
}