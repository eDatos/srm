package org.siemac.metamac.srm.core.criteria;

public enum CodelistVersionMetamacCriteriaPropertyEnum {

    CODE,
    URN,
    NAME,
    DESCRIPTION,
    MAINTAINER_URN,
    PROC_STATUS,
    IS_LAST_VERSION,
    ACCESS_TYPE,
    DESCRIPTION_SOURCE,
    CODELIST_FAMILY_URN,
    LATEST_FINAL,
    LATEST_PUBLIC,
    INTERNAL_PUBLICATION_DATE,
    INTERNAL_PUBLICATION_USER,
    EXTERNAL_PUBLICATION_DATE,
    EXTERNAL_PUBLICATION_USER,
    VARIABLE,
    VARIABLE_FAMILY;

    public String value() {
        return name();
    }
    public static CodelistVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
