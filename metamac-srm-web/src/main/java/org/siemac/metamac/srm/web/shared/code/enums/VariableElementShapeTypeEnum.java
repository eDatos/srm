package org.siemac.metamac.srm.web.shared.code.enums;

import java.io.Serializable;

public enum VariableElementShapeTypeEnum implements Serializable {
    POLYGON, POINT;

    private VariableElementShapeTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
