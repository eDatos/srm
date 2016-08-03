package org.siemac.metamac.srm.core.criteria;

public enum VariableFamilyCriteriaPropertyEnum {

    CODE, NAME;

    public String value() {
        return name();
    }
    public static VariableFamilyCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}
