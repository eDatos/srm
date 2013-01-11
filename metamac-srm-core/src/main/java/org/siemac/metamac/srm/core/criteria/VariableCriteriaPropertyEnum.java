package org.siemac.metamac.srm.core.criteria;

public enum VariableCriteriaPropertyEnum {

    CODE, NAME, VARIABLE_FAMILY_URN;

    public String value() {
        return name();
    }
    public static VariableCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}
