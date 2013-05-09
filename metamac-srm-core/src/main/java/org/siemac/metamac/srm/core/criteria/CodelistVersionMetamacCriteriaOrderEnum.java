package org.siemac.metamac.srm.core.criteria;

public enum CodelistVersionMetamacCriteriaOrderEnum {

    CODE, URN, PROC_STATUS, RESOURCE_CREATED_DATE, RESOURCE_LAST_UPDATED, LAST_UPDATED, CODELIST_FAMILY_URN;

    public String value() {
        return name();
    }

    public static CodelistVersionMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
