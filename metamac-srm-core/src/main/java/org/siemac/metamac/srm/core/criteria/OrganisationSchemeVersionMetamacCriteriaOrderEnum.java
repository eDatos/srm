package org.siemac.metamac.srm.core.criteria;

public enum OrganisationSchemeVersionMetamacCriteriaOrderEnum {

    CODE, URN, PROC_STATUS, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static OrganisationSchemeVersionMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}
