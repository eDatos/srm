package org.siemac.metamac.srm.web.shared;

import java.io.Serializable;

public enum StructuralResourcesRelationEnum implements Serializable {

    CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE,
    CONCEPTS_WITH_DSD_PRIMARY_MEASURE,
    CODELIST_WITH_DSD_PRIMARY_MEASURE_ENUMERATED_REPRESENTATION,
    CONCEPT_SCHEMES_WITH_DSD_DIMENSION,
    CONCEPT_WITH_DSD_DIMENSION,
    CONCEPT_SCHEMES_WITH_DSD_TIME_DIMENSION,
    CONCEPT_WITH_DSD_TIME_DIMENSION,
    CONCEPT_SCHEMES_WITH_DSD_MEASURE_DIMENSION,
    CONCEPT_WITH_DSD_MEASURE_DIMENSION,
    CONCEPT_SCHEME_WITH_DSD_MEASURE_DIMENSION_ENUMERATED_REPRESENTATION;

    private StructuralResourcesRelationEnum() {
    }

    public String getName() {
        return name();
    }
}
