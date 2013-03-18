package org.siemac.metamac.srm.core.criteria;

public enum OrganisationContactCriteriaPropertyEnum {

    ORGANISATION_URN, NAME, ORGANISATION_UNIT, RESPONSIBILITY;

    public String value() {
        return name();
    }
    public static OrganisationContactCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}