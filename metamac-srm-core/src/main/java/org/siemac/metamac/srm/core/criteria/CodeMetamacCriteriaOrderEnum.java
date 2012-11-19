package org.siemac.metamac.srm.core.criteria;

public enum CodeMetamacCriteriaOrderEnum {

    CODE, URN, NAME, CODELIST_URN, CODELIST_CODE;

    public String value() {
        return name();
    }

    public static CodeMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
