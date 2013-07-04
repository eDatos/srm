package org.siemac.metamac.srm.web.shared;

import java.io.Serializable;

public enum ImportableResourceTypeEnum implements Serializable {
    SDMX_STRUCTURE, CODES, VARIABLE_ELEMENTS, CODES_ORDER, VARIABLE_ELEMENT_SHAPE;

    private ImportableResourceTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
