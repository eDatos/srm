package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class ConceptsRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements ConceptsRest2DoMapper {

    private RestCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> conceptSchemeCriteriaMapper = null;
    private RestCriteria2SculptorCriteria<ConceptMetamac>              conceptCriteriaMapper       = null;

    public ConceptsRest2DoMapperImpl() {
        conceptSchemeCriteriaMapper = new RestCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamac.class, ConceptSchemeCriteriaPropertyOrder.class,
                ConceptSchemeCriteriaPropertyRestriction.class, new ConceptSchemeCriteriaCallback());
        conceptCriteriaMapper = new RestCriteria2SculptorCriteria<ConceptMetamac>(ConceptMetamac.class, ConceptCriteriaPropertyOrder.class, ConceptCriteriaPropertyRestriction.class,
                new ConceptCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeCriteriaMapper() {
        return conceptSchemeCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<ConceptMetamac> getConceptCriteriaMapper() {
        return conceptCriteriaMapper;
    }

    private class ConceptSchemeCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ConceptSchemeCriteriaPropertyRestriction propertyNameCriteria = ConceptSchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case TYPE:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.type(), propertyRestrictionValueToConceptSchemeTypeEnum(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                case STATISTICAL_OPERATION_URN:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.relatedOperation().uri(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case VALID_FROM:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, ConceptSchemeVersionMetamacProperties.maintainableArtefact().validFrom(), ConceptSchemeVersionMetamac.class, false);
                case VALID_TO:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, ConceptSchemeVersionMetamacProperties.maintainableArtefact().validTo(), ConceptSchemeVersionMetamac.class, false);
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(),
                            propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()), propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(), ConceptSchemeVersionMetamac.class,
                            true);
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(), ConceptSchemeVersionMetamac.class,
                            true);
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case LATEST:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            ConceptSchemeCriteriaPropertyOrder propertyNameCriteria = ConceptSchemeCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return ConceptSchemeVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return ConceptSchemeVersionMetamacProperties.maintainableArtefact().code();
        }
    }

    private class ConceptCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ConceptCriteriaPropertyRestriction propertyNameCriteria = ConceptCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION_SOURCE:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.descriptionSource().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case ACRONYM:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.acronym().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case EXTENDS_URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.conceptExtends().nameableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case RELATED_CONCEPT_URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.relatedConcepts().nameableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_URN:
                    return new SculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urnProvider(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_TYPE:
                    return new SculptorPropertyCriteria(new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(),
                            ConceptSchemeVersionMetamacProperties.type().getName(), false, ConceptMetamac.class), propertyRestrictionValueToConceptSchemeTypeEnum(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                case CONCEPT_SCHEME_STATISTICAL_OPERATION_URN:
                    return new SculptorPropertyCriteria(new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties
                            .relatedOperation().urn().getName(), false, ConceptMetamac.class), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            ConceptCriteriaPropertyOrder propertyNameCriteria = ConceptCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return ConceptMetamacProperties.nameableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return ConceptMetamacProperties.nameableArtefact().code();
        }
    }

    private ConceptSchemeTypeEnum propertyRestrictionValueToConceptSchemeTypeEnum(String value) {
        return value != null ? ConceptSchemeTypeEnum.valueOf(value) : null;
    }

}