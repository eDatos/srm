package org.siemac.metamac.soap.exception;

import java.math.BigInteger;
import java.text.MessageFormat;

import org.siemac.metamac.soap.structural_resources.v1_0.ExceptionFault;

// TODO put in common library if more soap services are created
public class SoapExceptionUtils {

    public static org.siemac.metamac.soap.common.v1_0.domain.Exception buildException(SoapCommonServiceExceptionType exceptionType, String... parameters) {
        org.siemac.metamac.soap.common.v1_0.domain.Exception exception = new org.siemac.metamac.soap.common.v1_0.domain.Exception();
        exception.setCode(exceptionType.getCode());
        exception.setMessage(MessageFormat.format(exceptionType.getMessageForReasonType(), (Object[]) parameters));
        if (parameters != null && parameters.length != 0) {
            exception.setParameters(new org.siemac.metamac.soap.common.v1_0.domain.ErrorParameters());
            exception.getParameters().setTotal(BigInteger.valueOf(parameters.length));
            for (int i = 0; i < parameters.length; i++) {
                String parameter = parameters[i];
                exception.getParameters().getParameters().add(parameter);
            }
        }
        return exception;
    }

    public static ExceptionFault buildExceptionFault(org.siemac.metamac.soap.common.v1_0.domain.Exception exception) {
        return new ExceptionFault(exception.getCode(), exception);
    }

    public static ExceptionFault buildExceptionFault(SoapCommonServiceExceptionType exceptionType, String... parameters) {
        org.siemac.metamac.soap.common.v1_0.domain.Exception exception = buildException(exceptionType, parameters);
        return buildExceptionFault(exception);
    }
}