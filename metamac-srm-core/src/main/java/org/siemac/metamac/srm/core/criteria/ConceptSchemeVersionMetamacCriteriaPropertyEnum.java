package org.siemac.metamac.srm.core.criteria;

public enum ConceptSchemeVersionMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, PROC_STATUS, IS_LAST_VERSION, LATEST_FINAL, LATEST_PUBLIC, EXTERNAL_PUBLICATION_DATE;

    public String value() {
        return name();
    }
    public static ConceptSchemeVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
