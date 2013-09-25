package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaOrder;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.soap.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.soap.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.soap.criteria.mapper.SoapCriteria2SculptorCriteria;
import org.siemac.metamac.soap.criteria.mapper.SoapCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.soap.exception.SoapCommonServiceExceptionType;
import org.siemac.metamac.soap.exception.SoapExceptionUtils;
import org.siemac.metamac.soap.structural_resources.v1_0.ExceptionFault;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistCriteriaPropertyOrder;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistCriteriaPropertyRestriction;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilyCriteriaPropertyOrder;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilyCriteriaPropertyRestriction;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableCriteriaPropertyOrder;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableCriteriaPropertyRestriction;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilyCriteriaPropertyOrder;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilyCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.soap.external.exception.SoapExceptionParameters;
import org.springframework.stereotype.Component;

@Component
public class CodesSoap2DoMapperV10Impl implements CodesSoap2DoMapperV10 {

    private SoapCriteria2SculptorCriteria<VariableFamily>         variableFamilyCriteriaMapper = null;
    private SoapCriteria2SculptorCriteria<Variable>               variableCriteriaMapper       = null;
    private SoapCriteria2SculptorCriteria<CodelistFamily>         codelistFamilyCriteriaMapper = null;
    private SoapCriteria2SculptorCriteria<CodelistVersionMetamac> codelistCriteriaMapper       = null;

    public CodesSoap2DoMapperV10Impl() throws ExceptionFault {
        variableFamilyCriteriaMapper = new SoapCriteria2SculptorCriteria<VariableFamily>(VariableFamily.class, VariableFamilyCriteriaPropertyOrder.class,
                VariableFamilyCriteriaPropertyRestriction.class, new VariableFamilyCriteriaCallback());
        variableCriteriaMapper = new SoapCriteria2SculptorCriteria<Variable>(Variable.class, VariableCriteriaPropertyOrder.class, VariableCriteriaPropertyRestriction.class,
                new VariableCriteriaCallback());
        codelistFamilyCriteriaMapper = new SoapCriteria2SculptorCriteria<CodelistFamily>(CodelistFamily.class, CodelistFamilyCriteriaPropertyOrder.class,
                CodelistFamilyCriteriaPropertyRestriction.class, new CodelistFamilyCriteriaCallback());
        codelistCriteriaMapper = new SoapCriteria2SculptorCriteria<CodelistVersionMetamac>(CodelistVersionMetamac.class, CodelistCriteriaPropertyOrder.class,
                CodelistCriteriaPropertyRestriction.class, new CodelistVersionCriteriaCallback());
    }

    @Override
    public SoapCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper() {
        return variableFamilyCriteriaMapper;
    }

    @Override
    public SoapCriteria2SculptorCriteria<Variable> getVariableCriteriaMapper() {
        return variableCriteriaMapper;
    }

    @Override
    public SoapCriteria2SculptorCriteria<CodelistFamily> getCodelistFamilyCriteriaMapper() {
        return codelistFamilyCriteriaMapper;
    }

    @Override
    public SoapCriteria2SculptorCriteria<CodelistVersionMetamac> getCodelistCriteriaMapper() {
        return codelistCriteriaMapper;
    }

    private class VariableFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws ExceptionFault {
            VariableFamilyCriteriaPropertyRestriction propertyNameCriteria = VariableFamilyCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case NAME:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<VariableFamily> retrievePropertyOrder(MetamacCriteriaOrder order) throws ExceptionFault {
            VariableFamilyCriteriaPropertyOrder propertyNameCriteria = VariableFamilyCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return VariableFamilyProperties.nameableArtefact().code();
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<VariableFamily> retrievePropertyOrderDefault() throws ExceptionFault {
            return VariableFamilyProperties.nameableArtefact().code();
        }
    }

    private class VariableCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws ExceptionFault {
            VariableCriteriaPropertyRestriction propertyNameCriteria = VariableCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(VariableProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case NAME:
                    return new SculptorPropertyCriteria(VariableProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case SHORT_NAME:
                    return new SculptorPropertyCriteria(VariableProperties.shortName().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case FAMILY_ID:
                    return new SculptorPropertyCriteria(VariableProperties.families().nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<Variable> retrievePropertyOrder(MetamacCriteriaOrder order) throws ExceptionFault {
            VariableCriteriaPropertyOrder propertyNameCriteria = VariableCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return VariableProperties.nameableArtefact().code();
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<Variable> retrievePropertyOrderDefault() throws ExceptionFault {
            return VariableProperties.nameableArtefact().code();
        }
    }

    private class CodelistFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws ExceptionFault {
            CodelistFamilyCriteriaPropertyRestriction propertyNameCriteria = CodelistFamilyCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistFamilyProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<CodelistFamily> retrievePropertyOrder(MetamacCriteriaOrder order) throws ExceptionFault {
            CodelistFamilyCriteriaPropertyOrder propertyNameCriteria = CodelistFamilyCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CodelistFamilyProperties.nameableArtefact().code();
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<CodelistFamily> retrievePropertyOrderDefault() throws ExceptionFault {
            return CodelistFamilyProperties.nameableArtefact().code();
        }
    }

    private class CodelistVersionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws ExceptionFault {
            CodelistCriteriaPropertyRestriction propertyNameCriteria = CodelistCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case NAME:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().name().texts().label(), propertyRestriction.getStringValue(),
                            propertyRestriction.getOperation());
                case DESCRIPTION_SOURCE:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.descriptionSource().texts().label(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case FAMILY_ID:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.family().nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case VARIABLE_ID:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.variable().nameableArtefact().code(), propertyRestriction.getStringValue(), propertyRestriction.getOperation());
                case LAST_VERSION:
                    return new SculptorPropertyCriteria(CodelistVersionMetamacProperties.maintainableArtefact().latestPublic(), propertyRestriction.getBooleanValue(),
                            propertyRestriction.getOperation());
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<CodelistVersionMetamac> retrievePropertyOrder(MetamacCriteriaOrder order) throws ExceptionFault {
            CodelistCriteriaPropertyOrder propertyNameCriteria = CodelistCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CodelistVersionMetamacProperties.maintainableArtefact().code();
                default:
                    throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
            }
        }

        @Override
        public Property<CodelistVersionMetamac> retrievePropertyOrderDefault() throws ExceptionFault {
            return CodelistVersionMetamacProperties.maintainableArtefact().code();
        }
    }
}
