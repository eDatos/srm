package org.siemac.metamac.srm.rest.external.v1_0.mapper.code;

import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.common.query.domain.OperationTypeEnum;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaDisjunction;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamilyCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamilyCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamilyCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamilyCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements CodesRest2DoMapper {

    @Autowired
    private ConfigurationService                                  configurationService;

    private RestCriteria2SculptorCriteria<CodelistVersionMetamac> codelistCriteriaMapper        = null;
    private RestCriteria2SculptorCriteria<CodeMetamac>            codeCriteriaMapper            = null;
    private RestCriteria2SculptorCriteria<VariableFamily>         variableFamilyCriteriaMapper  = null;
    private RestCriteria2SculptorCriteria<Variable>               variableCriteriaMapper        = null;
    private RestCriteria2SculptorCriteria<VariableElement>        variableElementCriteriaMapper = null;
    private RestCriteria2SculptorCriteria<CodelistFamily>         codelistFamilyCriteriaMapper  = null;

    public CodesRest2DoMapperImpl() {
        codelistCriteriaMapper = new RestCriteria2SculptorCriteria<CodelistVersionMetamac>(CodelistVersionMetamac.class, CodelistCriteriaPropertyOrder.class,
                CodelistCriteriaPropertyRestriction.class, new CodelistCriteriaCallback());
        codeCriteriaMapper = new RestCriteria2SculptorCriteria<CodeMetamac>(CodeMetamac.class, CodeCriteriaPropertyOrder.class, CodeCriteriaPropertyRestriction.class, new CodeCriteriaCallback());
        variableFamilyCriteriaMapper = new RestCriteria2SculptorCriteria<VariableFamily>(VariableFamily.class, VariableFamilyCriteriaPropertyOrder.class,
                VariableFamilyCriteriaPropertyRestriction.class, new VariableFamilyCriteriaCallback());
        variableCriteriaMapper = new RestCriteria2SculptorCriteria<Variable>(Variable.class, VariableCriteriaPropertyOrder.class, VariableCriteriaPropertyRestriction.class,
                new VariableCriteriaCallback());
        variableElementCriteriaMapper = new RestCriteria2SculptorCriteria<VariableElement>(VariableElement.class, VariableElementCriteriaPropertyOrder.class,
                VariableElementCriteriaPropertyRestriction.class, new VariableElementCriteriaCallback());
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
    public RestCriteria2SculptorCriteria<VariableElement> getVariableElementCriteriaMapper() {
        return variableElementCriteriaMapper;
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
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION_SOURCE:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.descriptionSource().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case VARIABLE_URN:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.variable().nameableArtefact().urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validFrom(), CodelistVersionMetamac.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CodelistVersionMetamacProperties.maintainableArtefact().validTo(), CodelistVersionMetamac.class, false);
                case PROC_STATUS:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().procStatus(), PropertyTypeEnum.PROC_STATUS, propertyRestriction);
                case INTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            CodelistVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case EXTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            CodelistVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case LATEST:
                    return buildSculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    return buildSculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CodeMetamacProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case SHORT_NAME:
                    SculptorPropertyCriteria propertyCriteria1 = buildSculptorPropertyCriteria(CodeMetamacProperties.shortName().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                    SculptorPropertyCriteria propertyCriteria2 = buildSculptorPropertyCriteria(CodeMetamacProperties.variableElement().shortName().texts().label(), PropertyTypeEnum.STRING,
                            propertyRestriction);
                    return new SculptorPropertyCriteriaDisjunction(propertyCriteria1, propertyCriteria2);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(CodeMetamacProperties.nameableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case CODELIST_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CodeMetamacProperties.itemSchemeVersion().maintainableArtefact());
                case CODELIST_PROC_STATUS:
                    return buildSculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic(), PropertyTypeEnum.PROC_STATUS_ITEM_SCHEME_FROM_ITEM,
                            propertyRestriction);
                case CODELIST_LATEST:
                    return buildSculptorPropertyCriteria(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().latestFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                case DEFAULT_GEOGRAPHICAL_GRANULARITIES_CODELIST: {
                    String codelistUrn = null;
                    try {
                        codelistUrn = configurationService.retrieveDefaultCodelistGeographicalGranularityUrn();;
                    } catch (MetamacException e) {
                        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.UNKNOWN);
                        throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
                    }
                    MetamacRestQueryPropertyRestriction codelistPropertyRestriction = new MetamacRestQueryPropertyRestriction();
                    codelistPropertyRestriction.setPropertyName(CodeCriteriaPropertyRestriction.CODELIST_URN.name());
                    codelistPropertyRestriction.setValue(codelistUrn);
                    Boolean value = Boolean.valueOf(propertyRestriction.getValue());
                    if (value) {
                        codelistPropertyRestriction.setOperationType(OperationTypeEnum.EQ);
                    } else {
                        codelistPropertyRestriction.setOperationType(OperationTypeEnum.NE);
                    }
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(codelistPropertyRestriction, CodeMetamacProperties.itemSchemeVersion().maintainableArtefact());
                }
                case VARIABLE_ELEMENT_URN:
                    return buildSculptorPropertyCriteria(CodeMetamacProperties.variableElement().identifiableArtefact().urn(), PropertyTypeEnum.STRING, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    return buildSculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, VariableFamilyProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    return buildSculptorPropertyCriteria(VariableProperties.nameableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, VariableProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(VariableProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case SHORT_NAME:
                    return buildSculptorPropertyCriteria(VariableProperties.shortName().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case FAMILY_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, VariableProperties.families().nameableArtefact());
                case VARIABLE_TYPE:
                    return buildSculptorPropertyCriteria(VariableProperties.type(), PropertyTypeEnum.VARIABLE_TYPE, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return VariableProperties.nameableArtefact().code();
        }
    }

    private class VariableElementCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            VariableElementCriteriaPropertyRestriction propertyNameCriteria = VariableElementCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(VariableElementProperties.identifiableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, VariableElementProperties.identifiableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(VariableElementProperties.shortName().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case GEOGRAPHICAL_GRANULARITY_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, VariableElementProperties.geographicalGranularity().nameableArtefact());
                case VARIABLE_TYPE:
                    return buildSculptorPropertyCriteria(VariableElementProperties.variable().type(), PropertyTypeEnum.VARIABLE_TYPE, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) throws RestException {
            VariableElementCriteriaPropertyOrder propertyNameCriteria = VariableElementCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return VariableElementProperties.identifiableArtefact().code();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return VariableElementProperties.identifiableArtefact().code();
        }
    }

    private class CodelistFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) throws RestException {
            CodelistFamilyCriteriaPropertyRestriction propertyNameCriteria = CodelistFamilyCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CodelistFamilyProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
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
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() throws RestException {
            return CodelistFamilyProperties.nameableArtefact().code();
        }
    }
}