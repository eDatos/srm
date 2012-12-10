package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CodeCriteriaPropertyOrder;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CodelistCriteriaPropertyOrder;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.CodelistCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class CodesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements CodesRest2DoMapper {

    private RestCriteria2SculptorCriteria<CodelistVersionMetamac> codelistCriteriaMapper = null;
    private RestCriteria2SculptorCriteria<CodeMetamac>            codeCriteriaMapper     = null;

    public CodesRest2DoMapperImpl() {
        codelistCriteriaMapper = new RestCriteria2SculptorCriteria<CodelistVersionMetamac>(CodelistVersionMetamac.class, CodelistCriteriaPropertyOrder.class,
                CodelistCriteriaPropertyRestriction.class, new CodelistCriteriaCallback());
        codeCriteriaMapper = new RestCriteria2SculptorCriteria<CodeMetamac>(CodeMetamac.class, CodeCriteriaPropertyOrder.class, CodeCriteriaPropertyRestriction.class, new CodeCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<CodelistVersionMetamac> getCodelistCriteriaMapper() {
        return codelistCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<CodeMetamac> getCodeCriteriaMapper() {
        return codeCriteriaMapper;
    }

    private class CodelistCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CodelistCriteriaPropertyRestriction propertyNameCriteria = CodelistCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue());
                case URN:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().urn(), propertyRestriction.getValue());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getValue());
                case VALID_FROM:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validFrom());
                case VALID_TO:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validTo());
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()));
                case INTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate());
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getValue());
                case EXTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate());
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getValue());
                case LATEST:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()));
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            CodelistCriteriaPropertyOrder propertyNameCriteria = CodelistCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CodelistVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CodelistVersionMetamacProperties.maintainableArtefact().code();
        }
    }

    private class CodeCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CodeCriteriaPropertyRestriction propertyNameCriteria = CodeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().code(), propertyRestriction.getValue());
                case URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().urn(), propertyRestriction.getValue());
                case NAME:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getValue());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            CodeCriteriaPropertyOrder propertyNameCriteria = CodeCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CodeMetamacProperties.nameableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CodeMetamacProperties.nameableArtefact().code();
        }
    }
}