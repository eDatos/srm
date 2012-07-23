package org.siemac.metamac.srm.core.service.dto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dozer.MappingException;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.ExternalItemRepository;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.ent.exception.ExternalItemNotFoundException;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.domain.srm.dto.AnnotableArtefactDto;
import org.siemac.metamac.domain.srm.dto.AnnotationDto;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.dto.IdentifiableArtefactDto;
import org.siemac.metamac.domain.srm.dto.MaintainableArtefactDto;
import org.siemac.metamac.domain.srm.dto.NameableArtefactDto;
import org.siemac.metamac.domain.srm.dto.RelationshipDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.domain.srm.enume.domain.TypeDataAttribute;
import org.siemac.metamac.srm.core.base.domain.AnnotableArtefact;
import org.siemac.metamac.srm.core.base.domain.Annotation;
import org.siemac.metamac.srm.core.base.domain.AnnotationRepository;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.domain.ComponentListRepository;
import org.siemac.metamac.srm.core.base.domain.ComponentRepository;
import org.siemac.metamac.srm.core.base.domain.EnumeratedRepresentation;
import org.siemac.metamac.srm.core.base.domain.Facet;
import org.siemac.metamac.srm.core.base.domain.FacetRepository;
import org.siemac.metamac.srm.core.base.domain.IdentifiableArtefact;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.base.domain.NameableArtefact;
import org.siemac.metamac.srm.core.base.domain.Representation;
import org.siemac.metamac.srm.core.base.domain.RepresentationRepository;
import org.siemac.metamac.srm.core.base.domain.TextFormatRepresentation;
import org.siemac.metamac.srm.core.base.exception.AnnotationNotFoundException;
import org.siemac.metamac.srm.core.base.exception.ComponentListNotFoundException;
import org.siemac.metamac.srm.core.base.exception.ComponentNotFoundException;
import org.siemac.metamac.srm.core.base.exception.FacetNotFoundException;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParametersInternal;
import org.siemac.metamac.srm.core.service.utils.SdmxToolsServer;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.AttributeRelationship;
import org.siemac.metamac.srm.core.structure.domain.AttributeRelationshipRepository;
import org.siemac.metamac.srm.core.structure.domain.DataAttribute;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionRepository;
import org.siemac.metamac.srm.core.structure.domain.Dimension;
import org.siemac.metamac.srm.core.structure.domain.DimensionComponent;
import org.siemac.metamac.srm.core.structure.domain.DimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.DimensionRelationship;
import org.siemac.metamac.srm.core.structure.domain.GroupDimensionDescriptor;
import org.siemac.metamac.srm.core.structure.domain.GroupRelationship;
import org.siemac.metamac.srm.core.structure.domain.MeasureDescriptor;
import org.siemac.metamac.srm.core.structure.domain.MeasureDimension;
import org.siemac.metamac.srm.core.structure.domain.NoSpecifiedRelationship;
import org.siemac.metamac.srm.core.structure.domain.PrimaryMeasure;
import org.siemac.metamac.srm.core.structure.domain.PrimaryMeasureRelationship;
import org.siemac.metamac.srm.core.structure.domain.ReportingYearStartDay;
import org.siemac.metamac.srm.core.structure.domain.TimeDimension;
import org.siemac.metamac.srm.core.structure.exception.DataStructureDefinitionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class Dto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    private ComponentRepository               componentRepository;

    @Autowired
    private ComponentListRepository           componentListRepository;

    @Autowired
    private DataStructureDefinitionRepository dataStructureDefinitionRepository;

    @Autowired
    private AnnotationRepository              annotationRepository;

    @Autowired
    private InternationalStringRepository     internationalStringRepository;

    @Autowired
    private AttributeRelationshipRepository   attributeRelationshipRepository;

    @Autowired
    private RepresentationRepository          representationRepository;

    @Autowired
    private ExternalItemRepository            externalItemRepository;

    @Autowired
    private FacetRepository                   facetRepository;

    /**************************************************************************
     * GETTERS
     **************************************************************************/
    protected ComponentListRepository getComponentListRepository() {
        return componentListRepository;
    }
    
    protected ComponentRepository getComponentRepository() {
        return componentRepository;
    }

    protected DataStructureDefinitionRepository getDataStructureDefinitionRepository() {
        return dataStructureDefinitionRepository;
    }

    public AnnotationRepository getAnnotationRepository() {
        return annotationRepository;
    }

    protected InternationalStringRepository getInternationalStringRepository() {
        return internationalStringRepository;
    }

    protected AttributeRelationshipRepository getAttributeRelationshipRepository() {
        return attributeRelationshipRepository;
    }

    public RepresentationRepository getRepresentationRepository() {
        return representationRepository;
    }

    public ExternalItemRepository getExternalItemRepository() {
        return externalItemRepository;
    }

    public FacetRepository getFacetRepository() {
        return facetRepository;
    }

    /**************************************************************************
     * METHODS
     **************************************************************************/

    /**
     * @param source Dto to transform
     * @param older Current Entity for this Dto, null if is new
     * @param metadataName Parameter name's on the internationalString relationship
     * @return
     */
    private InternationalString internationalStringToEntity(ServiceContext ctx, InternationalStringDto source, InternationalString older, String metadataName) throws MetamacException {
        if (source == null) {
            // Delete old entity
            if (older != null) {
                getInternationalStringRepository().delete(older);
            }
            return null;
        }

        if (older == null) {
            older = new InternationalString();
        }

        // Validate InternationalString and Localiseds
        if (ValidationUtils.isEmpty(source)) {
            throw new MetamacException(MetamacCoreExceptionType.METADATA_REQUIRED, metadataName);
        }

        // Create a MAP with all source locales
        Map<String, LocalisedStringDto> sourceTextMap = new HashMap<String, LocalisedStringDto>();
        for (LocalisedStringDto sourceLocalisedDto : source.getTexts()) {
            sourceTextMap.put(sourceLocalisedDto.getLocale(), sourceLocalisedDto);
        }

        for (LocalisedString targetLocalised : older.getTexts()) {
            // If a locale update?
            if (sourceTextMap.containsKey(targetLocalised.getLocale())) {
                targetLocalised.setLabel(sourceTextMap.get(targetLocalised.getLocale()).getLabel()); // update label
                sourceTextMap.remove(targetLocalised.getLocale());
            } else {
                // Remove this locale
                older.removeText(targetLocalised);
            }
        }

        // New locales
        Iterator<Entry<String, LocalisedStringDto>> it = sourceTextMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, LocalisedStringDto> newEntry = it.next();

            LocalisedString newLocale = new LocalisedString();
            newLocale.setLabel(newEntry.getValue().getLabel());
            newLocale.setLocale(newEntry.getKey());

            older.addText(newLocale);
        }

        return older;
    }

    /**
     * @param ctx
     * @param source
     * @return
     * @throws MetamacException
     */
    private Annotation annotationDtoToEntity(ServiceContext ctx, AnnotationDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // TODO pasarle el target desde los métodos que llaman a este y no, buscar la anotación desde el repositorio.
        Annotation target = null;
        if (source.getId() == null) {
            // Create
            target = new Annotation();
        } else {
            // Update
            try {
                target = getAnnotationRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
            } catch (AnnotationNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.ANNOTATION).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        // Metadata modifiable
        target.setIdLogic(source.getIdLogic());
        target.setTitle(source.getTitle());
        target.setType(source.getType());
        target.setUrl(source.getUrl());

        // Related entities
        target.setText(internationalStringToEntity(ctx, source.getText(), (target != null) ? target.getText() : null, ServiceExceptionParameters.ANNOTATION_TEXT));

        return target;
    }

    /**
     * @param <T>
     * @param <U>
     * @param ctx
     * @param source
     * @param target
     * @return
     * @throws MetamacException
     */
    private <T extends AnnotableArtefactDto, U extends AnnotableArtefact> U annotableToEntity(ServiceContext ctx, T source, U target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Required target entity because this class is abstract
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.ANNOTABLEARTEFACT).build();
        }

        // Related entities
        for (AnnotationDto annotationDto : source.getAnnotations()) {
            target.getAnnotations().add(annotationDtoToEntity(ctx, annotationDto));
        }

        return target;
    }

    private <T extends IdentifiableArtefactDto, U extends IdentifiableArtefact> U identifiableArtefactDtoToEntity(ServiceContext ctx, T source, U target) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Required target entity because this class is abstract
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLEARTEFACT).build();
        }

        // Metadata modifiable
        // IdLogic: Some artifacts has a fixed ID -> Overwrite with fixed ID if is possible
        String fixedID = null;
        if ((fixedID = SdmxToolsServer.checkIfFixedId(target)) == null) {
            // Generate a ID if is empty
            if (StringUtils.isEmpty(((IdentifiableArtefact) target).getIdLogic())) {
                ((IdentifiableArtefact) target).setIdLogic(SdmxToolsServer.generateIdForSDMXArtefact(target));
            }
        } else {
            ((IdentifiableArtefact) target).setIdLogic(fixedID);
        }

        // TODO URI, URN and ReplaceBy filled in service
        // target.setUri(source.getUri());
        // target.setUrn(urn);
        // TODO Sustituir por version a la que reemplaza. En este momento??? --> target.setReplacedBy(target.getReplacedBy());

        return annotableToEntity(ctx, source, target);
    }

    /**
     * @param <T>
     * @param <U>
     * @param ctx
     * @param source
     * @param target
     * @param older
     * @return
     * @throws MappingException
     * @throws MetamacException
     */
    private <T extends NameableArtefactDto, U extends NameableArtefact> U nameableToEntity(ServiceContext ctx, T source, U target, U older) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Required target entity because this class is abstract
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.NAMEABLEARTEFACT).build();
        }

        // Related entities
        // Name
        target.setName(internationalStringToEntity(ctx, source.getName(), (older != null) ? older.getName() : null, ServiceExceptionParameters.NAMEABLEARTEFACT_NAME));

        // Description
        target.setDescription(internationalStringToEntity(ctx, source.getDescription(), (older != null) ? older.getDescription() : null, ServiceExceptionParameters.NAMEABLEARTEFACT_DESCRIPTION));

        return identifiableArtefactDtoToEntity(ctx, source, target);
    }

    /**
     * @param <T>
     * @param <U>
     * @param ctx
     * @param source
     * @param target
     * @param older
     * @return
     * @throws MetamacException
     */
    private <T extends MaintainableArtefactDto, U extends MaintainableArtefact> U maintainableArtefactToEntity(ServiceContext ctx, T source, U target, U older) throws MetamacException {
        if (source == null) {
            return null;
        }

        // Required target entity because this class is abstract
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.MANTAINER).build();
        }

        // Metadata modifiable
        target.setVersionLogic(source.getVersionLogic());
        target.setValidFrom(CoreCommonUtil.transformDateToDateTime(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateToDateTime(source.getValidTo()));
        target.setFinalLogic(source.getFinalLogic());
        target.setIsExternalReference(source.getIsExternalReference());
        target.setStructureURL(source.getStructureURL());
        target.setServiceURL(source.getServiceURL());

        // Related entities
        target.setMaintainer(externalItemDtoToExternalItem(ctx, source.getMaintainer(), ServiceExceptionParameters.MANTAINER_TITLE));

        return nameableToEntity(ctx, source, target, older);
    }

    /**************************************************************************
     * COMPONENTS
     **************************************************************************/

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T componentDtoToComponent(ServiceContext ctx, ComponentDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        if (source.getTypeComponent() == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(CommonServiceExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.COMPONENT_TYPE)
                    .withLoggedLevel(ExceptionLevelEnum.DEBUG).build();
        }

        // Hierarchy:
        //
        // AnnotableArtefact > IdentifiableArtefact > Component
        // -|_ DataAttribute
        // --|_ ReportingYearStartDay
        // |_ DimensionComponent
        // --|_ Dimension
        // --|_ MeasureDimension
        // --|_ TimeDimension
        // |_ PrimaryMeasure
        //
        // IdentityDto > AuditableDto > AnnotableArtefactDto > IdentifiableArtefactDto > ComponentDto
        // |_ DataAttributeDto
        // |_ DimensionComponentDto

        T result = null;
        // DataAttribute ******************************************************
        if (source.getTypeComponent().equals(TypeComponent.DATA_ATTRIBUTE)) {
            result = (T)dataAttributeDtoToDataAttribute(ctx, (DataAttributeDto) source);
        }
        // DimensionComponent ***************************************************
        else if (source.getTypeComponent().equals(TypeComponent.DIMENSION_COMPONENT)) {
            result = (T)dimensionComponentDtoToDimensionComponent(ctx, (DimensionComponentDto) source);
        }
        // Primary Measure ***************************************************
        else if (source.getTypeComponent().equals(TypeComponent.PRIMARY_MEASURE)) {
            result = (T)componentDtoToPrimaryMeasure(ctx, source);
        } else {
            // The TargetObject may be enumerated and, if so, can use any ItemScheme
            // 785 (Codelist, ConceptScheme, OrganisationScheme, CategoryScheme,
            // 786 ReportingTaxonomy)
            // The MetadataAttribute may be non-enumerated and, if so, uses one or more
            // 792 ExtendedFacet
            // The MetadataAttribute may be enumerated and, if so, use a
            // 783 Codelist
            throw new UnsupportedOperationException("componentDtoToComponent for Unknown Entity not implemented");
        }

        return identifiableArtefactDtoToEntity(ctx, source, result);
    }

    private <T extends DataAttribute> T dataAttributeDtoToDataAttribute(ServiceContext ctx, DataAttributeDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // REPORTING_YEAR_START_DAY or DATA_ATTRIBUTE?
        if (source.getTypeDataAttribute() == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.COMPONENT_TYPE_DATA_ATTRIBUTE)
                    .withLoggedLevel(ExceptionLevelEnum.DEBUG).build();
        }

        T result = null;
        if (source.getId() == null) {
            switch (source.getTypeDataAttribute()) {
                case REPORTING_YEAR_START_DAY:
                    // DTO validation: ReportingYearStarDay not contains role relationship
                    if (!((DataAttributeDto) source).getRole().isEmpty()) {
                        throw MetamacExceptionBuilder.builder().withExceptionItems(MetamacCoreExceptionType.PARAMETER_REQUIRED).withMessageParameters(ServiceExceptionParameters.DATAATTRIBUTE_ROLE)
                                .withLoggedLevel(ExceptionLevelEnum.DEBUG).build();
                    }

                    if (source.getId() == null) {
                        result = (T) new ReportingYearStartDay();
                    }
                    break;
                case DATA_ATTRIBUTE:
                    DataAttribute dataAttribute = null;
                    if (source.getId() == null) {
                        result = (T) new DataAttribute();
                    }
                    break;
                default:
            }
        } else {
            // Update: Find previous entity
            try {
                result = (T) getComponentRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (ComponentNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.DATAATTRIBUTE).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        // Metadata modifiable
        ((DataAttribute) result).setUsageStatus(((DataAttributeDto) source).getUsageStatus());

        // Related entities
        // Relate To
        ((DataAttribute) result).setRelateTo(attributeRelationshipDtoToAttributeRelationship(ctx, source.getRelateTo(), ((DataAttribute) result).getRelateTo()));
        
        if (TypeDataAttribute.DATA_ATTRIBUTE.equals(source.getTypeDataAttribute())) {
            // Concept identity
            result.setCptIdRef(externalItemDtoToExternalItem(ctx, source.getCptIdRef(), "DataAttributeConceptIdentity")); // TODO args exp
        }

        // Role
        if (source.getId() != null) { // Update
            // Merge results
            for (ExternalItemDto listExternalItemDto : ((DataAttributeDto) source).getRole()) {
                ExternalItem externalItem = externalItemDtoToExternalItem(ctx, listExternalItemDto, ServiceExceptionParameters.DATAATTRIBUTE_ROLE);
                boolean found = false;
                for (ExternalItem extItemPersisted : ((DataAttribute) result).getRole()) {
                    if (extItemPersisted.equals(externalItem)) {
                        ((DataAttribute) result).addRole(extItemPersisted);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ((DataAttribute) result).addRole(externalItem);
                }

            }
        } else { // New
            for (ExternalItemDto listExternalItemDto : ((DataAttributeDto) source).getRole()) {
                ((DataAttribute) result).addRole(externalItemDtoToExternalItem(ctx, listExternalItemDto, ServiceExceptionParameters.DATAATTRIBUTE_ROLE));
            }
        }

        // LocalRepresentation
        result.setLocalRepresentation(representationDtoToRepresentation(ctx, source.getLocalRepresentation(), ((DataAttribute) result).getLocalRepresentation(),
                ServiceExceptionParameters.DATAATTRIBUTE + ServiceExceptionParametersInternal.REPRESENTATION));

        return result;
    }

    private <T extends Component> T dimensionComponentDtoToDimensionComponent(ServiceContext ctx, DimensionComponentDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        T result = null;

        if (((DimensionComponentDto) source).getTypeDimensionComponent() == null) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, "TypeDimensionComponent");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }

        switch (((DimensionComponentDto) source).getTypeDimensionComponent()) {
            case DIMENSION:
                result = (T) dimensionComponentDtoToDimension(ctx, source);
                break;
            case MEASUREDIMENSION:
                result = (T) dimensionComponentDtoToMeasureDimension(ctx, source);
                break;
            case TIMEDIMENSION:
                result = (T) dimensionComponentDtoToTimeDimension(ctx, source);
                break;
            default:
                // DimensionComponent is a abstract class, cannot be instantiated
                throw new UnsupportedOperationException("componentDtoToComponent::dimensionComponentDtoToDimensionComponent for Unknown Entity not implemented");
        }

        return result;
    }

    private <T extends Dimension> T dimensionComponentDtoToDimension(ServiceContext ctx, DimensionComponentDto source) throws MetamacException {

        if (source == null) {
            return null;
        }

        T result = null;
        if (source.getId() == null) {
            // New
            result = (T) new Dimension();
        } else {
            // Update
            try {
                result = (T) getComponentRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (ComponentNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.DIMENSION)
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }

        }

        // Related entities
        // ROLE
        if (source.getId() != null) { // Update
            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) source).getRole()) {
                ExternalItem externalItem = externalItemDtoToExternalItem(ctx, listExternalItemDto, ServiceExceptionParameters.DIMENSION_ROLE);
                boolean found = false;
                for (ExternalItem extItemPersisted : result.getRole()) {
                    if (extItemPersisted.equals(externalItem)) {
                        ((Dimension) result).addRole(extItemPersisted);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ((Dimension) result).addRole(externalItem);
                }

            }
        } else { // New
            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) source).getRole()) {
                ((Dimension) result).addRole(externalItemDtoToExternalItem(ctx, listExternalItemDto, ServiceExceptionParameters.DIMENSION_ROLE));
            }
        }

        // LocalRepresentation
        result.setLocalRepresentation(representationDtoToRepresentation(ctx, source.getLocalRepresentation(), result.getLocalRepresentation(),
                ServiceExceptionParameters.DIMENSION + ServiceExceptionParametersInternal.REPRESENTATION));
        
        // Concept identity
        result.setCptIdRef(externalItemDtoToExternalItem(ctx, source.getCptIdRef(), "DimensionConceptIdentity")); // TODO args exp

        return result;
    }

    private <T extends MeasureDimension> T dimensionComponentDtoToMeasureDimension(ServiceContext ctx, DimensionComponentDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        T result = null;
        if (source.getId() == null) {
            // New
            result = (T) new MeasureDimension();
        } else {
            // Update
            try {
                result = (T) getComponentRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (ComponentNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.MEASUREDIMENSION).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        // ROLE
        if (source.getId() != null) { // Exist
            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) source).getRole()) {
                ExternalItem externalItem = externalItemDtoToExternalItem(ctx, listExternalItemDto, ServiceExceptionParameters.MEASUREDIMENSION_ROLE);
                boolean found = false;
                for (ExternalItem extItemPersisted : result.getRole()) {
                    if (extItemPersisted.equals(externalItem)) {
                        ((MeasureDimension) result).addRole(extItemPersisted);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ((MeasureDimension) result).addRole(externalItem);
                }

            }
        } else { // New
            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) source).getRole()) {
                ((MeasureDimension) result).addRole(externalItemDtoToExternalItem(ctx, listExternalItemDto, ServiceExceptionParameters.MEASUREDIMENSION_ROLE));
            }
        }

        // LocalRepresentation
        result.setLocalRepresentation(representationDtoToRepresentation(ctx, source.getLocalRepresentation(), result.getLocalRepresentation(),
                ServiceExceptionParameters.MEASUREDIMENSION + ServiceExceptionParametersInternal.REPRESENTATION));

        // Concept identity
        result.setCptIdRef(externalItemDtoToExternalItem(ctx, source.getCptIdRef(), "MeasureDimensionConceptIdentity")); // TODO args exp
        
        return result;
    }

    private <T extends TimeDimension> T dimensionComponentDtoToTimeDimension(ServiceContext ctx, DimensionComponentDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        T result = null;
        if (source.getId() == null) {
            // New
            result = (T) new TimeDimension();
        } else {
            // Update
            try {
                result = (T) getComponentRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (ComponentNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.TIMEDIMENSION).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        // LocalRepresentation
        result.setLocalRepresentation(representationDtoToRepresentation(ctx, source.getLocalRepresentation(), result.getLocalRepresentation(),
                ServiceExceptionParameters.TIMEDIMENSION + ServiceExceptionParametersInternal.REPRESENTATION));

        return result;
    }

    private <T extends Component> T componentDtoToPrimaryMeasure(ServiceContext ctx, ComponentDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        T result = null;
        if (source.getId() == null) {
            // New
            result = (T) new PrimaryMeasure();
        } else {
            // Update
            try {
                result = (T) getComponentRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (ComponentNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.PRIMARYMEASURE).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        // LocalRepresentation
        result.setLocalRepresentation(representationDtoToRepresentation(ctx, source.getLocalRepresentation(), result.getLocalRepresentation(),
                ServiceExceptionParameters.COMPONENT + ServiceExceptionParametersInternal.REPRESENTATION));
        
        // Concept identity
        result.setCptIdRef(externalItemDtoToExternalItem(ctx, source.getCptIdRef(), "PrimaryMeasureConceptIdentity")); // TODO args exp

        return result;
    }

    /**************************************************************************
     * COMPONENT_LISTS
     **************************************************************************/
    @Override
    public <T extends ComponentList> T componentListDtoToComponentList(ServiceContext ctx, ComponentListDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        // Hierachy:
        // AnnotableArtefact < IdentifiableArtefact < ComponentList
        // |_ AttributeDescriptor
        // |_ DimensionDescriptor
        // |_ GroupDimensionDescriptor
        // |_ MeasureDescriptor
        // |_ MetadataTarget
        // |_ ReportStructure
        // DimensionDescriptorDto > ComponentListDto > IdentifiableArtefactDto > AnnotableArtefactDto > AuditablteDto > IdentityDto

        T result = null;

        if (source.getId() == null) {
            // New
            switch (source.getTypeComponentList()) {
                case ATTRIBUTE_DESCRIPTOR:
                    result = (T) new AttributeDescriptor();
                    break;
                case GROUP_DIMENSION_DESCRIPTOR:
                    result = (T) new GroupDimensionDescriptor();
                    // TODO Metamac not support AttachmenConstraint ((GroupDimensionDescriptor)result).setIsAttachmentConstraint(source.get)
                    break;
                case DIMENSION_DESCRIPTOR:
                    result = (T) new DimensionDescriptor();
                    break;
                case MEASURE_DESCRIPTOR:
                    result = (T) new MeasureDescriptor();
                    break;
                default:
                    // ComponentList is a abstract class, cannot be instantiated
                    throw new UnsupportedOperationException("componentListDtoToComponentList for Unknown Entity not implemented");
            }
        }
        else {
            // Update
            try {
                result = (T) getComponentListRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (ComponentListNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.COMPONENT_LIST)
                .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        // Related entities
        // Components
        for (ComponentDto componentDto : source.getComponents()) {
            result.addComponent(componentDtoToComponent(ctx, componentDto));
        }

        return identifiableArtefactDtoToEntity(ctx, source, result);
    }

    private AttributeRelationship attributeRelationshipDtoToAttributeRelationship(ServiceContext ctx, RelationshipDto source, AttributeRelationship attributeRelationshipOlder) throws MetamacException {
        if (source == null) {
            // Delete old entity
            if (attributeRelationshipOlder != null) {
                getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
            }
            return null;
        }

        // Hierachy:
        // AttributeRelationship
        // |_ NoSpecifiedRelationship
        // |_ PrimaryMeasureRelationship
        // |_ GroupRelationship
        // |_ DimensionRelationship
        // RelationshipDto > IdentityDto
        AttributeRelationship result = null;
        switch (source.getTypeRelathionship()) {
            case DIMENSION_RELATIONSHIP:
                result = attributeRelationshipDtoToDimensionRelationship(ctx, source, attributeRelationshipOlder);
                break;
            case GROUP_RELATIONSHIP:
                result = attributeRelationshipDtoToGroupRelationship(ctx, source, attributeRelationshipOlder);
                break;
            case NO_SPECIFIED_RELATIONSHIP:
                result = attributeRelationshipDtoToNoSpecifiedRelationship(ctx, source, attributeRelationshipOlder);
                break;
            case PRIMARY_MEASURE_RELATIONSHIP:
                result = attributeRelationshipDtoToPrimaryMeasureRelationship(ctx, source, attributeRelationshipOlder);
                break;
        }

        return result;
    }

    private DimensionRelationship attributeRelationshipDtoToDimensionRelationship(ServiceContext ctx, RelationshipDto source, AttributeRelationship attributeRelationshipOlder) throws MetamacException {

        if (source == null) {
            return null;
        }

        DimensionRelationship result = null;

        // Previous version?
        if (attributeRelationshipOlder != null) {
            // if source is different than previous
            if (!(attributeRelationshipOlder instanceof DimensionRelationship) || (source.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
            }
            else {
                // Update
                result = (DimensionRelationship)attributeRelationshipOlder;
            }
        }
        
        // New
        if (result == null) {
            result = new DimensionRelationship();
        }

        // DimensionRelationship: GroupKey
        for (DescriptorDto descriptorDto : source.getGroupKeyForDimensionRelationship()) {
            if (descriptorDto.getTypeComponentList().equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                result.addGroupKey((GroupDimensionDescriptor) componentListDtoToComponentList(ctx, descriptorDto));
            } else {
                MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "TypeComponentList");
                metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                throw metamacException;
            }
        }

        // DimensionRelationship: Dimension
        if (source.getDimensionForDimensionRelationship().isEmpty()) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MIN, "dimensionForDimensionRelationship");
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }
        for (DimensionComponentDto dimensionComponentDto : source.getDimensionForDimensionRelationship()) {
            result.addDimension((DimensionComponent) componentDtoToComponent(ctx, dimensionComponentDto));
        }

        return result;
    }

    private GroupRelationship attributeRelationshipDtoToGroupRelationship(ServiceContext ctx, RelationshipDto source, AttributeRelationship attributeRelationshipOlder) throws MetamacException {

        if (source == null) {
            return null;
        }

        GroupRelationship result = null;
        
        // Previous version?
        if (attributeRelationshipOlder != null) {
            // if source is different than previous
            if (!(attributeRelationshipOlder instanceof GroupRelationship) || (source.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
            }
            else {
                // Update
                result = (GroupRelationship)attributeRelationshipOlder;
            }
        }
        
        // New
        if (result == null) {
            result = new GroupRelationship();
        }

        // Group
        result.setGroupKey((GroupDimensionDescriptor) componentListDtoToComponentList(ctx, source.getGroupKeyForGroupRelationship()));

        return result;
    }

    private NoSpecifiedRelationship attributeRelationshipDtoToNoSpecifiedRelationship(ServiceContext ctx, RelationshipDto source, AttributeRelationship attributeRelationshipOlder)
            throws MetamacException {

        if (source == null) {
            return null;
        }

        NoSpecifiedRelationship result = null;
        
        // Previous version?
        if (attributeRelationshipOlder != null) {
            // if source is different than previous
            if (!(attributeRelationshipOlder instanceof NoSpecifiedRelationship) || (source.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
            }
            else {
                // Update
                result = (NoSpecifiedRelationship)attributeRelationshipOlder;
            }
        }
        
        // New
        if (result == null) {
            result = new NoSpecifiedRelationship();
        }

        return result;
    }

    private PrimaryMeasureRelationship attributeRelationshipDtoToPrimaryMeasureRelationship(ServiceContext ctx, RelationshipDto source, AttributeRelationship attributeRelationshipOlder)
            throws MetamacException {

        if (source == null) {
            return null;
        }

        PrimaryMeasureRelationship result = null;
        
        // Remove old data
        if (attributeRelationshipOlder != null) {
            // if source is different than previous
            if (!(attributeRelationshipOlder instanceof PrimaryMeasureRelationship) || (source.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
            }
            else {
                // Update
                result = (PrimaryMeasureRelationship)attributeRelationshipOlder;
            }
        }
        
        // New
        if (result == null) {
            result = new PrimaryMeasureRelationship();
        }

        return result;
    }

    /**************************************************************************
     * DATASTRUCTUREDEFINITION
     **************************************************************************/
    @Override
    public DataStructureDefinition dataStructureDefinitionDtoToDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        
        // Hierachy:
        // DataStructureDefinitionDto > MaintainableArtefactDto > NameableArtefactDto > IdentifiableArtefactDto > AnnotableArtefacDto > AuditableDto > IdentityDto
        // DataStructureDefinition > Structure > MaintainableArtefact > NameableArtefact > IdentifiableArtefact > AnnotableArtefact

        DataStructureDefinition result = null;
        if (source.getId() == null) {
            // New
            result = new DataStructureDefinition();
        } else {
            // Update
            try {
                result = getDataStructureDefinitionRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (DataStructureDefinitionNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.DATASTRUCTUREDEFINITION).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }

            // Withouts DTO fields: NO MERGE NEEDED!!!
            // |_ Grouping
            result.getGrouping().addAll(result.getGrouping());
            // |_ Annotations
        }

        // Parent
        return maintainableArtefactToEntity(ctx, source, result, result);
    }

    private Representation representationDtoToRepresentation(ServiceContext ctx, RepresentationDto source, Representation representationOlder, String metadataEnumTitle)
            throws MetamacException {
        if (source == null) {
            // Delete old entity
            if (representationOlder != null) {
                getRepresentationRepository().delete(representationOlder);
            }
            return null;
        }

        // Hierachy:
        // RepresentationDto --> AuditableDto -> IdentityDTo
        // Representation
        // | EnumeratedRepresentation
        // | TextFormatRepresentation

        Representation representation = null;

        switch (source.getTypeRepresentationEnum()) {
            case ENUMERATED:
                representation = representationDtoToEnumeratedRepresentation(ctx, source, representationOlder, metadataEnumTitle);
                break;
            case TEXT_FORMAT:
                representation = representationDtoToTextFormatRepresentation(ctx, source, representationOlder);
                break;
            default:
                break;
        }

        return representation;
    }

    private EnumeratedRepresentation representationDtoToEnumeratedRepresentation(ServiceContext ctx, RepresentationDto source, Representation representationOlder, String metadataEnumTitle)
            throws MetamacException {
        if (source == null) {
            return null;
        }
        
        EnumeratedRepresentation result = null;

        // Previous version?
        if (representationOlder != null) {
            // if source is different than previous
            if (!(representationOlder instanceof EnumeratedRepresentation) || (representationOlder.getId().compareTo(source.getId()) != 0)) {
                getRepresentationRepository().delete(representationOlder); // Remove old data
            }
            else {
                // Update
                result = (EnumeratedRepresentation)representationOlder;
            }
        }
        
        // New
        if (result == null) {
            result = new EnumeratedRepresentation();
        }

        // EnumeratedRepresentation: enumerated
        result.setEnumerated(externalItemDtoToExternalItem(ctx, source.getEnumerated(), metadataEnumTitle));

        return result;
    }

    private TextFormatRepresentation representationDtoToTextFormatRepresentation(ServiceContext ctx, RepresentationDto source, Representation representationOlder) throws MetamacException {
        if (source == null) {
            return null;
        }
        
        TextFormatRepresentation result = null;
        
        // Previous version?
        if (representationOlder != null) {
            // if source is different than previous
            if (!(representationOlder instanceof TextFormatRepresentation) || (representationOlder.getId().compareTo(source.getId()) != 0)) {
                getRepresentationRepository().delete(representationOlder);
            }
            else {
                // Update
                result = (TextFormatRepresentation)representationOlder;
            }
        }
        
        // New
        if (result == null) {
            result = new TextFormatRepresentation();
        }

        // Facet
        result.setNonEnumerated(facetDtoToFacet(ctx, source.getNonEnumerated()));
        return result;
    }

    private Facet facetDtoToFacet(ServiceContext ctx, FacetDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        Facet result = null;
        if (source.getId() == null) {
            // New
            result = new Facet(source.getFacetValue());
        } else {
            // Update
            try {
                result = getFacetRepository().findById(source.getId());
                OptimisticLockingUtils.checkVersion(result.getVersion(), source.getVersion());
            } catch (FacetNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.FACET)
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }

        }

        result.setFacetValue(source.getFacetValue());
        result.setIsSequenceFT(source.getIsSequenceFT());
        result.setIntervalFT(source.getInterval());
        result.setStartValueFT(source.getStartValueFT());
        result.setEndValueFT(source.getEndValueFT());
        result.setTimeIntervalFT(source.getTimeIntervalFT());
        result.setStartTimeFT(source.getStartTimeFT());
        result.setEndTimeFT(source.getEndTimeFT());
        result.setMinLengthFT(source.getMinLengthFT());
        result.setMaxLengthFT(source.getMaxLengthFT());
        result.setMinValueFT(source.getMinValueFT());
        result.setMaxValueFT(source.getMaxValueFT());
        result.setDecimalsFT(source.getDecimalsFT());
        result.setPatternFT(source.getPatternFT());
        result.setXhtmlEFT(source.getXhtmlEFT());
        result.setIsMultiLingual(source.getIsMultiLingual());

        return result;
    }

    private ExternalItem externalItemDtoToExternalItem(ServiceContext ctx, ExternalItemDto source, String metadataName) throws MetamacException {
        if (source == null) {
            return null;
        }

        ExternalItem result = null;

        if (source.getId() == null) {
            // New
            result = new ExternalItem(source.getCode(), source.getUri(), source.getUrn(), source.getType());
        } else {
            // Update
            try {
                result = getExternalItemRepository().findById(source.getId());
            } catch (ExternalItemNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND).withMessageParameters(metadataName)
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
        }

        // Relate Entities
        result.setTitle(internationalStringToEntity(ctx, source.getTitle(), result.getTitle(), metadataName + ServiceExceptionParametersInternal.EXTERNALITEM_TITLE));

        return result;
    }

    /*
     * @SuppressWarnings("unchecked")
     * @Override
     * public <T extends Item> T itemDtoToItem(ItemDto itemDto,
     * ServiceContext ctx, BaseService baseService) {
     * if (itemDto == null) {
     * return null;
     * }
     * // Hierachy:
     * // AnnotableArtefact < IdentifiableArtefact < NameableArtefact < Item
     * // |_ Category
     * // |_ Code
     * // |_ Concept
     * // |_ Organisation
     * // |_ Agency
     * // |_ DataConsumer
     * // |_ DataProvider
     * // |_ OrganisationUnit
     * // |_ ReportingCategory
     * // IdentityDto < AuditableDto < AnnotableArtefactDto < NameableArtefactDto < ItemDto
     * // |_ ConceptDto
     * // |_ CategoryDto
     * // |_ CodeDto
     * T result = null;
     * if (itemDto instanceof CategoryDto) {
     * result = (T) mapToEntity(itemDto, Category.class);
     * //***************
     * // FIELDS
     * //***************
     * }
     * else if (itemDto instanceof CodeDto) {
     * result = (T) mapToEntity(itemDto, Code.class);
     * //***************
     * //* FIELDS
     * /****************
     * }
     * else if (itemDto instanceof ConceptDto) {
     * result = (T) mapToEntity(itemDto, Concept.class);
     * //***************
     * //* FIELDS
     * //***************
     * }
     * else {
     * throw new UnsupportedOperationException("itemDtoToItem for Unknonw DTO not implemented");
     * }
     * //****************
     * // * FIELDS
     * // ***************
     * // Set<@ItemDto> hierarchy
     * for (ItemDto hierarchyItemDto : itemDto.getHierarchy()) {
     * result.addHierarchy(itemDtoToItem(hierarchyItemDto, ctx, baseService));
     * }
     * // Parent
     * return nameableToEntity(itemDto, result);
     * }
     */

    /*
     * @SuppressWarnings("unchecked")
     * @Override
     * public <T extends ItemScheme> T itemschemeDtoToItemScheme(
     * ItemSchemeDto itemSchemeDto, ServiceContext ctx,
     * BaseService baseService) throws MappingException, OrganisationNotFoundException {
     * if (itemSchemeDto == null) {
     * return null;
     * }
     * // Hierachy:
     * // AnnotableArtefact < IdentifiableArtefact < NameableArtefact < MaintainableArtefact < ItemScheme
     * // |_ CategoryScheme
     * // |_ CodeList
     * // |_ ConceptScheme
     * // |_ OrganisationScheme
     * // |_ AgencyScheme
     * // |_ DataConsumerScheme
     * // |_ DataProviderScheme
     * // |_ OrganisationUnitScheme
     * // |_ ReportingTaxonomy
     * // IdentityDTo < AuditableDto < AnnotableArtefacDto < IdentifiableArtefactDTO < NameableArtefactDto < MaintainableArtefactDto < ItemSchemeDto
     * // |_ CodeListDto
     * // |_ ConceptSchemeDto
     * T result = null;
     * if (itemSchemeDto instanceof CategorySchemeDto) {
     * throw new UnsupportedOperationException("itemschemeDtoToItemScheme for CategorySchemeDto not implemented");
     * }
     * else
     * if (itemSchemeDto instanceof CodeListDto) {
     * result = (T) mapToEntity(itemSchemeDto, CodeList.class);
     * //****************
     * // FIELDS
     * //****************
     * }
     * else
     * if (itemSchemeDto instanceof ConceptSchemeDto) {
     * result = (T) mapToEntity(itemSchemeDto, ConceptScheme.class);
     * //****************
     * // FIELDS
     * //****************
     * }
     * else if (itemSchemeDto instanceof OrganisationSchemeDto) {
     * }
     * else if (itemSchemeDto instanceof ReportingTaxonomy) {
     * }
     * else {
     * throw new UnsupportedOperationException("itemschemeDtoToItemScheme for Unknonw DTO not implemented");
     * }
     * //***************
     * // FIELDS
     * //***************
     * // ItemScheme: Set<@Item> items
     * for (ItemDto itemDto : itemSchemeDto.getItems()) {
     * result.addItem(itemDtoToItem(itemDto, ctx, baseService));
     * }
     * // Parent
     * return maintainableArtefactToEntity(itemSchemeDto, result, ctx, baseService);
     * }
     */
}
