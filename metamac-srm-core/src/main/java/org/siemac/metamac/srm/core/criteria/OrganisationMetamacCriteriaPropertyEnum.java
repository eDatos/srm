package org.siemac.metamac.srm.core.criteria;

public enum OrganisationMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, ORGANISATION_SCHEME_URN, ORGANISATION_SCHEME_TYPE, ORGANISATION_SCHEME_IS_LAST_VERSION;

    public String value() {
        return name();
    }
    public static OrganisationMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
