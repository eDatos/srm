package org.siemac.metamac.srm.core.criteria;

public enum CategorySchemeVersionMetamacCriteriaOrderEnum {

    CODE, URN, PROC_STATUS, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static CategorySchemeVersionMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
