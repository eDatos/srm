package org.siemac.metamac.srm.core.criteria;

public enum CategoryMetamacCriteriaOrderEnum {

    CODE, URN, NAME, CATEGORY_SCHEME_URN, CATEGORY_SCHEME_CODE;

    public String value() {
        return name();
    }

    public static CategoryMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
