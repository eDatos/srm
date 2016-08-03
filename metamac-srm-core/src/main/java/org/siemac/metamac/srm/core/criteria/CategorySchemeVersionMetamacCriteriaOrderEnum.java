package org.siemac.metamac.srm.core.criteria;

public enum CategorySchemeVersionMetamacCriteriaOrderEnum {

    CODE, URN, PROC_STATUS, RESOURCE_CREATED_DATE, RESOURCE_LAST_UPDATED, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static CategorySchemeVersionMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
