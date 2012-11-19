package org.siemac.metamac.srm.core.criteria;

public enum CodelistVersionMetamacCriteriaOrderEnum {

    CODE, URN, NAME, PROC_STATUS, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static CodelistVersionMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
