package org.siemac.metamac.srm.core.criteria;

public enum VariableElementCriteriaPropertyEnum {

    CODE, URN, SHORT_NAME, VARIABLE_URN;

    public String value() {
        return name();
    }
    public static VariableElementCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}
