package org.siemac.metamac.srm.web.shared;

import java.io.Serializable;

public enum StructuralResourcesRelationEnum implements Serializable {

    CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE,
    CONCEPT_WITH_DSD_PRIMARY_MEASURE,
    CODELIST_WITH_DSD_PRIMARY_MEASURE_ENUMERATED_REPRESENTATION,
    CONCEPT_SCHEMES_WITH_DSD_DIMENSION,
    CONCEPT_WITH_DSD_DIMENSION,
    CONCEPT_SCHEMES_WITH_DSD_TIME_DIMENSION,
    CONCEPT_WITH_DSD_TIME_DIMENSION,
    CONCEPT_SCHEMES_WITH_DSD_MEASURE_DIMENSION,
    CONCEPT_WITH_DSD_MEASURE_DIMENSION,
    CONCEPT_SCHEME_WITH_DSD_MEASURE_DIMENSION_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_DSD_DIMENSION_ENUMERATED_REPRESENTATION,
    CONCEPT_SCHEMES_WITH_DSD_ROLES,
    CONCEPTS_WITH_DSD_ROLES,
    CONCEPT_SCHEMES_WITH_DSD_ATTRIBUTE,
    CONCEPT_WITH_DSD_ATTRIBUTE,
    CODELIST_WITH_DSD_ATTRIBUTE_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_CONCEPT_ENUMERATED_REPRESENTATION,
    VARIABLE_ELEMENT_WITH_CODE,
    CODELIST_ORDER_FOR_DSD_DIMENSION,
    CODELIST_OPENNESS_LEVEL_FOR_DSD_DIMENSION;

    private StructuralResourcesRelationEnum() {
    }

    public String getName() {
        return name();
    }
}
