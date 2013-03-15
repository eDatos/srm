package org.siemac.metamac.srm.core.criteria;

public enum CodelistVersionMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, PROC_STATUS, IS_LAST_VERSION, CODELIST_FAMILY_URN, LATEST_FINAL, LATEST_PUBLIC;

    public String value() {
        return name();
    }
    public static CodelistVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
