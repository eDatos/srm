package org.siemac.metamac.srm.web.shared;

import java.io.Serializable;

public enum StructuralResourcesRelationEnum implements Serializable {

    CATEGORIES_FOR_CATEGORISATIONS,
    CATEGORY_SCHEMES_FOR_CATEGORISATIONS,
    CODE_WITH_QUANTITY_BASE_LOCATION,
    CODE_WITH_QUANTITY_UNIT,
    CODELIST_OPENNESS_LEVEL_FOR_DSD_DIMENSION,
    CODELIST_ORDER_FOR_DSD_DIMENSION,
    CODELIST_THAT_CAN_BE_REPLACED,
    CODELIST_WITH_CONCEPT_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_DSD_ATTRIBUTE_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_DSD_DIMENSION_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_DSD_PRIMARY_MEASURE_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_DSD_SPATIAL_ATTRIBUTE_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_DSD_SPATIAL_DIMENSION_ENUMERATED_REPRESENTATION,
    CODELIST_WITH_QUANTITY_BASE_LOCATION,
    CODELIST_WITH_QUANTITY_UNIT,
    CODELIST_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY,
    CODES_WITH_VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY,
    CONCEPT_SCHEME_WITH_CONCEPT_ENUMERATED_REPRESENTATION,
    CONCEPT_SCHEME_WITH_DSD_MEASURE_ATTRIBUTE_ENUMERATED_REPRESENTATION,
    CONCEPT_SCHEME_WITH_DSD_MEASURE_DIMENSION_ENUMERATED_REPRESENTATION,
    CONCEPT_SCHEME_WITH_QUANTITY_BASE_QUANTITY,
    CONCEPT_SCHEME_WITH_QUANTITY_DENOMINATOR,
    CONCEPT_SCHEME_WITH_QUANTITY_NUMERATOR,
    CONCEPT_SCHEMES_WITH_CONCEPT_EXTENDS,
    CONCEPT_SCHEMES_WITH_CONCEPT_ROLE,
    CONCEPT_SCHEMES_WITH_DSD_ATTRIBUTE,
    CONCEPT_SCHEMES_WITH_DSD_DIMENSION,
    CONCEPT_SCHEMES_WITH_DSD_MEASURE_DIMENSION,
    CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE,
    CONCEPT_SCHEMES_WITH_DSD_ROLES,
    CONCEPT_SCHEMES_WITH_DSD_TIME_DIMENSION,
    CONCEPT_SCHEMES_WITH_DSD_SPATIAL_DIMENSION,
    CONCEPT_WITH_CONCEPT_EXTENDS,
    CONCEPT_WITH_DSD_ATTRIBUTE,
    CONCEPT_WITH_DSD_DIMENSION,
    CONCEPT_WITH_DSD_MEASURE_DIMENSION,
    CONCEPT_WITH_DSD_PRIMARY_MEASURE,
    CONCEPT_WITH_DSD_TIME_DIMENSION,
    CONCEPT_WITH_DSD_SPATIAL_DIMENSION,
    CONCEPT_WITH_QUANTITY_BASE_QUANTITY,
    CONCEPT_WITH_QUANTITY_DENOMINATOR,
    CONCEPT_WITH_QUANTITY_NUMERATOR,
    CONCEPTS_WITH_CONCEPT_ROLE,
    CONCEPTS_WITH_DSD_ROLES,
    VARIABLE_ELEMENT_WITH_CODE;

    private StructuralResourcesRelationEnum() {
    }

    public String getName() {
        return name();
    }
}
