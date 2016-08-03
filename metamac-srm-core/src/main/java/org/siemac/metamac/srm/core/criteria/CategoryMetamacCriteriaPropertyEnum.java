package org.siemac.metamac.srm.core.criteria;

public enum CategoryMetamacCriteriaPropertyEnum {

    CODE,
    URN,
    NAME,
    DESCRIPTION,
    CATEGORY_PARENT_URN,
    CATEGORY_SCHEME_URN,
    CATEGORY_SCHEME_IS_LAST_VERSION,
    CATEGORY_SCHEME_LATEST_FINAL,
    CATEGORY_SCHEME_LATEST_PUBLIC,
    CATEGORY_SCHEME_EXTERNALLY_PUBLISHED;

    public String value() {
        return name();
    }
    public static CategoryMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}