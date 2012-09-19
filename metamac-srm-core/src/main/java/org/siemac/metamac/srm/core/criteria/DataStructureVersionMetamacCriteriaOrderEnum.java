package org.siemac.metamac.srm.core.criteria;

public enum DataStructureVersionMetamacCriteriaOrderEnum {

    CODE, URN, NAME, PROC_STATUS, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static DataStructureVersionMetamacCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
