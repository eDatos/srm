package org.siemac.metamac.srm.core.criteria;

public enum DataStructureDefinitionCriteriaPropertyEnum {

    CODE;

    public String value() {
        return name();
    }
    public static DataStructureDefinitionCriteriaPropertyEnum fromValue(String v) {
        return valueOf(v);
    }
}