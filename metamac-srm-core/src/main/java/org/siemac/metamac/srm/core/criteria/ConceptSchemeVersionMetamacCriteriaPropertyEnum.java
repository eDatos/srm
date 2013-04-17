package org.siemac.metamac.srm.core.criteria;

public enum ConceptSchemeVersionMetamacCriteriaPropertyEnum {

    CODE,
    URN,
    NAME,
    DESCRIPTION,
    MAINTAINER_URN,
    CONCEPT_SCHEME_TYPE,
    STATISTICAL_OPERATION_URN,
    PROC_STATUS,
    IS_LAST_VERSION,
    LATEST_FINAL,
    LATEST_PUBLIC,
    INTERNAL_PUBLICATION_DATE,
    INTERNAL_PUBLICATION_USER,
    EXTERNAL_PUBLICATION_DATE,
    EXTERNAL_PUBLICATION_USER;

    public String value() {
        return name();
    }
    public static ConceptSchemeVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
