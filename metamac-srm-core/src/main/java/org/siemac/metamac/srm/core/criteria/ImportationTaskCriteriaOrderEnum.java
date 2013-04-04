package org.siemac.metamac.srm.core.criteria;

public enum ImportationTaskCriteriaOrderEnum {

    STATUS;

    public String value() {
        return name();
    }

    public static ImportationTaskCriteriaOrderEnum fromValue(String v) {
        return valueOf(v);
    }

}
