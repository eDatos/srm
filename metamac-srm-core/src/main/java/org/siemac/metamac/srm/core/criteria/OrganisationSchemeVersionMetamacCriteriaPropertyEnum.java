package org.siemac.metamac.srm.core.criteria;

public enum OrganisationSchemeVersionMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, PROC_STATUS, IS_LAST_VERSION;

    public String value() {
        return name();
    }
    public static OrganisationSchemeVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
