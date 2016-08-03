package org.siemac.metamac.srm.core.criteria;

public enum OrganisationMetamacCriteriaPropertyEnum {

    CODE,
    URN,
    NAME,
    DESCRIPTION,
    TYPE,
    ORGANISATION_PARENT_URN,
    ORGANISATION_SCHEME_URN,
    ORGANISATION_SCHEME_TYPE,
    ORGANISATION_SCHEME_IS_LAST_VERSION,
    ORGANISATION_SCHEME_LATEST_FINAL,
    ORGANISATION_SCHEME_LATEST_PUBLIC;

    public String value() {
        return name();
    }
    public static OrganisationMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
