package org.siemac.metamac.srm.core.service.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.dozer.DozerBeanMapper;
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
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.domain.srm.dto.AnnotableArtefactDto;
import org.siemac.metamac.domain.srm.dto.AnnotationDto;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.dto.MaintainableArtefactDto;
import org.siemac.metamac.domain.srm.dto.NameableArtefactDto;
import org.siemac.metamac.domain.srm.dto.RelationshipDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.srm.core.base.domain.AnnotableArtefact;
import org.siemac.metamac.srm.core.base.domain.Annotation;
import org.siemac.metamac.srm.core.base.domain.AnnotationRepository;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.domain.ComponentListRepository;
import org.siemac.metamac.srm.core.base.domain.ComponentRepository;
import org.siemac.metamac.srm.core.base.domain.EnumeratedRepresentation;
import org.siemac.metamac.srm.core.base.domain.Facet;
import org.siemac.metamac.srm.core.base.domain.IdentifiableArtefact;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.base.domain.NameableArtefact;
import org.siemac.metamac.srm.core.base.domain.Representation;
import org.siemac.metamac.srm.core.base.domain.RepresentationRepository;
import org.siemac.metamac.srm.core.base.domain.TextFormatRepresentation;
import org.siemac.metamac.srm.core.base.exception.AnnotationNotFoundException;
import org.siemac.metamac.srm.core.base.exception.ComponentNotFoundException;
import org.siemac.metamac.srm.core.base.serviceapi.SdmxBaseService;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseInvocationValidator;
import org.siemac.metamac.srm.core.common.error.MetamacCoreExceptionType;
import org.siemac.metamac.srm.core.service.utils.SdmxToolsServer;
import org.siemac.metamac.srm.core.service.utils.ValidationUtil;
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
import org.springframework.beans.factory.annotation.Qualifier;

public class Dto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    @Qualifier("mapperCoreUpdateMode")
    private DozerBeanMapper                   mapperCoreUpdateMode;

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

    /**************************************************************************
     * GETTERS
     **************************************************************************/
    protected ComponentRepository getComponentRepository() {
        return componentRepository;
    }

    protected ComponentListRepository getComponentListRepository() {
        return componentListRepository;
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
    /**************************************************************************
     * PRIVATE
     **************************************************************************/
    private <T> T mapToEntity(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }
        // TODO obtener el objeto de bbdd en vez de que dozer haga un new de la instancia, con esto se consigue que...
        // si tenemos un dto parcial (no con todas los campos de la entidad) sino solo con algunos la actualización sobre bbdd no machaque los campos que pudiera haber.
        // Con esto perderíamos el optimistick loocking, porque el objeto recibido de hibernate tiene la version de la bbdd y aunque se setee a la del dto se lo pasa
        // por los mismmísimos. Entonces ante este problema lo que habría que hacer es un IF que compruebe si la version del dto es != de la version de la entity que
        // se carga de bbdd.
        /*
         * T result = null;
         * if (source instanceof DataStructureDefinitionDto) {
         * DataStructureDefinition def;
         * try {
         * def = getDataStructureDefinitionRepository().findById(Long.valueOf("159"));
         * mapperCoreUpdateMode.map(source, def);
         * result = (T) def;
         * } catch (NumberFormatException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * } catch (DataStructureDefinitionNotFoundException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         * }
         * else if (source instanceof InternationalStringDto) {
         * InternationalString def;
         * try {
         * def = getInternationalStringRepository().findById(((InternationalStringDto)source).getId());
         * mapperCoreUpdateMode.map(source, def);
         * result = (T) def;
         * } catch (NumberFormatException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * } catch (InternationalStringNotFoundException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         * }
         * else {
         * result = mapperCoreUpdateMode.map(source, destinationClass);
         * }
         */

        T result = mapperCoreUpdateMode.map(source, destinationClass);

        if (result instanceof IdentifiableArtefact) {
            // IdLogic
            // Some artifacts has a fixed ID -> Overwrite with fixed ID if is possible
            String fixedID = null;
            if ((fixedID = SdmxToolsServer.checkIfFixedId(result)) == null) {
                // Generate a ID if is empty
                if (StringUtils.isEmpty(((IdentifiableArtefact) result).getIdLogic())) {
                    ((IdentifiableArtefact) result).setIdLogic(SdmxToolsServer.generateIdForSDMXArtefact(result));
                }
            } else {
                ((IdentifiableArtefact) result).setIdLogic(fixedID);
            }

            // URI
            if (StringUtils.isEmpty(((IdentifiableArtefact) result).getUri())) {
                ((IdentifiableArtefact) result).setUri(SdmxToolsServer.generateUri());
            }

            // URN
            if (StringUtils.isEmpty(((IdentifiableArtefact) result).getUrn())) {
                ((IdentifiableArtefact) result).setUrn(SdmxToolsServer.generateInternalUrn(result));
            }
        } else if (result instanceof Annotation) {
            // IdLogic
            // Some artifacts has a fixed ID -> Overwrite with fixed ID if is possible
            String fixedID = null;
            if ((fixedID = SdmxToolsServer.checkIfFixedId(result)) == null) {
                // Generate a ID if is empty
                if (StringUtils.isEmpty(((Annotation) result).getIdLogic())) {
                    ((Annotation) result).setIdLogic(SdmxToolsServer.generateIdForSDMXArtefact(result));
                }
            } else {
                ((Annotation) result).setIdLogic(fixedID);
            }

            // URI
            if (StringUtils.isEmpty(((Annotation) result).getUrl())) {
                ((Annotation) result).setUrl(SdmxToolsServer.generateUri());
            }
        }

        return result;
    }

    private InternationalString internationalStringToEntity(InternationalStringDto internationalStringDto, InternationalString internationalStringOlder) {
        if (internationalStringDto == null) {
            // Delete old entity
            if (internationalStringOlder != null) {
                getInternationalStringRepository().delete(internationalStringOlder);
            }

            return null;
        }
        // NAME
        // InternationalStringDTO to InternationalString

        // Avoid the appearance of trash.
        if (internationalStringOlder != null) {
            internationalStringDto.setId(internationalStringOlder.getId());
            internationalStringDto.setVersion(internationalStringOlder.getVersion());
        }

        InternationalString internationalString = mapToEntity(internationalStringDto, InternationalString.class);

        // LocalisedStringDto to LocalisedString
        for (LocalisedStringDto item : internationalStringDto.getTexts()) {
            if (StringUtils.isNotBlank(item.getLabel())) {
                internationalString.addText(mapToEntity(item, LocalisedString.class));
            }
        }

        return internationalString;
    }

    private Annotation annotationDtoToAnnotation(ServiceContext ctx, AnnotationDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        Annotation annotation = mapToEntity(source, Annotation.class);

        Annotation older = null;
        if (source.getId() != null) {

            try {
                older = getAnnotationRepository().findById(source.getId());
            } catch (AnnotationNotFoundException e) {
                MetamacException metamacException = new MetamacException(e, MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND, Annotation.class.getSimpleName());
                metamacException.setLoggedLevel(ExceptionLevelEnum.ERROR);
                throw metamacException;
            }
        }

        annotation.setText(internationalStringToEntity(source.getText(), (older != null) ? older.getText() : null));

        return annotation;
    }

    private <T extends AnnotableArtefactDto, U extends AnnotableArtefact> U annotableToEntity(ServiceContext ctx, T source, U result) throws MappingException, MetamacException {
        if (source == null) {
            return null;
        }

        for (AnnotationDto annotationDto : source.getAnnotations()) {
            result.getAnnotations().add(annotationDtoToAnnotation(ctx, annotationDto));
        }

        return result;
    }

    private <T extends NameableArtefactDto, U extends NameableArtefact> U nameableToEntity(ServiceContext ctx, T source, U result, U older) throws MappingException, MetamacException {
        // Dozer bypass, Name is required
        if (source == null || source.getName() == null) {
            return null;
        }

        // Name
        result.setName(internationalStringToEntity(source.getName(), (older != null) ? older.getName() : null));
        Validate.notNull(result.getName(), "NameableArtefact.name must not be null"); // By Hibernate strategy inheritance, this is defined here and not in the relational model

        // Description
        result.setDescription(internationalStringToEntity(source.getDescription(), (older != null) ? older.getDescription() : null));

        return annotableToEntity(ctx, source, result);
    }

    private <T extends MaintainableArtefactDto, U extends MaintainableArtefact> U maintainableArtefactToEntity(ServiceContext ctx, T source, U result, U older, SdmxBaseService sdmxBaseService)
            throws MappingException, MetamacException {
        // Dozer bypass, Name is required
        if (source == null || source.getName() == null) {
            return null;
        }

        // // Load Agency for associated it to DSD
        // Organisation organisation = (Organisation) sdmxBaseService.findOrganization(ctx, source.getMaintainerIdLogic());
        // result.setMaintainer((Agency) organisation);
        // result.setMaintainer(mapToEntity(source, ExternalItemBt.class));

        // Basic types check
        List<MetamacExceptionItem> exceptions = new ArrayList<MetamacExceptionItem>();
        BaseInvocationValidator.checkMantainer(result.getMaintainer(), exceptions);
        ExceptionUtils.throwIfException(exceptions);

        Validate.notEmpty(result.getVersionLogic(), "MaintainableArtefact.versionLogic must not be empty"); // By Hibernate strategy inheritance, this is defined here and not in the relational model

        return nameableToEntity(ctx, source, result, older);
    }

    /**************************************************************************
     * PUBLIC (INTERFACE)
     **************************************************************************/
    /*
     * @SuppressWarnings("unchecked")
     * @Override
     * public <T extends Item> T itemDtoToItem(ItemDto itemDto,
     * ServiceContext ctx, SdmxBaseService sdmxBaseService) {
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
     * result.addHierarchy(itemDtoToItem(hierarchyItemDto, ctx, sdmxBaseService));
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
     * SdmxBaseService sdmxBaseService) throws MappingException, OrganisationNotFoundException {
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
     * result.addItem(itemDtoToItem(itemDto, ctx, sdmxBaseService));
     * }
     * // Parent
     * return maintainableArtefactToEntity(itemSchemeDto, result, ctx, sdmxBaseService);
     * }
     */

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Component> T componentDtoToComponent(ComponentDto componentDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MetamacException {
        if (componentDto == null) {
            return null;
        }
        if (componentDto.getTypeComponent() == null) {
            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, TypeComponent.class.getSimpleName());
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }
        // Hierachy:
        // AnnotableArtefact > IdentifiableArtefact > Component
        // |_ DataAttribute
        // |_ ReportingYearStartDay
        // |_ DimensionComponent
        // |_ Dimension
        // |_ MeasureDimension
        // |_ TimeDimension
        // |_ PrimaryMeasure
        // IdentityDto > AuditableDto > AnnotableArtefactDto > IdentifiableArtefactDto > ComponentDto
        // |_ DataAttributeDto
        // |_ DimensionComponentDto

        T result = null;

        Representation representation = null;

        try {
            // DataAttribute ******************************************************
            if (componentDto.getTypeComponent().equals(TypeComponent.DATA_ATTRIBUTE)) {
                if (((DataAttributeDto) componentDto).getTypeDataAttribute() == null) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, ComponentDto.class.getName());
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }

                switch (((DataAttributeDto) componentDto).getTypeDataAttribute()) {
                    case REPORTING_YEAR_START_DAY:
                        // Restriction on the DSD
                        if (!((DataAttributeDto) componentDto).getRole().isEmpty()) {
                            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "role");
                            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                            throw metamacException;
                        }
                        result = (T) mapToEntity(componentDto, ReportingYearStartDay.class);

                        // LocalRepresentation
                        ValidationUtil.validateRepresentationForReportingYearStartDayType(ctx, componentDto.getLocalRepresentation());
                        break;
                    case DATA_ATTRIBUTE:
                        result = (T) mapToEntity(componentDto, DataAttribute.class);
                        break;
                    default:
                }

                Validate.notNull(((DataAttribute) result).getUsageStatus(), "DataAttribute.usageStatus must not be null"); // By Hibernate strategy inheritance, this is defined here and not in the
                                                                                                                           // relational model

                DataAttribute dataAttributeOlder = null;
                if (componentDto.getId() != null) {
                    dataAttributeOlder = (DataAttribute) getComponentRepository().findById(componentDto.getId());
                }

                // Relate To
                ((DataAttribute) result).setRelateTo(attributeRelationshipDtoToAttributeRelationship(((DataAttributeDto) componentDto).getRelateTo(), ctx, sdmxBaseService,
                        (dataAttributeOlder != null) ? dataAttributeOlder.getRelateTo() : null));
                Validate.notNull(((DataAttribute) result).getRelateTo(), "DataAttribute.relateTo must not be null"); // By Hibernate strategy inheritance, this is defined here and not in the
                                                                                                                     // relational
                                                                                                                     // model

                // Role
                if (componentDto.getId() != null) { // Exist
                    for (ExternalItemDto listExternalItemDto : ((DataAttributeDto) componentDto).getRole()) {
                        ExternalItem externalItem = externalItemBtDtoToExternalItem(listExternalItemDto, ctx, sdmxBaseService);
                        boolean found = false;
                        for (ExternalItem extItemPersisted : dataAttributeOlder.getRole()) {
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
                    for (ExternalItemDto listExternalItemDto : ((DataAttributeDto) componentDto).getRole()) {
                        ((DataAttribute) result).addRole(externalItemBtDtoToExternalItem(listExternalItemDto, ctx, sdmxBaseService));
                    }
                }

                // ConceptIdentity
                if (((DataAttributeDto) componentDto).getCptIdRef() == null) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "conceptIdentity");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.INFO);
                    throw metamacException;
                }

                // LocalRepresentation
                ValidationUtil.validateRepresentationForDataAttribute(ctx, componentDto.getLocalRepresentation());
                representation = representationDtoToRepresentation(componentDto.getLocalRepresentation(), ctx, sdmxBaseService,
                        (dataAttributeOlder != null) ? dataAttributeOlder.getLocalRepresentation() : null);
            }
            // DimensionComponent ***************************************************
            else if (componentDto.getTypeComponent().equals(TypeComponent.DIMENSION_COMPONENT)) {
                if (((DimensionComponentDto) componentDto).getTypeDimensionComponent() == null) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_REQUIRED, "TypeDimensionComponent");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }

                // ConceptIdentity
                if (((DimensionComponentDto) componentDto).getCptIdRef() == null) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "conceptIdentity");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }

                switch (((DimensionComponentDto) componentDto).getTypeDimensionComponent()) {
                    case DIMENSION:
                        result = (T) mapToEntity(componentDto, Dimension.class);
                        Dimension dimensionOlder = null;
                        if (componentDto.getId() != null) {
                            dimensionOlder = (Dimension) getComponentRepository().findById(componentDto.getId());
                        }

                        // ROLE
                        if (componentDto.getId() != null) { // Exist
                            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) componentDto).getRole()) {
                                ExternalItem externalItem = externalItemBtDtoToExternalItem(listExternalItemDto, ctx, sdmxBaseService);
                                boolean found = false;
                                for (ExternalItem extItemPersisted : dimensionOlder.getRole()) {
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
                            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) componentDto).getRole()) {
                                ((Dimension) result).addRole(externalItemBtDtoToExternalItem(listExternalItemDto, ctx, sdmxBaseService));
                            }
                        }

                        // LocalRepresentation
                        ValidationUtil.validateRepresentationForDimension(ctx, componentDto.getLocalRepresentation());
                        representation = representationDtoToRepresentation(componentDto.getLocalRepresentation(), ctx, sdmxBaseService,
                                (dimensionOlder != null) ? dimensionOlder.getLocalRepresentation() : null);
                        break;
                    case MEASUREDIMENSION:
                        result = (T) mapToEntity(componentDto, MeasureDimension.class);
                        MeasureDimension measureDimensionOlder = null;
                        if (componentDto.getId() != null) {
                            measureDimensionOlder = (MeasureDimension) getComponentRepository().findById(componentDto.getId());
                        }

                        // ROLE
                        if (componentDto.getId() != null) { // Exist
                            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) componentDto).getRole()) {
                                ExternalItem externalItem = externalItemBtDtoToExternalItem(listExternalItemDto, ctx, sdmxBaseService);
                                boolean found = false;
                                for (ExternalItem extItemPersisted : measureDimensionOlder.getRole()) {
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
                            for (ExternalItemDto listExternalItemDto : ((DimensionComponentDto) componentDto).getRole()) {
                                ((MeasureDimension) result).addRole(externalItemBtDtoToExternalItem(listExternalItemDto, ctx, sdmxBaseService));
                            }
                        }
                        // LocalRepresentation
                        ValidationUtil.validateRepresentationForMeasureDimension(ctx, componentDto.getLocalRepresentation());
                        representation = representationDtoToRepresentation(componentDto.getLocalRepresentation(), ctx, sdmxBaseService,
                                (measureDimensionOlder != null) ? measureDimensionOlder.getLocalRepresentation() : null);
                        break;
                    case TIMEDIMENSION:
                        result = (T) mapToEntity(componentDto, TimeDimension.class);
                        TimeDimension timeDimensionOlder = null;
                        if (componentDto.getId() != null) {
                            timeDimensionOlder = (TimeDimension) getComponentRepository().findById(componentDto.getId());
                        }

                        if (!((DimensionComponentDto) componentDto).getRole().isEmpty()) {
                            MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "role");
                            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                            throw metamacException;
                        }
                        // LocalRepresentation
                        ValidationUtil.validateRepresentationForTimeDimension(ctx, componentDto.getLocalRepresentation());
                        representation = representationDtoToRepresentation(componentDto.getLocalRepresentation(), ctx, sdmxBaseService,
                                (timeDimensionOlder != null) ? timeDimensionOlder.getLocalRepresentation() : null);
                        break;
                    default:
                        // DimensionComponent is a abstract class, cannot be instantiated
                        throw new UnsupportedOperationException("componentDtoToComponent::dimensionComponentDtoToDimensionComponent for Unknown Entity not implemented");
                }
            }
            // Primary Measure ***************************************************
            else if (componentDto.getTypeComponent().equals(TypeComponent.PRIMARY_MEASURE)) {
                result = (T) mapToEntity(componentDto, PrimaryMeasure.class);
                PrimaryMeasure primaryMeasureOlder = null;
                if (componentDto.getId() != null) {
                    primaryMeasureOlder = (PrimaryMeasure) getComponentRepository().findById(componentDto.getId());
                }

                // ConceptIdentity
                if (componentDto.getCptIdRef() == null) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "conceptIdentity");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }

                // LocalRepresentation
                ValidationUtil.validateRepresentationForPrimaryMeasure(ctx, componentDto.getLocalRepresentation());
                representation = representationDtoToRepresentation(componentDto.getLocalRepresentation(), ctx, sdmxBaseService,
                        (primaryMeasureOlder != null) ? primaryMeasureOlder.getLocalRepresentation() : null);
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

            /************************
             * FIELDS for component
             ***********************/

            // ConceptIdentity
            // result.setConceptIdentity((Concept) itemDtoToItem(componentDto.getConceptIdentity(), ctx, sdmxBaseService));
            // result.setCptIdRef(mapToEntity(componentDto.getCptIdRef(), ExternalItemBt.class));

            // LocalRepresentation
            result.setLocalRepresentation(representation);
        } catch (ComponentNotFoundException e) {
            // TODO poner la excepcion adecuada y no la unknows
            MetamacException metamacException = new MetamacException(e, MetamacCoreExceptionType.UNKNOWN);
            metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
            throw metamacException;
        }

        return annotableToEntity(ctx, componentDto, result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ComponentList> T componentListDtoToComponentList(ComponentListDto componentListDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MetamacException {
        if (componentListDto == null) {
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

        switch (componentListDto.getTypeComponentList()) {
            case ATTRIBUTE_DESCRIPTOR:
                result = (T) mapToEntity(componentListDto, AttributeDescriptor.class);
                break;
            case GROUP_DIMENSION_DESCRIPTOR:
                result = (T) mapToEntity(componentListDto, GroupDimensionDescriptor.class);
                // Validate.notNull(((GroupDimensionDescriptor)result).getIsAttachmentConstraint(), "GroupDimensionDescriptor.isAttachmentConstraint must not be null"); // By Hibernate strategy
                // inheritance, this is defined here and not in the relational model
                break;
            case DIMENSION_DESCRIPTOR:
                result = (T) mapToEntity(componentListDto, DimensionDescriptor.class);
                break;
            case MEASURE_DESCRIPTOR:
                result = (T) mapToEntity(componentListDto, MeasureDescriptor.class);
                break;
            default:
                // ComponentList is a abstract class, cannot be instantiated
                throw new UnsupportedOperationException("componentListDtoToComponentList for Unknown Entity not implemented");
        }

        // Update
        // if (componentListDto.getId() != null) {
        // ComponentList componentListOld = getComponentListRepository().findById(componentListDto.getId());
        //
        // // Without DTO fields: NO MERGE NEEDED!!!
        // result.getAnnotations().addAll(componentListOld.getAnnotations());
        // }

        /****************
         * FIELDS
         ****************/
        // Components
        for (ComponentDto componentDto : componentListDto.getComponents()) {
            result.addComponent(componentDtoToComponent(componentDto, ctx, sdmxBaseService));
        }

        return annotableToEntity(ctx, componentListDto, result);
    }

    private AttributeRelationship attributeRelationshipDtoToAttributeRelationship(RelationshipDto relationshipDto, ServiceContext ctx, SdmxBaseService sdmxBaseService,
            AttributeRelationship attributeRelationshipOlder) throws MetamacException {
        if (relationshipDto == null) {
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

        AttributeRelationship attributeRelationship = null;

        switch (relationshipDto.getTypeRelathionship()) {
            case DIMENSION_RELATIONSHIP:
                attributeRelationship = mapToEntity(relationshipDto, DimensionRelationship.class);

                // Remove old data
                if (attributeRelationshipOlder != null) {
                    if (!(attributeRelationshipOlder instanceof DimensionRelationship) || (relationshipDto.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                        getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
                        attributeRelationship = new DimensionRelationship();
                        attributeRelationship.setVersion(relationshipDto.getVersion());
                    }
                }

                // DimensionRelationship: GroupKey
                for (DescriptorDto descriptorDto : relationshipDto.getGroupKeyForDimensionRelationship()) {
                    if (descriptorDto.getTypeComponentList().equals(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR)) {
                        ((DimensionRelationship) attributeRelationship).addGroupKey((GroupDimensionDescriptor) componentListDtoToComponentList(descriptorDto, ctx, sdmxBaseService));
                    } else {
                        MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.PARAMETER_INCORRECT, "TypeComponentList");
                        metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                        throw metamacException;
                    }
                }

                // DimensionRelationship: Dimension
                if (relationshipDto.getDimensionForDimensionRelationship().isEmpty()) {
                    MetamacException metamacException = new MetamacException(MetamacCoreExceptionType.MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MIN, "dimensionForDimensionRelationship");
                    metamacException.setLoggedLevel(ExceptionLevelEnum.DEBUG);
                    throw metamacException;
                }
                for (DimensionComponentDto dimensionComponentDto : relationshipDto.getDimensionForDimensionRelationship()) {
                    ((DimensionRelationship) attributeRelationship).addDimension((DimensionComponent) componentDtoToComponent(dimensionComponentDto, ctx, sdmxBaseService));
                }
                break;
            case GROUP_RELATIONSHIP:
                attributeRelationship = mapToEntity(relationshipDto, GroupRelationship.class);

                // Remove old data
                if (attributeRelationshipOlder != null) {
                    if (!(attributeRelationshipOlder instanceof GroupRelationship) || (relationshipDto.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                        getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
                        attributeRelationship = new GroupRelationship();
                        attributeRelationship.setVersion(relationshipDto.getVersion());
                    }
                }

                // Group
                ((GroupRelationship) attributeRelationship).setGroupKey((GroupDimensionDescriptor) componentListDtoToComponentList(relationshipDto.getGroupKeyForGroupRelationship(), ctx,
                        sdmxBaseService));

                Validate.notNull(((GroupRelationship) attributeRelationship).getGroupKey(), "GroupRelationship.groupKey must not be null"); // By Hibernate strategy inheritance, this is defined here
                                                                                                                                            // and not in the relational model
                break;
            case NO_SPECIFIED_RELATIONSHIP:
                attributeRelationship = mapToEntity(relationshipDto, NoSpecifiedRelationship.class);

                // Remove old data
                if (attributeRelationshipOlder != null) {
                    if (!(attributeRelationshipOlder instanceof NoSpecifiedRelationship) || (relationshipDto.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                        getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
                        attributeRelationship = new NoSpecifiedRelationship();
                        attributeRelationship.setVersion(relationshipDto.getVersion());
                    }
                }

                break;
            case PRIMARY_MEASURE_RELATIONSHIP:
                attributeRelationship = mapToEntity(relationshipDto, PrimaryMeasureRelationship.class);

                // Remove old data
                if (attributeRelationshipOlder != null) {
                    if (!(attributeRelationshipOlder instanceof PrimaryMeasureRelationship) || (relationshipDto.getId().compareTo(attributeRelationshipOlder.getId()) != 0)) {
                        getAttributeRelationshipRepository().delete(attributeRelationshipOlder);
                        attributeRelationship = new PrimaryMeasureRelationship();
                        attributeRelationship.setVersion(relationshipDto.getVersion());
                    }
                }

                break;
        }

        return attributeRelationship;
    }

    @Override
    public DataStructureDefinition dataStructureDefinitionDtoToDataStructureDefinition(DataStructureDefinitionDto dataStructureDefinitionDto, ServiceContext ctx, SdmxBaseService sdmxBaseService)
            throws MetamacException {
        if (dataStructureDefinitionDto == null) {
            return null;
        }
        // Hierachy:
        // DataStructureDefinitionDto > MaintainableArtefactDto > NameableArtefactDto > IdentifiableArtefactDto > AnnotableArtefacDto > AuditableDto > IdentityDto
        // DataStructureDefinition > Structure > MaintainableArtefact > NameableArtefact > IdentifiableArtefact > AnnotableArtefact

        DataStructureDefinition result = mapToEntity(dataStructureDefinitionDto, DataStructureDefinition.class);

        // If update
        DataStructureDefinition dataStructureDefinition;
        try {
            dataStructureDefinition = null;
            if (dataStructureDefinitionDto.getId() != null) {
                dataStructureDefinition = getDataStructureDefinitionRepository().findById(dataStructureDefinitionDto.getId());

                // Withouts DTO fields: NO MERGE NEEDED!!!
                // |_ Grouping
                result.getGrouping().addAll(dataStructureDefinition.getGrouping());
                // |_ Annotations

            }
        } catch (DataStructureDefinitionNotFoundException e) {
            // TODO poner la excepcion adecuada y no la unknows
            MetamacException metamacException = new MetamacException(e, MetamacCoreExceptionType.UNKNOWN);
            metamacException.setLoggedLevel(ExceptionLevelEnum.ERROR);
            throw metamacException;
        }

        // Parent
        return maintainableArtefactToEntity(ctx, dataStructureDefinitionDto, result, dataStructureDefinition, sdmxBaseService);
    }

    @Override
    public Representation representationDtoToRepresentation(RepresentationDto representationDto, ServiceContext ctx, SdmxBaseService sdmxBaseService, Representation representationOlder)
            throws MetamacException {
        if (representationDto == null) {
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

        switch (representationDto.getTypeRepresentationEnum()) {
            case ENUMERATED:
                representation = mapToEntity(representationDto, EnumeratedRepresentation.class);

                // Remove old data if type changed
                if (representationOlder != null) {
                    if (!(representationOlder instanceof EnumeratedRepresentation) || (representationOlder.getId().compareTo(representationDto.getId()) != 0)) {
                        getRepresentationRepository().delete(representationOlder);
                        representation = new EnumeratedRepresentation();
                        representation.setVersion(representationDto.getVersion());
                    }
                }

                // EnumeratedRepresentation: enumerated
                ((EnumeratedRepresentation) representation).setEnumerated(mapToEntity(representationDto.getEnumerated(), ExternalItem.class));

                break;
            case TEXT_FORMAT:
                representation = mapToEntity(representationDto, TextFormatRepresentation.class);

                // Remove old data if type changed
                if (representationOlder != null) {
                    if (!(representationOlder instanceof TextFormatRepresentation) || (representationOlder.getId().compareTo(representationDto.getId()) != 0)) {
                        getRepresentationRepository().delete(representationOlder);
                        representation = new TextFormatRepresentation();
                        representation.setVersion(representationDto.getVersion());
                    }
                }

                // Facet
                ((TextFormatRepresentation) representation).setNonEnumerated(facetDtoToFacet(representationDto.getNonEnumerated(), ctx, sdmxBaseService));

                break;
            default:
                break;
        }

        return representation;
    }

    @Override
    public Facet facetDtoToFacet(FacetDto facetDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MetamacException {
        if (facetDto == null) {
            return null;
        }

        Facet facet = mapToEntity(facetDto, Facet.class);

        return facet;
    }

    @Override
    public ExternalItem externalItemBtDtoToExternalItem(ExternalItemDto externalItemBtDto, ServiceContext ctx, SdmxBaseService sdmxBaseService) throws MetamacException {
        if (externalItemBtDto == null) {
            return null;
        }

        // ExternalItem result = new ExternalItem(new ExternalItemBt(externalItemBtDto.getUriInt(), externalItemBtDto.getCodeId(), externalItemBtDto.getType()));
        ExternalItem result = mapToEntity(externalItemBtDto, ExternalItem.class);;

        ExternalItem older = null;
        
        if (externalItemBtDto.getId() != null) {
            try {
                older = getExternalItemRepository().findById(externalItemBtDto.getId());
            }
            catch (ExternalItemNotFoundException e) {
                MetamacException metamacException = new MetamacException(e, MetamacCoreExceptionType.MTM_CORE_SEARCH_NOT_FOUND, ExternalItem.class.getSimpleName());
                metamacException.setLoggedLevel(ExceptionLevelEnum.ERROR);
                throw metamacException;
            }
        }
        
        result.setTitle(internationalStringToEntity(externalItemBtDto.getTitle(), (older != null) ? older.getTitle() : null));

        return result;
    }

}
