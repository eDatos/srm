package org.siemac.metamac.srm.core.criteria;

public enum CodeMetamacCriteriaOrderEnum {

    CODE, URN, CODELIST_URN, CODELIST_CODE, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static CodeMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
