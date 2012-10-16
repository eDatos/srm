package org.siemac.metamac.srm.core.criteria;

public enum CategorySchemeVersionMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, PROC_STATUS, IS_LAST_VERSION;

    public String value() {
        return name();
    }
    public static CategorySchemeVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
