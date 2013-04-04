package org.siemac.metamac.srm.core.criteria;

public enum CategoryMetamacCriteriaOrderEnum {

    CODE, URN, CATEGORY_SCHEME_URN, CATEGORY_SCHEME_CODE, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static CategoryMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
