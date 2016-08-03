package org.siemac.metamac.srm.web.concept.enums;

public enum QuantityIndexBaseTypeEnum {

    BASE_VALUE, BASE_TIME, BASE_LOCATION;

    private QuantityIndexBaseTypeEnum() {
    }

    public String getName() {
        return name();
    }

}
