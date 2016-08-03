package org.siemac.metamac.srm.core.criteria;

public enum CodelistFamilyCriteriaPropertyEnum {

    CODE, NAME;

    public String value() {
        return name();
    }
    public static CodelistFamilyCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}
