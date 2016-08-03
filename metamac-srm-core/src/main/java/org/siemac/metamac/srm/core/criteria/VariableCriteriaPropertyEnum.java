package org.siemac.metamac.srm.core.criteria;

public enum VariableCriteriaPropertyEnum {

    CODE, NAME, VARIABLE_FAMILY_URN, SHORT_NAME, URN, GEOGRAPHICAL;

    public String value() {
        return name();
    }
    public static VariableCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}
