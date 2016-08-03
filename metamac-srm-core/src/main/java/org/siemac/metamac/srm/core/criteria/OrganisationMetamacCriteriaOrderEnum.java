package org.siemac.metamac.srm.core.criteria;

public enum OrganisationMetamacCriteriaOrderEnum {

    CODE, URN, ORGANISATION_SCHEME_URN, ORGANISATION_SCHEME_CODE, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static OrganisationMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
