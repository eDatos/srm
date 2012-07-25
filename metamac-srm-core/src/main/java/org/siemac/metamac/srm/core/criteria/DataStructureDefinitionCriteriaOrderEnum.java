package org.siemac.metamac.srm.core.criteria;

public enum DataStructureDefinitionCriteriaOrderEnum {

    CODE;

    public String value() {
        return name();
    }

    public static DataStructureDefinitionCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }
}