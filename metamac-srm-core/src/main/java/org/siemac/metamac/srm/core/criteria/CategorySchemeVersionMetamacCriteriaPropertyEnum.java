package org.siemac.metamac.srm.core.criteria;

public enum CategorySchemeVersionMetamacCriteriaPropertyEnum {

    CODE,
    URN,
    NAME,
    DESCRIPTION,
    MAINTAINER_URN,
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
    public static CategorySchemeVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
