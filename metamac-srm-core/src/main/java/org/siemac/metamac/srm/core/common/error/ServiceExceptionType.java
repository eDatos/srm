package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {

    // Search
    public static final CommonServiceExceptionType SRM_SEARCH_NOT_FOUND                              = create("exception.srm.search.not_found");

    // Validation
    public static final CommonServiceExceptionType SRM_VALIDATION_CONSTRAINT_ENUMERATED              = create("exception.srm.validation.constraint.enumerated");
    public static final CommonServiceExceptionType SRM_VALIDATION_CONSTRAINT_CARDINALITY_MAX         = create("exception.srm.validation.constraint.cardinality_max");
    public static final CommonServiceExceptionType SRM_VALIDATION_CONSTRAINT_CARDINALITY_MIN         = create("exception.srm.validation.constraint.cardinality_min");
    public static final CommonServiceExceptionType SRM_VALIDATION_FACETTYPE_PROHIBITED               = create("exception.srm.validation.facettype_prohibited");
    public static final CommonServiceExceptionType SRM_VALIDATION_GROUP_DESCRIPTOR_UNABLE_UPDATE     = create("exception.srm.validation.group_descriptor.unable_update");
    public static final CommonServiceExceptionType SRM_VALIDATION_ATTRIBUTE_DESCRIPTOR_UNABLE_UPDATE = create("exception.srm.validation.attribute_descriptor.unable_update");

    // Concept Schemes
    public static final CommonServiceExceptionType CONCEPT_SCHEME_NOT_FOUND                          = create("exception.srm.concepts.concept_scheme.not_found");
    public static final CommonServiceExceptionType CONCEPT_SCHEME_ALREADY_EXIST_CODE_DUPLICATED      = create("exception.srm.concepts.concept_scheme.already_exist.code_duplicated");
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_PROC_STATUS                  = create("exception.srm.concepts.concept_scheme.wrong_proc_status");

}
