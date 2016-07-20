package org.siemac.metamac.srm.core.criteria;

public enum VariableElementCriteriaPropertyEnum {

    // @formatter:off
    CODE, 
    URN, 
    SHORT_NAME, 
    VARIABLE_URN,
    IS_GEOGRAPHICAL_VARIABLE_ELEMENT,
    HAS_SHAPE,
    GEOGRAPHICAL_GRANULARITY,
    VALID_FROM_DATE,
    VALID_TO_DATE;
    // @formatter:on

    public String value() {
        return name();
    }
    public static VariableElementCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}
