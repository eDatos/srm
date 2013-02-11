package org.siemac.metamac.srm.soap.external.exception;

import org.siemac.metamac.soap.exception.SoapCommonServiceExceptionType;

public class SoapServiceExceptionType extends SoapCommonServiceExceptionType {

    public static final SoapCommonServiceExceptionType VARIABLE_FAMILY_NOT_FOUND = create("exception.structural_resources.codes.variable_family.not_found");
    public static final SoapCommonServiceExceptionType VARIABLE_NOT_FOUND        = create("exception.structural_resources.codes.variable.not_found");
    public static final SoapCommonServiceExceptionType CODELIST_FAMILY_NOT_FOUND = create("exception.structural_resources.codes.codelist_family.not_found");
    public static final SoapCommonServiceExceptionType CODELIST_NOT_FOUND        = create("exception.structural_resources.codes.codelist.not_found");
}