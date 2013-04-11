package org.siemac.metamac.srm.core.criteria;

public enum TaskCriteriaOrderEnum {

    STATUS;

    public String value() {
        return name();
    }

    public static TaskCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
