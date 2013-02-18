package org.siemac.metamac.srm.core.criteria;

public enum ImportDataCriteriaOrderEnum {

    STATUS;

    public String value() {
        return name();
    }

    public static ImportDataCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
