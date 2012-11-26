package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    // Common
    public static final CommonServiceExceptionType LIFE_CYCLE_WRONG_PROC_STATUS           = create("exception.srm.life_cycle.wrong_proc_status");
    public static final CommonServiceExceptionType ITEM_SCHEME_WITHOUT_ITEMS              = create("exception.srm.item_scheme.without_items");

    // Concepts
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_TYPE              = create("exception.srm.concepts.concept_scheme.wrong_type");
    public static final CommonServiceExceptionType CONCEPT_TYPE_NOT_FOUND                 = create("exception.srm.concepts.concept_type.not_found");

    // Organisations
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WRONG_TYPE         = create("exception.srm.organisations.organisation_scheme.wrong_type");

    // Codes
    public static final CommonServiceExceptionType CODELIST_FAMILY_NOT_FOUND              = create("exception.srm.codes.codelist_family.not_found");
    public static final CommonServiceExceptionType CODELIST_FAMILY_DUPLICATED_IDENTIFIER  = create("exception.srm.codes.codelist_family.duplicated_code");
    public static final CommonServiceExceptionType VARIABLE_FAMILY_NOT_FOUND              = create("exception.srm.codes.variable_family.not_found");
    public static final CommonServiceExceptionType VARIABLE_FAMILY_DUPLICATED_IDENTIFIER  = create("exception.srm.codes.variable_family.duplicated_code");
    public static final CommonServiceExceptionType VARIABLE_NOT_FOUND                     = create("exception.srm.codes.variable.not_found");
    public static final CommonServiceExceptionType VARIABLE_DUPLICATED_IDENTIFIER         = create("exception.srm.codes.variable.duplicated_code");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_DUPLICATED_IDENTIFIER = create("exception.srm.codes.variable_element.duplicated_code");
}
