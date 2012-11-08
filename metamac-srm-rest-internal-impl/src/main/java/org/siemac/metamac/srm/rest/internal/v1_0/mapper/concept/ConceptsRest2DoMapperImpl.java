package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeCriteriaPropertyOrder;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.springframework.stereotype.Component;

@Component
public class ConceptsRest2DoMapperImpl implements ConceptsRest2DoMapper {

    private RestCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> conceptSchemeCriteriaMapper = null;

    public ConceptsRest2DoMapperImpl() {
        conceptSchemeCriteriaMapper = new RestCriteria2SculptorCriteria<ConceptSchemeVersionMetamac>(ConceptSchemeVersionMetamac.class, ConceptSchemeCriteriaPropertyOrder.class,
                ConceptSchemeCriteriaPropertyRestriction.class, new ConceptSchemeCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<ConceptSchemeVersionMetamac> getConceptSchemeCriteriaMapper() {
        return conceptSchemeCriteriaMapper;
    }

    private class ConceptSchemeCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            ConceptSchemeCriteriaPropertyRestriction propertyNameCriteria = ConceptSchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue());
                case NAME:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name(), propertyRestriction.getValue());
                case URN:
                    return new SculptorPropertyCriteria(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getValue());
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
            return ConceptSchemeVersionMetamacProperties.id();
        }
    }

    private RestException toRestExceptionParameterIncorrect(String parameter) {
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameter);
        throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
    }
}