package org.siemac.metamac.srm.soap.external.exception;

import java.math.BigInteger;
import java.text.MessageFormat;

// TODO librería común?
public class SoapExceptionUtils {

    public static org.siemac.metamac.soap.srm.v1_0.exception.Exception getException(SoapCommonServiceExceptionType exceptionType, String... parameters) {
        org.siemac.metamac.soap.srm.v1_0.exception.Exception exception = new org.siemac.metamac.soap.srm.v1_0.exception.Exception();
        exception.setCode(exceptionType.getCode());
        exception.setMessage(MessageFormat.format(exceptionType.getMessageForReasonType(), (Object[]) parameters));
        if (parameters != null && parameters.length != 0) {
            exception.setParameters(new org.siemac.metamac.soap.srm.v1_0.exception.ErrorParameters());
            exception.getParameters().setTotal(BigInteger.valueOf(parameters.length));
            for (int i = 0; i < parameters.length; i++) {
                String parameter = parameters[i];
                exception.getParameters().getParameter().add(parameter);
            }
        }
        return exception;
    }
}