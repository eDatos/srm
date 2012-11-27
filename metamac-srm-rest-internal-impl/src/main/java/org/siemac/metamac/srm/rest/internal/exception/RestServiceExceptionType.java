package org.siemac.metamac.srm.rest.internal.exception;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;

public class RestServiceExceptionType extends RestCommonServiceExceptionType {

    public static final RestCommonServiceExceptionType CONCEPT_SCHEME_NOT_FOUND           = create("exception.srm.concept_scheme.not_found");
    public static final RestCommonServiceExceptionType CONCEPT_NOT_FOUND                  = create("exception.srm.concept.not_found");

    public static final RestCommonServiceExceptionType CATEGORY_SCHEME_NOT_FOUND          = create("exception.srm.category_scheme.not_found");
    public static final RestCommonServiceExceptionType CATEGORY_NOT_FOUND                 = create("exception.srm.category.not_found");

    public static final RestCommonServiceExceptionType ORGANISATION_SCHEME_NOT_FOUND      = create("exception.srm.organisation_scheme.not_found");
    public static final RestCommonServiceExceptionType ORGANISATION_NOT_FOUND             = create("exception.srm.organisation.not_found");

    public static final RestCommonServiceExceptionType AGENCY_SCHEME_NOT_FOUND            = create("exception.srm.agency_scheme.not_found");
    public static final RestCommonServiceExceptionType AGENCY_NOT_FOUND                   = create("exception.srm.agency.not_found");

    public static final RestCommonServiceExceptionType ORGANISATION_UNIT_SCHEME_NOT_FOUND = create("exception.srm.organisation_unit_scheme.not_found");
    public static final RestCommonServiceExceptionType ORGANISATION_UNIT_NOT_FOUND        = create("exception.srm.organisation_unit.not_found");

    public static final RestCommonServiceExceptionType DATA_PROVIDER_SCHEME_NOT_FOUND     = create("exception.srm.data_provider_scheme.not_found");
    public static final RestCommonServiceExceptionType DATA_PROVIDER_NOT_FOUND            = create("exception.srm.data_provider.not_found");

    public static final RestCommonServiceExceptionType DATA_CONSUMER_SCHEME_NOT_FOUND     = create("exception.srm.data_consumer_scheme.not_found");
    public static final RestCommonServiceExceptionType DATA_CONSUMER_NOT_FOUND            = create("exception.srm.data_consumer.not_found");

}