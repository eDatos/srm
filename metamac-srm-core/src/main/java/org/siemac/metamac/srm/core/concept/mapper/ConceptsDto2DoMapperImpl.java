package org.siemac.metamac.srm.core.concept.mapper;

import org.apache.commons.lang.ObjectUtils;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableRepository;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.ConceptTypeRepository;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.domain.QuantityRepository;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.dto.QuantityDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.RepresentationDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

@org.springframework.stereotype.Component("conceptsDto2DoMapper")
public class ConceptsDto2DoMapperImpl extends BaseDto2DoMapperImpl implements ConceptsDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private ConceptSchemeVersionMetamacRepository                                conceptSchemeVersionMetamacRepository;

    @Autowired
    private ConceptMetamacRepository                                             conceptMetamacRepository;

    @Autowired
    private ConceptTypeRepository                                                conceptTypeRepository;

    @Autowired
    private VariableRepository                                                   variableRepository;

    @Autowired
    private QuantityRepository                                                   quantityRepository;

    @Autowired
    private CodeMetamacRepository                                                codeMetamacRepository;

    // ------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------

    @Override
    public ConceptSchemeVersionMetamac conceptSchemeDtoToDo(ConceptSchemeMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        ConceptSchemeVersionMetamac target = null;
        if (source.getUrn() == null) {
            target = new ConceptSchemeVersionMetamac();
        } else {
            target = retrieveConceptScheme(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        target.setIsTypeUpdated(!ObjectUtils.equals(source.getType(), target.getType()));
        target.setIsTypeEmptyPreviously(target.getType() == null);
        target.setType(source.getType());
        target.setRelatedOperation(externalItemDtoStatisticalOperationsToExternalItemDo(source.getRelatedOperation(), target.getRelatedOperation(),
                ServiceExceptionParameters.CONCEPT_SCHEME_RELATED_OPERATION));

        dto2DoMapperSdmxSrm.conceptSchemeDtoToDo(source, target);

        return target;
    }

    @Override
    public ConceptMetamac conceptDtoToDo(ConceptMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        ConceptMetamac target = null;
        if (source.getUrn() == null) {
            target = new ConceptMetamac();
        } else {
            target = retrieveConcept(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        target.setPluralName(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getPluralName(), target.getPluralName(), ServiceExceptionParameters.CONCEPT_PLURAL_NAME));
        target.setAcronym(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getAcronym(), target.getAcronym(), ServiceExceptionParameters.CONCEPT_ACRONYM));
        target.setDescriptionSource(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getDescriptionSource(), target.getDescriptionSource(), ServiceExceptionParameters.CONCEPT_DESCRIPTION_SOURCE));
        target.setContext(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getContext(), target.getContext(), ServiceExceptionParameters.CONCEPT_CONTEXT));
        target.setDocMethod(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getDocMethod(), target.getDocMethod(), ServiceExceptionParameters.CONCEPT_DOC_METHOD));
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setConceptType(conceptTypeDtoToDo(source.getConceptType()));
        target.setDerivation(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getDerivation(), target.getDerivation(), ServiceExceptionParameters.CONCEPT_DERIVATION));
        target.setLegalActs(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getLegalActs(), target.getLegalActs(), ServiceExceptionParameters.CONCEPT_LEGAL_ACTS));
        target.setConceptExtends(toConceptRelation(source.getConceptExtends()));
        target.setQuantity(quantityDtoToDo(source.getQuantity(), target.getQuantity()));
        // note: not conversion to relatedConcepts, roles... Call 'add/delete RelatedConcepts' operation of Service

        if (source.getVariable() != null) {
            Variable variable = retrieveVariable(source.getVariable().getUrn());
            target.setVariable(variable);
        } else {
            target.setVariable(null);
        }

        dto2DoMapperSdmxSrm.conceptDtoToDo(source, target);

        // Set if special requirements are required
        if (target.getCoreRepresentation() != null) {
            target.getCoreRepresentation().setIsExtended(isCoreRepresentationExtends(source.getCoreRepresentation()));
        }

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

    private Quantity quantityDtoToDo(QuantityDto source, Quantity target) throws MetamacException {
        if (source == null) {
            if (target != null) {
                quantityRepository.delete(target);
            }
            return null;
        }
        if (target == null) {
            target = new Quantity();
        }
        target.setQuantityType(source.getQuantityType());
        target.setUnitCode(toCodeRelation(source.getUnitCode()));
        target.setUnitSymbolPosition(source.getUnitSymbolPosition());
        target.setSignificantDigits(source.getSignificantDigits());
        target.setDecimalPlaces(source.getDecimalPlaces());
        target.setUnitMultiplier(source.getUnitMultiplier());
        target.setMinimum(source.getMinimum());
        target.setMaximum(source.getMaximum());
        target.setNumerator(toConceptRelation(source.getNumerator()));
        target.setDenominator(toConceptRelation(source.getDenominator()));
        target.setIsPercentage(source.getIsPercentage());
        target.setPercentageOf(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getPercentageOf(), target.getPercentageOf(), ServiceExceptionParameters.CONCEPT_QUANTITY_PERCENTAGE_OF));
        target.setBaseValue(source.getBaseValue());
        target.setBaseTime(source.getBaseTime());
        // TODO quantity.baseLocation
        target.setBaseQuantity(toConceptRelation(source.getBaseQuantity()));
        return target;
    }

    private ConceptSchemeVersionMetamac retrieveConceptScheme(String urn) throws MetamacException {
        ConceptSchemeVersionMetamac target = conceptSchemeVersionMetamacRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private ConceptMetamac toConceptRelation(RelatedResourceDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        return retrieveConcept(source.getUrn());
    }

    private ConceptMetamac retrieveConcept(String urn) throws MetamacException {
        ConceptMetamac target = conceptMetamacRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private Variable retrieveVariable(String urn) throws MetamacException {
        Variable target = variableRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private CodeMetamac toCodeRelation(RelatedResourceDto source) throws MetamacException {
        if (source == null) {
            return null;
        }
        return retrieveCode(source.getUrn());
    }

    private CodeMetamac retrieveCode(String urn) throws MetamacException {
        CodeMetamac target = codeMetamacRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private boolean isCoreRepresentationExtends(RepresentationDto representationDto) throws MetamacException {
        if (representationDto == null) {
            return false;
        }

        if (RepresentationTypeEnum.ENUMERATION.equals(representationDto.getRepresentationType())) {
            if (representationDto.getEnumeration() != null && RelatedResourceTypeEnum.CONCEPT_SCHEME.equals(representationDto.getEnumeration().getType())) {
                return true;
            }
        }
        return false;
    }
}