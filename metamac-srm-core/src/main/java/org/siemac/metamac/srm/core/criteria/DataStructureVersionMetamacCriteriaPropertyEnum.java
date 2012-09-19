package org.siemac.metamac.srm.core.criteria;

public enum DataStructureVersionMetamacCriteriaPropertyEnum {

    CODE, URN, PROC_STATUS, IS_LAST_VERSION;

    public String value() {
        return name();
    }
    public static DataStructureVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
