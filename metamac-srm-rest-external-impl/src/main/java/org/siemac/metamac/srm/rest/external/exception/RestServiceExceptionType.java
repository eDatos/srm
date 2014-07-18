package org.siemac.metamac.srm.rest.external.exception;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;

public class RestServiceExceptionType extends RestCommonServiceExceptionType {

    public static final RestCommonServiceExceptionType CONCEPT_SCHEME_NOT_FOUND                  = create("exception.structural_resources.concept_scheme.not_found");
    public static final RestCommonServiceExceptionType CONCEPT_NOT_FOUND                         = create("exception.structural_resources.concept.not_found");

    public static final RestCommonServiceExceptionType CATEGORY_SCHEME_NOT_FOUND                 = create("exception.structural_resources.category_scheme.not_found");
    public static final RestCommonServiceExceptionType CATEGORY_NOT_FOUND                        = create("exception.structural_resources.category.not_found");
    public static final RestCommonServiceExceptionType CATEGORISATION_NOT_FOUND                  = create("exception.structural_resources.categorisation.not_found");

    public static final RestCommonServiceExceptionType ORGANISATION_SCHEME_NOT_FOUND             = create("exception.structural_resources.organisation_scheme.not_found");
    public static final RestCommonServiceExceptionType ORGANISATION_NOT_FOUND                    = create("exception.structural_resources.organisation.not_found");

    public static final RestCommonServiceExceptionType AGENCY_SCHEME_NOT_FOUND                   = create("exception.structural_resources.agency_scheme.not_found");
    public static final RestCommonServiceExceptionType AGENCY_NOT_FOUND                          = create("exception.structural_resources.agency.not_found");

    public static final RestCommonServiceExceptionType ORGANISATION_UNIT_SCHEME_NOT_FOUND        = create("exception.structural_resources.organisation_unit_scheme.not_found");
    public static final RestCommonServiceExceptionType ORGANISATION_UNIT_NOT_FOUND               = create("exception.structural_resources.organisation_unit.not_found");

    public static final RestCommonServiceExceptionType DATA_PROVIDER_SCHEME_NOT_FOUND            = create("exception.structural_resources.data_provider_scheme.not_found");
    public static final RestCommonServiceExceptionType DATA_PROVIDER_NOT_FOUND                   = create("exception.structural_resources.data_provider.not_found");

    public static final RestCommonServiceExceptionType DATA_CONSUMER_SCHEME_NOT_FOUND            = create("exception.structural_resources.data_consumer_scheme.not_found");
    public static final RestCommonServiceExceptionType DATA_CONSUMER_NOT_FOUND                   = create("exception.structural_resources.data_consumer.not_found");

    public static final RestCommonServiceExceptionType CODELIST_NOT_FOUND                        = create("exception.structural_resources.codelist.not_found");
    public static final RestCommonServiceExceptionType CODELIST_ORDER_CONFIGURATION_NOT_FOUND    = create("exception.structural_resources.codelist_order_configuration.not_found");
    public static final RestCommonServiceExceptionType CODELIST_OPENNESS_CONFIGURATION_NOT_FOUND = create("exception.structural_resources.codelist_openness_configuration.not_found");
    public static final RestCommonServiceExceptionType CODE_NOT_FOUND                            = create("exception.structural_resources.code.not_found");
    public static final RestCommonServiceExceptionType VARIABLE_FAMILY_NOT_FOUND                 = create("exception.structural_resources.variable_family.not_found");
    public static final RestCommonServiceExceptionType VARIABLE_NOT_FOUND                        = create("exception.structural_resources.variable.not_found");
    public static final RestCommonServiceExceptionType VARIABLE_ELEMENT_NOT_FOUND                = create("exception.structural_resources.variable_element.not_found");
    public static final RestCommonServiceExceptionType CODELIST_FAMILY_NOT_FOUND                 = create("exception.structural_resources.codelist_family.not_found");

    public static final RestCommonServiceExceptionType DATA_STRUCTURE_NOT_FOUND                  = create("exception.structural_resources.data_structure.not_found");
    public static final RestCommonServiceExceptionType CONTENT_CONSTRAINT_NOT_FOUND              = create("exception.structural_resources.content_constraint.not_found");
    public static final RestCommonServiceExceptionType CONTENT_CONSTRAINT_REGION_NOT_FOUND       = create("exception.structural_resources.content_constraint.region.not_found");
    public static final RestCommonServiceExceptionType CONTENT_CONSTRAINT_UNPUBLISHED            = create("exception.structural_resources.content_constraint.unpublished");
}