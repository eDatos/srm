package org.siemac.metamac.srm.core.common.error;

import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType {

    // Common
    public static final CommonServiceExceptionType CONFIGURATION_PROPERTY_NOT_FOUND                    = create("exception.srm.configuration.property_not_found");
    public static final CommonServiceExceptionType LIFE_CYCLE_WRONG_PROC_STATUS                        = create("exception.srm.life_cycle.wrong_proc_status");
    public static final CommonServiceExceptionType ITEM_SCHEME_WITHOUT_ITEMS                           = create("exception.srm.item_scheme.without_items");
    public static final CommonServiceExceptionType ITEM_WITH_INCORRECT_METADATA                        = create("exception.srm.item_scheme.item_with_incorrect_metadata");

    // Concepts
    public static final CommonServiceExceptionType CONCEPT_SCHEME_WRONG_TYPE                           = create("exception.srm.concepts.concept_scheme.wrong_type");
    public static final CommonServiceExceptionType CONCEPT_TYPE_NOT_FOUND                              = create("exception.srm.concepts.concept_type.not_found");

    // Organisations
    public static final CommonServiceExceptionType ORGANISATION_SCHEME_WRONG_TYPE                      = create("exception.srm.organisations.organisation_scheme.wrong_type");
    public static final CommonServiceExceptionType MAINTAINER_MUST_BE_DEFAULT                          = create("exception.srm.organisations.maintainer.not_default");

    // Codelists
    public static final CommonServiceExceptionType VARIABLE_ONLY_IN_ONE_FAMILY                         = create("exception.srm.codelists.variable.variable_only_one_family");
    public static final CommonServiceExceptionType VARIABLE_WITH_RELATIONS                             = create("exception.srm.codelists.variable.with_relations");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_WITH_RELATIONS                     = create("exception.srm.codelists.variable_element.with_relations");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_WITH_OPERATIONS                    = create("exception.srm.codelists.variable_element.with_operations");
    public static final CommonServiceExceptionType CODELIST_ALPHABETICAL_ORDER_OPERATION_NOT_SUPPORTED = create("exception.srm.codelists.order_visualisation.alphabetical.operation_not_supported");

    // Variable elements
    public static final CommonServiceExceptionType VARIABLE_ELEMENTS_MUST_BELONG_TO_SAME_VARIABLE      = create("exception.srm.codelists.variable_element.must_belong_to_same_variable");
    public static final CommonServiceExceptionType VARIABLE_ELEMENT_OPERATION_NOT_FOUND                = create("exception.srm.codelists.variable_element.operation.not_found");

    // Codes miscellaneous
    public static final CommonServiceExceptionType ARTEFACT_CAN_NOT_REPLACE_ITSELF                     = create("exception.srm.codelists.artefact.cannot_replace_itself");
    public static final CommonServiceExceptionType ARTEFACT_IS_ALREADY_REPLACED                        = create("exception.srm.codelists.artefact.already_replaced");
}
