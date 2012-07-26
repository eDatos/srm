package org.siemac.metamac.srm.core.criteria;

public enum DataStructureDefinitionCriteriaOrderEnum {

    CODE, NAME, LAST_UPDATED;

    public String value() {
        return name();
    }

    public static DataStructureDefinitionCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}