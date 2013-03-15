package org.siemac.metamac.srm.core.criteria;

public enum OrganisationSchemeVersionMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, PROC_STATUS, TYPE, IS_LAST_VERSION, LATEST_FINAL, LATEST_PUBLIC;

    public String value() {
        return name();
    }
    public static OrganisationSchemeVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
