package org.siemac.metamac.srm.core.mapper;

import java.util.HashSet;
import java.util.Set;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.ent.domain.ExternalItemRepository;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.InternationalStringRepository;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.ConceptTypeRepository;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.exception.ConceptMetamacNotFoundException;
import org.siemac.metamac.srm.core.concept.exception.ConceptSchemeVersionMetamacNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionParametersInternal;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;

@org.springframework.stereotype.Component("dto2DoMapper")
public class Dto2DoMapperImpl implements Dto2DoMapper {

    @Autowired
    private InternationalStringRepository                        internationalStringRepository;

    @Autowired
    private ExternalItemRepository                               externalItemRepository;

    @Autowired
    @Qualifier("dto2DoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.mapper.Dto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private ConceptSchemeVersionMetamacRepository                conceptSchemeVersionMetamacRepository;

    @Autowired
    private ConceptMetamacRepository                             conceptMetamacRepository;

    @Autowired
    private ConceptTypeRepository                                conceptTypeRepository;

    // ------------------------------------------------------------
    // DSDs
    // ------------------------------------------------------------

    @Override
    public <U extends Component> U componentDtoToComponent(ServiceContext ctx, ComponentDto source) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentDtoToComponent(ctx, source);
    }

    @Override
    public <U extends ComponentList> U componentListDtoToComponentList(ServiceContext ctx, ComponentListDto componentListDto) throws MetamacException {
        return (U) dto2DoMapperSdmxSrm.componentListDtoToComponentList(ctx, componentListDto);
    }

    @Override
    public DataStructureDefinitionVersion dataStructureDefinitionDtoToDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionDto dataStructureDefinitionDto) throws MetamacException {
        return dto2DoMapperSdmxSrm.dataStructureDefinitionDtoToDataStructureDefinition(ctx, dataStructureDefinitionDto);
    }

    // ------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------

    @Override
    public ConceptSchemeVersionMetamac conceptSchemeDtoToDo(ServiceContext ctx, ConceptSchemeMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        ConceptSchemeVersionMetamac target = null;
        if (source.getId() == null) {
            target = new ConceptSchemeVersionMetamac();
        } else {
            try {
                target = conceptSchemeVersionMetamacRepository.findById(source.getId());
            } catch (ConceptSchemeVersionMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.CONCEPT_SCHEME)
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        // Optimistic locking: Update "update date" attribute to force root entity update, to increment "version" attribute
        target.setUpdateDateMetamac(new DateTime());

        // Modifiable attributes
        target.setType(source.getType());
        target.setRelatedOperation(externalItemDtoToExternalItem(ctx, source.getRelatedOperation(), target.getRelatedOperation(), ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION));
        target.setProcStatus(source.getProcStatus());

        dto2DoMapperSdmxSrm.conceptSchemeDtoToDo(ctx, source, target);

        return target;
    }

    @Override
    public ConceptMetamac conceptDtoToDo(ServiceContext ctx, ConceptMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        ConceptMetamac target = null;
        if (source.getId() == null) {
            target = new ConceptMetamac();
        } else {
            try {
                target = conceptMetamacRepository.findById(source.getId());
            } catch (ConceptMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SRM_SEARCH_NOT_FOUND).withMessageParameters(ServiceExceptionParameters.CONCEPT)
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        // Optimistic locking: Update "update date" attribute to force root entity update, to increment "version" attribute
        target.setUpdateDateMetamac(new DateTime());

        // Modifiable attributes
        target.setPluralName(internationalStringToDo(ctx, source.getPluralName(), target.getPluralName(), ServiceExceptionParameters.CONCEPT_PLURAL_NAME));
        target.setAcronym(internationalStringToDo(ctx, source.getAcronym(), target.getAcronym(), ServiceExceptionParameters.CONCEPT_ACRONYM));
        target.setDescriptionSource(internationalStringToDo(ctx, source.getDescriptionSource(), target.getDescriptionSource(), ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE));
        target.setContext(internationalStringToDo(ctx, source.getContext(), target.getContext(), ServiceExceptionParameters.CONCEPT_CONTEXT));
        target.setDocMethod(internationalStringToDo(ctx, source.getDocMethod(), target.getDocMethod(), ServiceExceptionParameters.CONCEPT_DOC_METHOD));
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setType(conceptTypeDtoToDo(source.getType()));
        target.setDerivation(internationalStringToDo(ctx, source.getDerivation(), target.getDerivation(), ServiceExceptionParameters.CONCEPT_DERIVATION));
        target.setLegalActs(internationalStringToDo(ctx, source.getLegalActs(), target.getLegalActs(), ServiceExceptionParameters.CONCEPT_LEGAL_ACTS));

        if (source.getConceptExtendsUrn() != null) {
            ConceptMetamac conceptExtends = conceptMetamacRepository.findByUrn(source.getConceptExtendsUrn());
            if (conceptExtends == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_NOT_FOUND).withMessageParameters(source.getConceptExtendsUrn()).build();
            }
            target.setConceptExtends(conceptExtends);
        } else {
            target.setConceptExtends(null);
        }
        
        // note: not conversion to relatedConcepts, roles... Call 'add/delete RelatedConcepts' operation of Service

        dto2DoMapperSdmxSrm.conceptDtoToDo(ctx, source, target);

        return target;
    }

    private ConceptType conceptTypeDtoToDo(ConceptTypeDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        ConceptType conceptType = conceptTypeRepository.findByIdentifier(source.getIdentifier());
        if (conceptType == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_TYPE_NOT_FOUND).withMessageParameters(source.getIdentifier()).build();
        }
        return conceptType;
    }

    // ------------------------------------------------------------
    // EXTERNAL ITEM
    // ------------------------------------------------------------
    private ExternalItem externalItemDtoToExternalItem(ServiceContext ctx, ExternalItemDto source, ExternalItem target, String metadataName) throws MetamacException {
        if (source == null) {
            if (target != null) {
                // delete previous entity
                externalItemRepository.delete(target);
            }
            return null;
        }

        if (target == null) {
            // New
            target = new ExternalItem(source.getCode(), source.getUri(), source.getUrn(), source.getType());
        }
        target.setCode(source.getCode());
        target.setUri(source.getUri());
        target.setUrn(source.getUrn());
        target.setType(source.getType());
        target.setManagementAppUrl(source.getManagementAppUrl());
        target.setTitle(internationalStringToDo(ctx, source.getTitle(), target.getTitle(), metadataName + ServiceExceptionParametersInternal.EXTERNAL_ITEM_TITLE));

        return target;
    }

    // ------------------------------------------------------------
    // INTERNATIONAL STRING
    // ------------------------------------------------------------

    /**
     * Transform {@link InternationalStringDto} to {@link InternationalString}
     * 
     * @param source DTO to transform
     * @param target Current Entity for this DTO, null if is new
     * @param metadataName Parameter name's on the internationalString relationship
     * @return
     */
    private InternationalString internationalStringToDo(ServiceContext ctx, InternationalStringDto source, InternationalString target, String metadataName) throws MetamacException {
        if (source == null) {
            if (target != null) {
                // delete previous entity
                internationalStringRepository.delete(target);
            }
            return null;
        }

        if (target == null) {
            target = new InternationalString();
        }

        if (ValidationUtils.isEmpty(source)) {
            throw new MetamacException(ServiceExceptionType.METADATA_REQUIRED, metadataName);
        }

        Set<LocalisedString> localisedStringEntities = localisedStringDtoToDo(ctx, source.getTexts(), target.getTexts());
        target.getTexts().clear();
        target.getTexts().addAll(localisedStringEntities);

        return target;
    }

    /**
     * Transform a {@link LocalisedString}, reusing existing locales
     */
    private Set<LocalisedString> localisedStringDtoToDo(ServiceContext ctx, Set<LocalisedStringDto> sources, Set<LocalisedString> targets) {

        Set<LocalisedString> targetsBefore = targets;
        targets = new HashSet<LocalisedString>();

        for (LocalisedStringDto source : sources) {
            boolean existsBefore = false;
            for (LocalisedString target : targetsBefore) {
                if (source.getLocale().equals(target.getLocale())) {
                    targets.add(localisedStringDtoToDo(ctx, source, target));
                    existsBefore = true;
                    break;
                }
            }
            if (!existsBefore) {
                targets.add(localisedStringDtoToDo(ctx, source));
            }
        }
        return targets;
    }

    private LocalisedString localisedStringDtoToDo(ServiceContext ctx, LocalisedStringDto source) {
        LocalisedString target = new LocalisedString();
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        return target;
    }

    private LocalisedString localisedStringDtoToDo(ServiceContext ctx, LocalisedStringDto source, LocalisedString target) {
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        return target;
    }

}
