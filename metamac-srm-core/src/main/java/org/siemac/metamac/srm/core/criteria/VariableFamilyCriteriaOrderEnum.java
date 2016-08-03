package org.siemac.metamac.srm.core.criteria;

public enum VariableFamilyCriteriaOrderEnum {

    CODE, URN, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static VariableFamilyCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
