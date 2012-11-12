package org.siemac.metamac.srm.rest.internal.exception;

import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;

public class RestServiceExceptionType extends RestCommonServiceExceptionType {

    public static final RestCommonServiceExceptionType CONCEPT_SCHEME_NOT_FOUND = create("exception.srm.concept_scheme.not_found");
}