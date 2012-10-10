package org.siemac.metamac.srm.core.criteria;

public enum OrganisationMetamacCriteriaOrderEnum {

    CODE, URN, NAME, ORGANISATION_SCHEME_URN, ORGANISATION_SCHEME_CODE;

    public String value() {
        return name();
    }

    public static OrganisationMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
