package org.siemac.metamac.srm.core.criteria;

public enum VariableCriteriaOrderEnum {

    CODE, URN, LAST_UPDATED, VARIABLE_FAMILY_URN;

    public String value() {
        return name();
    }

    public static VariableCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
