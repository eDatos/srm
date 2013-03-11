package org.siemac.metamac.srm.core.concept.mapper;

import org.apache.commons.lang.ObjectUtils;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
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
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("conceptsDto2DoMapper")
public class ConceptsDto2DoMapperImpl implements ConceptsDto2DoMapper {

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
        target.setType(source.getType());
        target.setRelatedOperation(dto2DoMapperSdmxSrm.externalItemDtoToExternalItem(source.getRelatedOperation(), target.getRelatedOperation(),
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

        if (source.getConceptExtends() != null) {
            ConceptMetamac conceptExtends = retrieveConcept(source.getConceptExtends().getUrn());
            target.setConceptExtends(conceptExtends);
        } else {
            target.setConceptExtends(null);
        }
        // note: not conversion to relatedConcepts, roles... Call 'add/delete RelatedConcepts' operation of Service

        if (source.getVariable() != null) {
            Variable variable = retrieveVariable(source.getVariable().getUrn());
            target.setVariable(variable);
        } else {
            target.setVariable(null);
        }

        dto2DoMapperSdmxSrm.conceptDtoToDo(source, target);

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

    private ConceptSchemeVersionMetamac retrieveConceptScheme(String urn) throws MetamacException {
        ConceptSchemeVersionMetamac target = conceptSchemeVersionMetamacRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
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
}