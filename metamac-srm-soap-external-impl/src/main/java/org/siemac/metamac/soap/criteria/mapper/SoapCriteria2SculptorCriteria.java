package org.siemac.metamac.soap.criteria.mapper;

import java.util.List; 

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.ConditionRoot;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.core.common.criteria.SculptorCriteria;
import org.siemac.metamac.core.common.criteria.SculptorPropertyCriteria;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaOrder;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteriaRestriction;
import org.siemac.metamac.soap.common.v1_0.domain.OrderType;
import org.siemac.metamac.soap.exception.SoapCommonServiceExceptionType;
import org.siemac.metamac.soap.exception.SoapExceptionUtils;
import org.siemac.metamac.soap.structural_resources.v1_0.ExceptionFault;
import org.siemac.metamac.srm.soap.external.exception.SoapExceptionParameters;

// // TODO put in common library if more soap services are created
@SuppressWarnings({"unchecked", "rawtypes"})
public class SoapCriteria2SculptorCriteria<T> {

    private Class<T>              entityClass                  = null;

    private Class<? extends Enum> propertyOrderEnumClass       = null;
    private Class<? extends Enum> propertyRestrictionEnumClass = null;

    private CriteriaCallback      callback                     = null;

    private final Integer         MAXIMUM_RESULT_SIZE_DEFAULT  = Integer.valueOf(25);
    private final Integer         MAXIMUM_RESULT_SIZE_ALLOWED  = Integer.valueOf(1000);

    public SoapCriteria2SculptorCriteria(Class<T> entityClass, Class<? extends Enum> propertyOrderEnumClass, Class<? extends Enum> propertyRestrictionEnumClass, CriteriaCallback callback)
            throws ExceptionFault {

        this.entityClass = entityClass;
        this.propertyOrderEnumClass = propertyOrderEnumClass;
        this.propertyRestrictionEnumClass = propertyRestrictionEnumClass;
        this.callback = callback;
    }

    public SculptorCriteria metamacCriteria2SculptorCriteria(MetamacCriteria metamacCriteria) throws ExceptionFault {

        if (metamacCriteria == null) {
            metamacCriteria = new MetamacCriteria();
        }

        ConditionRoot<T> criteria = ConditionalCriteriaBuilder.criteriaFor(entityClass);

        // Orders
        if (metamacCriteria.getOrdersBy() == null) {
            Property defaultOrder = callback.retrievePropertyOrderDefault();
            if (defaultOrder != null) {
                criteria.orderBy(defaultOrder).ascending();
            }
        } else {
            for (MetamacCriteriaOrder order : metamacCriteria.getOrdersBy().getOrders()) {
                checkPropertyOrder(order);
                if (order.getType() == null || OrderType.ASC.equals(order.getType())) {
                    criteria.orderBy(callback.retrievePropertyOrder(order)).ascending();
                } else {
                    criteria.orderBy(callback.retrievePropertyOrder(order)).descending();
                }
            }
        }

        // Restrictions
        if (metamacCriteria.getRestriction() != null) {
            addRestriction(metamacCriteria.getRestriction(), criteria);
        }

        List<ConditionalCriteria> conditions = criteria.distinctRoot().build();

        // Paging parameter
        Integer startRow = null;
        Integer maximumResultSize = null;
        Boolean countTotalResults = null;
        if (metamacCriteria != null) {
            if (metamacCriteria.getFirstResult() != null) {
                startRow = metamacCriteria.getFirstResult().intValue();
            }
            if (metamacCriteria.getMaximumResultSize() != null) {
                maximumResultSize = metamacCriteria.getMaximumResultSize().intValue();
            }
            countTotalResults = metamacCriteria.getCountTotalResults();
        }
        if (startRow == null || startRow < 0) {
            startRow = Integer.valueOf(0);
        }
        if (maximumResultSize == null) {
            maximumResultSize = MAXIMUM_RESULT_SIZE_DEFAULT;
        }
        if (maximumResultSize > MAXIMUM_RESULT_SIZE_ALLOWED) {
            maximumResultSize = MAXIMUM_RESULT_SIZE_ALLOWED;
        }
        if (countTotalResults == null) {
            countTotalResults = Boolean.FALSE;
        }
        PagingParameter pagingParameter = PagingParameter.rowAccess(startRow, startRow + maximumResultSize, countTotalResults);

        return new SculptorCriteria(conditions, pagingParameter, maximumResultSize);
    }

    private void addRestriction(MetamacCriteriaRestriction metamacCriteriaRestriction, ConditionRoot criteria) throws ExceptionFault {

        if (metamacCriteriaRestriction instanceof MetamacCriteriaDisjunctionRestriction) {
            addRestriction((MetamacCriteriaDisjunctionRestriction) metamacCriteriaRestriction, criteria);
        } else if (metamacCriteriaRestriction instanceof MetamacCriteriaConjunctionRestriction) {
            addRestriction((MetamacCriteriaConjunctionRestriction) metamacCriteriaRestriction, criteria);
        } else if (metamacCriteriaRestriction instanceof MetamacCriteriaPropertyRestriction) {
            addRestriction((MetamacCriteriaPropertyRestriction) metamacCriteriaRestriction, criteria);
        } else {
            throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA);
        }
    }

    private void addRestriction(MetamacCriteriaDisjunctionRestriction disjunction, ConditionRoot criteria) throws ExceptionFault {
        criteria.lbrace();
        for (int i = 0; i < disjunction.getRestrictions().getRestrictions().size(); i++) {
            MetamacCriteriaRestriction metamacCriteriaSubrestriction = disjunction.getRestrictions().getRestrictions().get(i);
            addRestriction(metamacCriteriaSubrestriction, criteria);
            if (i < disjunction.getRestrictions().getRestrictions().size() - 1) {
                criteria.or();
            }
        }
        criteria.rbrace();
    }

    private void addRestriction(MetamacCriteriaConjunctionRestriction conjunction, ConditionRoot criteria) throws ExceptionFault {
        criteria.lbrace();
        for (int i = 0; i < conjunction.getRestrictions().getRestrictions().size(); i++) {
            MetamacCriteriaRestriction metamacCriteriaSubrestriction = conjunction.getRestrictions().getRestrictions().get(i);
            addRestriction(metamacCriteriaSubrestriction, criteria);
            if (i < conjunction.getRestrictions().getRestrictions().size() - 1) {
                criteria.and();
            }
        }
        criteria.rbrace();
    }

    private void addRestriction(MetamacCriteriaPropertyRestriction metamacCriteriaPropertyRestriction, ConditionRoot criteria) throws ExceptionFault {
        checkPropertyRestriction(metamacCriteriaPropertyRestriction);

        SculptorPropertyCriteria sculptorPropertyCriteria = callback.retrieveProperty(metamacCriteriaPropertyRestriction);
        addRestrictionWithProperty(metamacCriteriaPropertyRestriction, criteria, sculptorPropertyCriteria.getProperty(), sculptorPropertyCriteria.getValue());
    }

    private void addRestrictionWithProperty(MetamacCriteriaPropertyRestriction metamacCriteriaPropertyRestriction, ConditionRoot criteria, Property property, Object value) throws ExceptionFault {
        switch (metamacCriteriaPropertyRestriction.getOperation()) {
            case EQ:
                criteria.withProperty(property).eq(value);
                break;
            case IEQ:
                criteria.withProperty(property).ignoreCaseEq(value);
                break;
            case LIKE:
                criteria.withProperty(property).like("%" + value + "%");
                break;
            case ILIKE:
                criteria.withProperty(property).ignoreCaseLike("%" + value + "%");
                break;
            case NE:
                criteria.not().withProperty(property).eq(value);
                break;
            case LT:
                criteria.withProperty(property).lessThan(value);
                break;
            case LE:
                criteria.withProperty(property).lessThanOrEqual(value);
                break;
            case GT:
                criteria.withProperty(property).greaterThan(value);
                break;
            case GE:
                criteria.withProperty(property).greaterThanOrEqual(value);
                break;
            case IS_NULL:
                criteria.withProperty(property).isNull();
                break;
            case IS_NOT_NULL:
                criteria.withProperty(property).isNotNull();
                break;
            default:
                throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_OPERATION);
        }
    }

    private void checkPropertyOrder(MetamacCriteriaOrder order) throws ExceptionFault {
        try {
            Enum.valueOf(propertyOrderEnumClass, order.getPropertyName());
        } catch (Throwable e) {
            throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_ORDER);
        }
    }

    private void checkPropertyRestriction(MetamacCriteriaPropertyRestriction propertyRestriction) throws ExceptionFault {
        try {
            Enum.valueOf(propertyRestrictionEnumClass, propertyRestriction.getPropertyName());
        } catch (Throwable e) {
            throw SoapExceptionUtils.buildExceptionFault(SoapCommonServiceExceptionType.PARAMETER_INCORRECT, SoapExceptionParameters.CRITERIA_PROPERTY_NAME);
        }
    }

    public static interface CriteriaCallback {

        public SculptorPropertyCriteria retrieveProperty(MetamacCriteriaPropertyRestriction propertyRestriction) throws ExceptionFault;
        public Property retrievePropertyOrder(MetamacCriteriaOrder order) throws ExceptionFault;
        public Property retrievePropertyOrderDefault() throws ExceptionFault;
    }
}
