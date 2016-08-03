package org.siemac.metamac.srm.web.dsd.enums;

import java.io.Serializable;

public enum DsdTabTypeEnum implements Serializable {
    DSD_GENERAL, PRIMARY_MEASURE, DIMENSIONS, ATTRIBUTES, GROUP_KEYS, CATEGORISATIONS;

    private DsdTabTypeEnum() {
    }

    public String getName() {
        return name();
    }
}
