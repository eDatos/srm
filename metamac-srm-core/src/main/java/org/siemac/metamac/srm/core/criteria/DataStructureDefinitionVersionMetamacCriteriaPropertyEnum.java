package org.siemac.metamac.srm.core.criteria;

public enum DataStructureDefinitionVersionMetamacCriteriaPropertyEnum {

    CODE, URN, NAME, PROC_STATUS, IS_LAST_VERSION;

    public String value() {
        return name();
    }

    public static DataStructureDefinitionVersionMetamacCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }

}
