package org.siemac.metamac.srm.soap.external.v1_0.service.utils;

import org.siemac.metamac.core.common.serviceimpl.utils.ValidationUtils;
import org.siemac.metamac.soap.common.v1_0.domain.MetamacCriteria;
import org.siemac.metamac.soap.exception.SoapExceptionUtils;
import org.siemac.metamac.soap.structural_resources.v1_0.ExceptionFault;
import org.siemac.metamac.srm.soap.external.exception.SoapExceptionParameters;
import org.siemac.metamac.srm.soap.external.exception.SoapServiceExceptionType;

public class InvocationValidator {

    public static void validateFindVariableFamilies(MetamacCriteria criteria) throws ExceptionFault {
        // nothing
    }

    public static void validateFindVariables(MetamacCriteria criteria) throws ExceptionFault {
        // nothing
    }

    public static void validateFindCodelistFamilies(MetamacCriteria criteria) throws ExceptionFault {
        // nothing
    }

    public static void validateFindCodelists(MetamacCriteria criteria) throws ExceptionFault {
        // nothing
    }

    public static void validateRetrieveCodelist(String urn) throws ExceptionFault {
        if (ValidationUtils.isEmpty(urn)) {
            throw SoapExceptionUtils.buildExceptionFault(SoapServiceExceptionType.PARAMETER_REQUIRED, SoapExceptionParameters.URN);
        }
    }
}
