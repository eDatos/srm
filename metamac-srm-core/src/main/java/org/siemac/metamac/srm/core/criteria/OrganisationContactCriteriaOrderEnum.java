package org.siemac.metamac.srm.core.criteria;

public enum OrganisationContactCriteriaOrderEnum {

    CODE;

    public String value() {
        return name();
    }

    public static OrganisationContactCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
