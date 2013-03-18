package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteria;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.common.query.domain.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistCriteriaPropertyRestriction;
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
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case VALID_FROM:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validFrom(), CodelistVersionMetamac.class, false);
                case VALID_TO:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validTo(), CodelistVersionMetamac.class, false);
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                case REPLACE_TO:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.replaceToCodelists().maintainableArtefact().urn(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case INTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(), CodelistVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case EXTERNAL_PUBLICATION_DATE:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(), CodelistVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case LATEST:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
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
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CodeCriteriaPropertyRestriction propertyNameCriteria = CodeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().urnProvider(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case NAME:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case SHORT_NAME:
                    SculptorPropertyCriteria propertyCriteria1 = new SculptorPropertyCriteria(CodeMetamacProperties.shortName().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                    SculptorPropertyCriteria propertyCriteria2 = new SculptorPropertyCriteria(CodeMetamacProperties.variableElement().shortName().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                    return new SculptorPropertyCriteriaDisjunction(propertyCriteria1, propertyCriteria2);
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().description().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
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