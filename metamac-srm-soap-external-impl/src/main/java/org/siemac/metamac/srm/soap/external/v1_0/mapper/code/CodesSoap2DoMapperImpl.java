package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaOrder;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilyCriteriaPropertyOrder;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilyCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.SoapCriteria2SculptorCriteria;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.SoapCriteria2SculptorCriteria.CriteriaCallback;
import org.springframework.stereotype.Component;

@Component
public class CodesSoap2DoMapperImpl implements CodesSoap2DoMapper {

    private SoapCriteria2SculptorCriteria<VariableFamily> variableFamilyCriteriaMapper = null;

    public CodesSoap2DoMapperImpl() throws MetamacException {
        variableFamilyCriteriaMapper = new SoapCriteria2SculptorCriteria<VariableFamily>(VariableFamily.class, VariableFamilyCriteriaPropertyOrder.class,
                VariableFamilyCriteriaPropertyRestriction.class, new VariableFamilyCriteriaCallback());
    }

    @Override
    public SoapCriteria2SculptorCriteria<VariableFamily> getVariableFamilyCriteriaMapper() {
        return variableFamilyCriteriaMapper;
    }

    private class VariableFamilyCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            VariableFamilyCriteriaPropertyRestriction propertyNameCriteria = VariableFamilyCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().code(), propertyRestriction.getStringValue());
                case NAME:
                    return new SculptorPropertyCriteria(VariableFamilyProperties.nameableArtefact().name().texts().label(), propertyRestriction.getStringValue());
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<VariableFamily> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            VariableFamilyCriteriaPropertyOrder propertyNameCriteria = VariableFamilyCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return VariableFamilyProperties.nameableArtefact().code();
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<VariableFamily> retrievePropertyOrderDefault() throws MetamacException {
            return VariableFamilyProperties.id();
        }
    }
}
