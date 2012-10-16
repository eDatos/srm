package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    // Concepts
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_PROC_STATUS            = create("exception.srm.concepts.concept_scheme.wrong_proc_status");
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_TYPE                   = create("exception.srm.concepts.concept_scheme.wrong_type");
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WITHOUT_CONCEPTS             = create("exception.srm.concepts.concept_scheme.without_concepts");
    public static final CommonServiceExceptionType CONCEPT_TYPE_NOT_FOUND                      = create("exception.srm.concepts.concept_type.not_found");

    // Organisations
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WRONG_PROC_STATUS       = create("exception.srm.organisations.organisation_scheme.wrong_proc_status");
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WRONG_TYPE              = create("exception.srm.organisations.organisation_scheme.wrong_type");
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WITHOUT_ORGANISATIONS   = create("exception.srm.organisations.organisation_scheme.without_organisations");

    // Categories
    public static final CommonServiceExceptionType CATEGORY_SCHEME_WRONG_PROC_STATUS           = create("exception.srm.categories.category_scheme.wrong_proc_status");
    public static final CommonServiceExceptionType CATEGORY_SCHEME_WITHOUT_CATEGORIES          = create("exception.srm.categories.category_scheme.without_categories");

    // Data structure definition
    public static final CommonServiceExceptionType DATA_STRUCTURE_DEFINITION_WRONG_PROC_STATUS = create("exception.srm.dsds.data_structure_definition.wrong_proc_status");

    // Security
    public static final CommonServiceExceptionType SECURITY_ACTION_NOT_ALLOWED                 = create("exception.srm.security.action_not_allowed");
}
