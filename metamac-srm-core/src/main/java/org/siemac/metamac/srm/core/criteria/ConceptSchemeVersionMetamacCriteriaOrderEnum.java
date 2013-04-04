package org.siemac.metamac.srm.core.criteria;

public enum ConceptSchemeVersionMetamacCriteriaOrderEnum {

    CODE, URN, PROC_STATUS, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static ConceptSchemeVersionMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
