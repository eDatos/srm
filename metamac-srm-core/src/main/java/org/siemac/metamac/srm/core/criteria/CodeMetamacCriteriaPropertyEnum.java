package org.siemac.metamac.srm.core.criteria;

public enum CodeMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, SHORT_NAME, DESCRIPTION, CODE_PARENT_URN, CODELIST_URN, CODELIST_IS_LAST_VERSION;

    public String value() {
        return name();
    }
    public static CodeMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}