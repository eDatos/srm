package org.siemac.metamac.srm.web.client.enums;

import java.io.Serializable;

public enum BooleanItemEnum implements Serializable {

    NULL_VALUE, TRUE_VALUE, FALSE_VALUE;

    private BooleanItemEnum() {
    }

    public String getName() {
        return name();
    }
}
