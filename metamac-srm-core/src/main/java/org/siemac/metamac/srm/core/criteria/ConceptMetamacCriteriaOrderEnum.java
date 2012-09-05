package org.siemac.metamac.srm.core.criteria;

public enum ConceptMetamacCriteriaOrderEnum {

    CODE, URN, NAME, CONCEPT_SCHEME_URN;

    public String value() {
        return name();
    }

    public static ConceptMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
