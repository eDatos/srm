package org.siemac.metamac.srm.rest.external.v1_0.mapper.concept;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptSchemeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
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
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ConceptSchemeCriteriaPropertyRestriction propertyNameCriteria = ConceptSchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, ConceptSchemeVersionMetamacProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case TYPE:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.type(), PropertyTypeEnum.CONCEPT_SCHEME_TYPE, propertyRestriction);
                case STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.relatedOperation().urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, ConceptSchemeVersionMetamacProperties.maintainableArtefact().validFrom(),
                            ConceptSchemeVersionMetamac.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, ConceptSchemeVersionMetamacProperties.maintainableArtefact().validTo(), ConceptSchemeVersionMetamac.class,
                            false);
                case PROC_STATUS:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), PropertyTypeEnum.PROC_STATUS, propertyRestriction);
                case INTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            ConceptSchemeVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case EXTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            ConceptSchemeVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case LATEST:
                    return buildSculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ConceptCriteriaPropertyRestriction propertyNameCriteria = ConceptCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, ConceptMetamacProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(ConceptMetamacProperties.nameableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION_SOURCE:
                    return buildSculptorPropertyCriteria(ConceptMetamacProperties.descriptionSource().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case ACRONYM:
                    return buildSculptorPropertyCriteria(ConceptMetamacProperties.acronym().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case EXTENDS_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, ConceptMetamacProperties.conceptExtends().nameableArtefact());
                case RELATED_CONCEPT_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, ConceptMetamacProperties.relatedConcepts().nameableArtefact());
                case CONCEPT_SCHEME_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact());
                case CONCEPT_SCHEME_TYPE:
                    return buildSculptorPropertyCriteria(new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type()
                            .getName(), false, ConceptMetamac.class), PropertyTypeEnum.CONCEPT_SCHEME_TYPE, propertyRestriction);
                case CONCEPT_SCHEME_STATISTICAL_OPERATION_URN:
                    return buildSculptorPropertyCriteria(new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties
                            .relatedOperation().urn().getName(), false, ConceptMetamac.class), PropertyTypeEnum.STRING, propertyRestriction);
                case CONCEPT_SCHEME_PROC_STATUS:
                    return buildSculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic(), PropertyTypeEnum.PROC_STATUS_ITEM_SCHEME_FROM_ITEM,
                            propertyRestriction);
                case CONCEPT_SCHEME_LATEST:
                    return buildSculptorPropertyCriteria(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return ConceptMetamacProperties.nameableArtefact().code();
        }
    }

}