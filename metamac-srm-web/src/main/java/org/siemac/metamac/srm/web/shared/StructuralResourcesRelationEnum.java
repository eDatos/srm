package org.siemac.metamac.srm.web.shared;

import java.io.Serializable;

public enum StructuralResourcesRelationEnum implements Serializable {

    CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE, CONCEPTS_WITH_DSD_PRIMARY_MEASURE;

    private StructuralResourcesRelationEnum() {
    }

    public String getName() {
        return name();
    }
}
