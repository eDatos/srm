package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilyCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilyCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilyCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilyCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class CodesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements CodesRest2DoMapper {

    private RestCriteria2SculptorCriteria<CodelistVersionMetamac> codelistCriteriaMapper       = null;
    private RestCriteria2SculptorCriteria<CodeMetamac>            codeCriteriaMapper           = null;
    private RestCriteria2SculptorCriteria<VariableFamily>         variableFamilyCriteriaMapper = null;
    private RestCriteria2SculptorCriteria<Variable>               variableCriteriaMapper       = null;
    private RestCriteria2SculptorCriteria<CodelistFamily>         codelistFamilyCriteriaMapper = null;

    public CodesRest2DoMapperImpl() {
        codelistCriteriaMapper = new RestCriteria2SculptorCriteria<CodelistVersionMetamac>(CodelistVersionMetamac.class, CodelistCriteriaPropertyOrder.class,
                CodelistCriteriaPropertyRestriction.class, new CodelistCriteriaCallback());
        codeCriteriaMapper = new RestCriteria2SculptorCriteria<CodeMetamac>(CodeMetamac.class, CodeCriteriaPropertyOrder.class, CodeCriteriaPropertyRestriction.class, new CodeCriteriaCallback());
        variableFamilyCriteriaMapper = new RestCriteria2SculptorCriteria<VariableFamily>(VariableFamily.class, VariableFamilyCriteriaPropertyOrder.class,
                VariableFamilyCriteriaPropertyRestriction.class, new VariableFamilyCriteriaCallback());
        variableCriteriaMapper = new RestCriteria2SculptorCriteria<Variable>(Variable.class, VariableCriteriaPropertyOrder.class, VariableCriteriaPropertyRestriction.class,
                new VariableCriteriaCallback());
        codelistFamilyCriteriaMapper = new RestCriteria2SculptorCriteria<CodelistFamily>(CodelistFamily.class, CodelistFamilyCriteriaPropertyOrder.class,
                CodelistFamilyCriteriaPropertyRestriction.class, new CodelistFamilyCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<CodelistVersionMetamac> getCodelistCriteriaMapper() {
        return codelistCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<CodeMetamac> getCodeCriteriaMapper() {
        return codeCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper() {
        return variableFamilyCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<Variable> getVariableCriteriaMapper() {
        return variableCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<CodelistFamily> getCodelistFamilyCriteriaMapper() {
        return codelistFamilyCriteriaMapper;
    }

    private class CodelistCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CodelistCriteriaPropertyRestriction propertyNameCriteria = CodelistCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return getUrnSculptorPropertyCriteriaDisjunction(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().description().texts().label(), propertyRestriction.getValue(),
                            propertyRestriction.getOperationType());
                case DESCRIPTION_SOURCE:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.descriptionSource().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case VALID_FROM:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validFrom(), CodelistVersionMetamac.class, false);
                case VALID_TO:
                    return getSculptorPropertyCriteriaDate(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validTo(), CodelistVersionMetamac.class, false);
                case PROC_STATUS:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus(), propertyRestrictionValueToProcStatusEnum(propertyRestriction.getValue()),
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
                    return getUrnSculptorPropertyCriteriaDisjunction(propertyRestriction, CodeMetamacProperties.nameableArtefact());
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
                case CODELIST_URN:
                    return getUrnSculptorPropertyCriteriaDisjunction(propertyRestriction, CodeMetamacProperties.itemSchemeVersion().maintainableArtefact());
                case CODELIST_EXTERNALLY_PUBLISHED:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
                case CODELIST_LATEST:
                    return new SculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), Boolean.valueOf(propertyRestriction.getValue()),
                            propertyRestriction.getOperationType());
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

    private class VariableFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            VariableFamilyCriteriaPropertyRestriction propertyNameCriteria = VariableFamilyCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return getUrnSculptorPropertyCriteriaDisjunction(propertyRestriction, VariableFamilyProperties.nameableArtefact());
                case NAME:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            VariableFamilyCriteriaPropertyOrder propertyNameCriteria = VariableFamilyCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return VariableFamilyProperties.nameableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return VariableFamilyProperties.nameableArtefact().code();
        }
    }

    private class VariableCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            VariableCriteriaPropertyRestriction propertyNameCriteria = VariableCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(VariableProperties.nameableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return getUrnSculptorPropertyCriteriaDisjunction(propertyRestriction, VariableProperties.nameableArtefact());
                case NAME:
                    return new SculptorPropertyCriteria(VariableProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case SHORT_NAME:
                    return new SculptorPropertyCriteria(VariableProperties.shortName().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case FAMILY_URN:
                    return getUrnSculptorPropertyCriteriaDisjunction(propertyRestriction, VariableProperties.families().nameableArtefact());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            VariableCriteriaPropertyOrder propertyNameCriteria = VariableCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return VariableProperties.nameableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return VariableProperties.nameableArtefact().code();
        }
    }

    private class CodelistFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CodelistFamilyCriteriaPropertyRestriction propertyNameCriteria = CodelistFamilyCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().code(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                case URN:
                    return getUrnSculptorPropertyCriteriaDisjunction(propertyRestriction, CodelistFamilyProperties.nameableArtefact());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().name().texts().label(), propertyRestriction.getValue(), propertyRestriction.getOperationType());
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            CodelistFamilyCriteriaPropertyOrder propertyNameCriteria = CodelistFamilyCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CodelistFamilyProperties.nameableArtefact().code();
                default:
                    throw toRestExceptionParameterIncorrect(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CodelistFamilyProperties.nameableArtefact().code();
        }
    }
}