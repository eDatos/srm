package org.siemac.metamac.srm.core.mapper;

import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.constants.CoreCommonConstants;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria;
import org.siemac.metamac.core.common.criteria.mapper.MetamacCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.DataStructureDefinitionCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinition;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionProperties;
import org.springframework.stereotype.Component;

@Component
public class MetamacCriteria2SculptorCriteriaMapperImpl implements MetamacCriteria2SculptorCriteriaMapper {

    private MetamacCriteria2SculptorCriteria<DataStructureDefinition> dataStructureDefinitionMapper = null;

    public MetamacCriteria2SculptorCriteriaMapperImpl() throws MetamacException {
        dataStructureDefinitionMapper = new MetamacCriteria2SculptorCriteria<DataStructureDefinition>(DataStructureDefinition.class, DataStructureDefinitionCriteriaOrderEnum.class,
                DataStructureDefinitionCriteriaPropertyEnum.class, new DataStructureDefinitionCriteriaCallback());
    }

    @Override
    public MetamacCriteria2SculptorCriteria<DataStructureDefinition> getDataStructureDefinitionCriteriaMapper() {
        return dataStructureDefinitionMapper;
    }

    private class DataStructureDefinitionCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws MetamacException {
            DataStructureDefinitionCriteriaPropertyEnum propertyNameCriteria = DataStructureDefinitionCriteriaPropertyEnum.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return new SculptorPropertyCriteria(DataStructureDefinitionProperties.code(), propertyRestriction.getStringValue());

                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, propertyRestriction.getPropertyName());
            }
        }

        @Override
        public Property<DataStructureDefinition> retrievePropertyOrder(MetamacCriteriaOrder order) throws MetamacException {
            DataStructureDefinitionCriteriaOrderEnum propertyNameCriteria = DataStructureDefinitionCriteriaOrderEnum.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case CODE:
                    return DataStructureDefinitionProperties.code();
                case NAME:
                    return DataStructureDefinitionProperties.name().texts().label();
                case LAST_UPDATED:
                    return new LeafProperty<DataStructureDefinition>(DataStructureDefinitionProperties.lastUpdated().getName(), CoreCommonConstants.CRITERIA_DATETIME_COLUMN_OFFSET, true,
                            DataStructureDefinition.class);
                default:
                    throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, order.getPropertyName());
            }
        }

        @Override
        public Property<DataStructureDefinition> retrievePropertyOrderDefault() throws MetamacException {
            return DataStructureDefinitionProperties.id();
        }

    }

}
