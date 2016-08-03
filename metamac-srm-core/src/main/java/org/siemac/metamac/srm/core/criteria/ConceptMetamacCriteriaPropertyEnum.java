package org.siemac.metamac.srm.core.criteria;

public enum ConceptMetamacCriteriaPropertyEnum {

    CODE,
    URN,
    NAME,
    ACRONYM,
    DESCRIPTION,
    DESCRIPTION_SOURCE,
    CONCEPT_PARENT_URN,
    CONCEPT_SCHEME_URN,
    CONCEPT_SCHEME_TYPE,
    CONCEPT_SCHEME_IS_LAST_VERSION,
    CONCEPT_SCHEME_LATEST_FINAL,
    CONCEPT_SCHEME_LATEST_PUBLIC;

    public String value() {
        return name();
    }
    public static ConceptMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
