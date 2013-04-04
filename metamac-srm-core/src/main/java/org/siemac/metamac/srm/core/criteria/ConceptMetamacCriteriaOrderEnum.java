package org.siemac.metamac.srm.core.criteria;

public enum ConceptMetamacCriteriaOrderEnum {

    CODE, URN, CONCEPT_SCHEME_URN, CONCEPT_SCHEME_CODE, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static ConceptMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
