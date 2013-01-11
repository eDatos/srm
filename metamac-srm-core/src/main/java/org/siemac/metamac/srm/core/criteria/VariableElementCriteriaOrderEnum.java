package org.siemac.metamac.srm.core.criteria;

public enum VariableElementCriteriaOrderEnum {

    CODE, URN, NAME, LAST_UPDATED, VARIABLE_URN;

    public String value() {
        return name();
    }

    public static VariableElementCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
