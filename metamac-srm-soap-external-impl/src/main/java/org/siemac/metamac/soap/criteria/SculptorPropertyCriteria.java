package org.siemac.metamac.soap.criteria;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.soap.common.v1_0.domain.OperationType;

public class SculptorPropertyCriteria extends SculptorPropertyCriteriaBase {

    private final Property<?>   property;
    private final Object        value;
    private final OperationType operationType;

    public SculptorPropertyCriteria(Property<?> property, Object value, OperationType operationType) {
        this.property = property;
        this.value = value;
        this.operationType = operationType;
    }

    public Property<?> getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    public OperationType getOperationType() {
        return operationType;
    }
}