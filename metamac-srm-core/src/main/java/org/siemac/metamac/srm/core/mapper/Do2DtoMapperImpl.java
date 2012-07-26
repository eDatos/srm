package org.siemac.metamac.srm.core.mapper;

import org.dozer.DozerBeanMapper;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.domain.srm.dto.AnnotableArtefactDto;
import org.siemac.metamac.domain.srm.dto.AnnotationDto;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.ComponentListDto;
import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionExtendDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.domain.srm.dto.FacetDto;
import org.siemac.metamac.domain.srm.dto.MaintainableArtefactDto;
import org.siemac.metamac.domain.srm.dto.NameableArtefactDto;
import org.siemac.metamac.domain.srm.dto.RelationshipDto;
import org.siemac.metamac.domain.srm.dto.RepresentationDto;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeComponentList;
import org.siemac.metamac.domain.srm.enume.domain.TypeDataAttribute;
import org.siemac.metamac.domain.srm.enume.domain.TypeDimensionComponent;
import org.siemac.metamac.domain.srm.enume.domain.TypeDozerCopyMode;
import org.siemac.metamac.domain.srm.enume.domain.TypeRelathionship;
import org.siemac.metamac.domain.srm.enume.domain.TypeRepresentationEnum;
import org.siemac.metamac.srm.core.base.domain.AnnotableArtefact;
import org.siemac.metamac.srm.core.base.domain.Annotation;
import org.siemac.metamac.srm.core.base.domain.Component;
import org.siemac.metamac.srm.core.base.domain.ComponentList;
import org.siemac.metamac.srm.core.base.domain.EnumeratedRepresentation;
import org.siemac.metamac.srm.core.base.domain.Facet;
import org.siemac.metamac.srm.core.base.domain.MaintainableArtefact;
import org.siemac.metamac.srm.core.base.domain.NameableArtefact;
import org.siemac.metamac.srm.core.base.domain.Representation;
import org.siemac.metamac.srm.core.base.domain.TextFormatRepresentation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersion;
import org.siemac.metamac.srm.core.structure.domain.AttributeDescriptor;
import org.siemac.metamac.srm.core.structure.domain.AttributeRelationship;
import org.siemac.metamac.srm.core.structure.domain.DataAttribute;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Do2DtoMapperImpl implements Do2DtoMapper {

    @Autowired
    @Qualifier("mapperCoreUpdateMode")
    private DozerBeanMapper mapperCoreUpdateMode;

    @Autowired
    @Qualifier("mapperCoreCreateMode")
    private DozerBeanMapper mapperCoreCreateMode;

    /**************************************************************************
     * GETTERS
     **************************************************************************/
    @Override
    public DozerBeanMapper getMapperCore(TypeDozerCopyMode typeDozerCopyMode) {
        switch (typeDozerCopyMode) {
            case UPDATE:
                return mapperCoreUpdateMode;
            case CREATE:
                return mapperCoreCreateMode;
            default:
                break;
        }
        return null;
    }

    /**************************************************************************
     * PUBLIC (INTERFACE)
     **************************************************************************/

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ComponentDto> T componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component) {
        if (component == null) {
            return null;
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

        // DataAttribute ******************************************************
        if (component instanceof DataAttribute) {
            result = (T) getMapperCore(typeDozerCopyMode).map(component, DataAttributeDto.class);
            result.setTypeComponent(TypeComponent.DATA_ATTRIBUTE);

            // Type of DataAttribute
            if (component instanceof ReportingYearStartDay) {
                ((DataAttributeDto) result).setTypeDataAttribute(TypeDataAttribute.REPORTING_YEAR_START_DAY);
            } else {
                // DataAttribute is a concrete class, could be instantiated
                ((DataAttributeDto) result).setTypeDataAttribute(TypeDataAttribute.DATA_ATTRIBUTE);
            }

            // Relate To
            ((DataAttributeDto) result).setRelateTo(attributeRelationshipToattributeRelationshipDto(typeDozerCopyMode, ((DataAttribute) component).getRelateTo()));

            // Role
            for (ExternalItem conceptExtItem : ((DataAttribute) component).getRole()) {
                // ((DataAttributeDto)result).addRole((ConceptDto) itemToItemDto(concept, ctx, sdmxBaseService));
                ((DataAttributeDto) result).addRole(externalItemToExternalItemDto(typeDozerCopyMode, conceptExtItem));
            }
        }
        // DimensionComponent *************************************************
        else if (component instanceof DimensionComponent) {
            result = (T) getMapperCore(typeDozerCopyMode).map(component, DimensionComponentDto.class);
            result.setTypeComponent(TypeComponent.DIMENSION_COMPONENT);

            // TYPE of Dimension **********************************************
            if (component instanceof Dimension) {
                ((DimensionComponentDto) result).setTypeDimensionComponent(TypeDimensionComponent.DIMENSION);

                // Role
                for (ExternalItem conceptExtItem : ((Dimension) component).getRole()) {
                    // ((DimensionComponentDto)result).addRole((ConceptDto) itemToItemDto(concept, ctx, sdmxBaseService));
                    ((DimensionComponentDto) result).addRole(externalItemToExternalItemDto(typeDozerCopyMode, conceptExtItem));
                }
            }
            // TYPE of MeasureDimension ***************************************
            else if (component instanceof MeasureDimension) {
                ((DimensionComponentDto) result).setTypeDimensionComponent(TypeDimensionComponent.MEASUREDIMENSION);

                // Role
                for (ExternalItem conceptExtItem : ((MeasureDimension) component).getRole()) {
                    // ((DimensionComponentDto)result).addRole((ConceptDto) itemToItemDto(concept, ctx, sdmxBaseService));
                    ((DimensionComponentDto) result).addRole(externalItemToExternalItemDto(typeDozerCopyMode, conceptExtItem));
                }
            }
            // TYPE of TimeDimension ******************************************
            else if (component instanceof TimeDimension) {
                ((DimensionComponentDto) result).setTypeDimensionComponent(TypeDimensionComponent.TIMEDIMENSION);
            } else {
                // DimensionComponent is a abstract class, cannot be instantiated
                throw new UnsupportedOperationException("componentToComponentDto::dimensionComponentToDimensionComponentDto for Unknown Entity not implemented");
            }

        }
        // PrimaryMeasure *****************************************************
        else if (component instanceof PrimaryMeasure) {
            result = (T) getMapperCore(typeDozerCopyMode).map(component, ComponentDto.class);
            result.setTypeComponent(TypeComponent.PRIMARY_MEASURE);
        } else {
            // Component is a abstract class, cannot be instantiated
            throw new UnsupportedOperationException("componentToComponentDto for Unknown Entity not implemented");
        }

        /************************
         * FIELDS
         ***********************/
        // ConceptIdentity
        if (component.getCptIdRef() != null) {
            result.setCptIdRef(getMapperCore(typeDozerCopyMode).map(component.getCptIdRef(), ExternalItemDto.class));
        }
        // result.setConceptIdentity((ConceptDto) itemToItemDto(component.getConceptIdentity(), ctx, sdmxBaseService));

        // LocalRepresentation
        result.setLocalRepresentation(representationToRepresentationDto(typeDozerCopyMode, component.getLocalRepresentation()));

        return annotableToDto(typeDozerCopyMode, component, result);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ComponentListDto> T componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList) {
        if (componentList == null) {
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

        if (componentList instanceof AttributeDescriptor) {
            result = (T) getMapperCore(typeDozerCopyMode).map(componentList, DescriptorDto.class);
            // TypeComponentList
            result.setTypeComponentList(TypeComponentList.ATTRIBUTE_DESCRIPTOR);
        } else if (componentList instanceof DimensionDescriptor) {
            result = (T) getMapperCore(typeDozerCopyMode).map(componentList, DescriptorDto.class);
            // TypeComponentList
            result.setTypeComponentList(TypeComponentList.DIMENSION_DESCRIPTOR);
        } else if (componentList instanceof GroupDimensionDescriptor) {
            result = (T) getMapperCore(typeDozerCopyMode).map(componentList, DescriptorDto.class);
            // TypeComponentList
            result.setTypeComponentList(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
        } else if (componentList instanceof MeasureDescriptor) {
            result = (T) getMapperCore(typeDozerCopyMode).map(componentList, DescriptorDto.class);
            // TypeComponentList
            result.setTypeComponentList(TypeComponentList.MEASURE_DESCRIPTOR);
        } else {
            // ComponentList is a abstract class, cannot be instantiated
            throw new UnsupportedOperationException("componentListToComponentListDto for Unknown Entity not implemented");
        }

        /****************
         * FIELDS
         ****************/
        // Components
        for (Component component : componentList.getComponents()) {
            result.addComponent(componentToComponentDto(typeDozerCopyMode, component));
        }

        return annotableToDto(typeDozerCopyMode, componentList, result);
    }

    @Override
    public DataStructureDefinitionDto dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition) {
        if (dataStructureDefinition == null) {
            return null;
        }
        // Hierachy:
        // DataStructureDefinitionDto > MaintainableArtefactDto > NameableArtefactDto > IdentifiableArtefactDto > AnnotableArtefacDto > AuditableDto > IdentityDto
        // DataStructureDefinition > Structure > MaintainableArtefact > NameableArtefact > IdentifiableArtefact > AnnotableArtefact

        DataStructureDefinitionDto result = getMapperCore(typeDozerCopyMode).map(dataStructureDefinition, DataStructureDefinitionDto.class);

        // Parent
        return maintainableArtefactToDto(typeDozerCopyMode, dataStructureDefinition, result);
    }

    @Override
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinition dataStructureDefinition) {
        if (dataStructureDefinition == null) {
            return null;
        }
        // Hierachy:
        // DataStructureDefinitionDto > MaintainableArtefactDto > NameableArtefactDto > IdentifiableArtefactDto > AnnotableArtefacDto > AuditableDto > IdentityDto
        // DataStructureDefinition > Structure > MaintainableArtefact > NameableArtefact > IdentifiableArtefact > AnnotableArtefact

        // TO EXTENDS
        DataStructureDefinitionExtendDto result = getMapperCore(typeDozerCopyMode).map(dataStructureDefinition, DataStructureDefinitionExtendDto.class);

        for (ComponentList componentList : dataStructureDefinition.getGrouping()) {
            result.addGrouping((DescriptorDto) componentListToComponentListDto(typeDozerCopyMode, componentList));
        }

        // Parent
        return maintainableArtefactToDto(typeDozerCopyMode, dataStructureDefinition, result);
    }

    /**************************************************************************
     * PRIVATE
     **************************************************************************/

    /**
     * @param typeDozerCopyMode
     * @param internationalString
     * @return
     */
    private InternationalStringDto internationalStringToDto(TypeDozerCopyMode typeDozerCopyMode, InternationalString internationalString) {
        if (internationalString == null) {
            return null;
        }

        // NAME
        // InternationalString to InternationalString Dto
        InternationalStringDto internationalStringDto = getMapperCore(typeDozerCopyMode).map(internationalString, InternationalStringDto.class);

        // LocalisedStringDto to LocalisedString
        for (LocalisedString item : internationalString.getTexts()) {
            internationalStringDto.addText(getMapperCore(typeDozerCopyMode).map(item, LocalisedStringDto.class));
        }

        return internationalStringDto;
    }

    /**
     * @param <T>
     * @param <U>
     * @param typeDozerCopyMode
     * @param source
     * @param result
     * @return
     */
    private <T extends AnnotableArtefact, U extends AnnotableArtefactDto> U annotableToDto(TypeDozerCopyMode typeDozerCopyMode, T source, U result) {
        // Dozer bypass, Name is required
        if (source == null) {
            return null;
        }

        for (Annotation annotation : source.getAnnotations()) {
            result.getAnnotations().add(annotationToDto(typeDozerCopyMode, annotation));
        }

        return result;
    }

    /**
     * @param typeDozerCopyMode
     * @param source
     * @return
     */
    private AnnotationDto annotationToDto(TypeDozerCopyMode typeDozerCopyMode, Annotation source) {
        if (source == null) {
            return null;
        }

        AnnotationDto annotationDto = getMapperCore(typeDozerCopyMode).map(source, AnnotationDto.class);
        annotationDto.setText(internationalStringToDto(typeDozerCopyMode, source.getText()));

        return annotationDto;
    }

    /**
     * @param <T>
     * @param <U>
     * @param typeDozerCopyMode
     * @param source
     * @param result
     * @return
     */
    private <T extends NameableArtefact, U extends NameableArtefactDto> U nameableToDto(TypeDozerCopyMode typeDozerCopyMode, T source, U result) {
        // Dozer bypass, Name is required
        if (source == null || source.getName() == null) {
            return null;
        }

        // Name
        result.setName(internationalStringToDto(typeDozerCopyMode, source.getName()));

        // Description
        result.setDescription(internationalStringToDto(typeDozerCopyMode, source.getDescription()));

        return annotableToDto(typeDozerCopyMode, source, result);
    }

    /**
     * @param <T>
     * @param <U>
     * @param typeDozerCopyMode
     * @param source
     * @param result
     * @return
     */
    private <T extends MaintainableArtefact, U extends MaintainableArtefactDto> U maintainableArtefactToDto(TypeDozerCopyMode typeDozerCopyMode, T source, U result) {
        // Dozer bypass, Name is required
        if (source == null || source.getName() == null) {
            return null;
        }

        // Load MaintenanceAgency for associated it to DSD DTO
        result.setMaintainer(getMapperCore(typeDozerCopyMode).map(source.getMaintainer(), ExternalItemDto.class));

        // Parent
        return nameableToDto(typeDozerCopyMode, source, result);
    }

    private RelationshipDto attributeRelationshipToattributeRelationshipDto(TypeDozerCopyMode typeDozerCopyMode, AttributeRelationship attributeRelationship) {
        if (attributeRelationship == null) {
            return null;
        }
        // Hierachy:
        // AttributeRelationship
        // |_ NoSpecifiedRelationship
        // |_ PrimaryMeasureRelationship
        // |_ GroupRelationship
        // |_ DimensionRelationship
        // RelationshipDto > IdentityDto

        RelationshipDto result = null;

        if (attributeRelationship instanceof NoSpecifiedRelationship) {
            result = getMapperCore(typeDozerCopyMode).map(attributeRelationship, RelationshipDto.class);
            // TypeRelationship
            result.setTypeRelathionship(TypeRelathionship.NO_SPECIFIED_RELATIONSHIP);

        } else if (attributeRelationship instanceof PrimaryMeasureRelationship) {
            result = getMapperCore(typeDozerCopyMode).map(attributeRelationship, RelationshipDto.class);
            // TypeRelationship
            result.setTypeRelathionship(TypeRelathionship.PRIMARY_MEASURE_RELATIONSHIP);

        } else if (attributeRelationship instanceof GroupRelationship) {
            result = getMapperCore(typeDozerCopyMode).map(attributeRelationship, RelationshipDto.class);
            // TypeRelationship
            result.setTypeRelathionship(TypeRelathionship.GROUP_RELATIONSHIP);
            // groupKeyForGroupRelationship
            result.setGroupKeyForGroupRelationship((DescriptorDto) componentListToComponentListDto(typeDozerCopyMode, ((GroupRelationship) attributeRelationship).getGroupKey()));

        } else if (attributeRelationship instanceof DimensionRelationship) {
            result = getMapperCore(typeDozerCopyMode).map(attributeRelationship, RelationshipDto.class);
            // TypeRelationship
            result.setTypeRelathionship(TypeRelathionship.DIMENSION_RELATIONSHIP);
            // dimensionForDimensionRelationship
            for (DimensionComponent dimensionComponent : ((DimensionRelationship) attributeRelationship).getDimension()) {
                result.addDimensionForDimensionRelationship((DimensionComponentDto) componentToComponentDto(typeDozerCopyMode, dimensionComponent));
            }
            // groupKeyForDimensionRelationship
            for (GroupDimensionDescriptor groupDimensionDescriptor : ((DimensionRelationship) attributeRelationship).getGroupKey()) {
                result.addGroupKeyForDimensionRelationship((DescriptorDto) componentListToComponentListDto(typeDozerCopyMode, groupDimensionDescriptor));
            }
        }

        /****************
         * FIELDS
         ****************/

        return result;
    }

    private RepresentationDto representationToRepresentationDto(TypeDozerCopyMode typeDozerCopyMode, Representation representation) {
        if (representation == null) {
            return null;
        }

        // Hierachy:
        // AttributeRelationship
        // |_ NoSpecifiedRelationship
        // |_ PrimaryMeasureRelationship
        // |_ GroupRelationship
        // |_ DimensionRelationship
        // RelationshipDto > IdentityDto

        RepresentationDto result = null;

        if (representation instanceof EnumeratedRepresentation) {
            result = getMapperCore(typeDozerCopyMode).map(representation, RepresentationDto.class);

            // TypeRepresentationEnum
            result.setTypeRepresentationEnum(TypeRepresentationEnum.ENUMERATED);
        } else if (representation instanceof TextFormatRepresentation) {
            result = getMapperCore(typeDozerCopyMode).map(representation, RepresentationDto.class);

            // TypeRepresentationEnum
            result.setTypeRepresentationEnum(TypeRepresentationEnum.TEXT_FORMAT);

            result.setNonEnumerated(facetToFacetDto(typeDozerCopyMode, ((TextFormatRepresentation) representation).getNonEnumerated()));
        }

        return result;
    }

    private FacetDto facetToFacetDto(TypeDozerCopyMode typeDozerCopyMode, Facet facet) {
        if (facet == null) {
            return null;
        }

        FacetDto facetDto = getMapperCore(typeDozerCopyMode).map(facet, FacetDto.class);

        return facetDto;
    }

    private ExternalItemDto externalItemToExternalItemDto(TypeDozerCopyMode typeDozerCopyMode, ExternalItem externalItem) {
        if (externalItem == null) {
            return null;
        }

        ExternalItemDto result = getMapperCore(typeDozerCopyMode).map(externalItem, ExternalItemDto.class);
        result.setTitle(internationalStringToDto(typeDozerCopyMode, externalItem.getTitle()));

        return result;
    }

    @Override
    public ConceptSchemeDto conceptSchemeDoToDto(ConceptSchemeVersion conceptSchemeVersion) {
        // TODO Auto-generated method stub
        return null;
    }

}
