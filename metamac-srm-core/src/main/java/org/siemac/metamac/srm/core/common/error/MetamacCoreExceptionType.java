package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class MetamacCoreExceptionType extends CommonServiceExceptionType {

    // Search
    public static final CommonServiceExceptionType MTM_CORE_SEARCH_NOT_FOUND                      = create("exception.core.search.not_found");

    // Validation
    public static final CommonServiceExceptionType MTM_CORE_VALIDATION_CONSTRAINT_ENUMERATED      = create("exception.core.validation.constraint.enumerated");
    public static final CommonServiceExceptionType MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MAX = create("exception.core.validation.constraint.cardinality_max");
    public static final CommonServiceExceptionType MTM_CORE_VALIDATION_CONSTRAINT_CARDINALITY_MIN = create("exception.core.validation.constraint.cardinality_min");
    public static final CommonServiceExceptionType MTM_CORE_VALIDATION_FACETTYPE_PROHIBITED       = create("exception.core.validation.facettype_prohibited");

    // Concept Schemes
    public static final CommonServiceExceptionType CONCEPT_SCHEME_NOT_FOUND                       = create("exception.core.concepts.concept_scheme.not_found");

}
