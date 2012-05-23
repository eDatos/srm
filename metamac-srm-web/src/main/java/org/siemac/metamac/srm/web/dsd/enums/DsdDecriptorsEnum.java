package org.siemac.metamac.srm.web.dsd.enums;

import com.smartgwt.client.types.ValueEnum;

public enum DsdDecriptorsEnum implements ValueEnum {

    GENERAL("general-descriptor"), DIMENSIONS("dimensions-descriptor"), PRIMARY_MEASURE("primarymeasure-descriptor"), GROUP_KEYS("groupkeys-descriptor"), ATTRIBUTES("attributes-descriptor");

    private String value;

    DsdDecriptorsEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
