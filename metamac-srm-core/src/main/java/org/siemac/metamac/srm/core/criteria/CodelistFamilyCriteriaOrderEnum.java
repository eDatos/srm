package org.siemac.metamac.srm.core.criteria;

public enum CodelistFamilyCriteriaOrderEnum {

    CODE, URN, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static CodelistFamilyCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
