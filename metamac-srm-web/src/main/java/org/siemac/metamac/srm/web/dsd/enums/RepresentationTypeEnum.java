package org.siemac.metamac.internal.web.dsd.enums;

import com.smartgwt.client.types.ValueEnum;

public enum RepresentationTypeEnum implements ValueEnum {

    ENUMERATED("enumerated"), NON_NUMERATED("non-enumerated");

    private String value;

    RepresentationTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public String getName() {
        return name();
    }

}
