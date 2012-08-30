package org.siemac.metamac.srm.core.criteria;

public enum ConceptSchemeVersionMetamacCriteriaPropertyEnum {

    CODE, URN, PROC_STATUS, IS_LAST_VERSION;

    public String value() {
        return name();
    }
    public static ConceptSchemeVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
