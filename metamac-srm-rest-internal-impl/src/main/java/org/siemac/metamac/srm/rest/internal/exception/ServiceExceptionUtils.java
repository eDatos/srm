package org.siemac.metamac.srm.rest.internal.exception;

import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;

public class ServiceExceptionUtils {

    private final static Logger logger = LoggerFactory.getLogger(ServiceExceptionUtils.class);

    public static MetamacException manageMetamacRestException(Exception e, String apiName, Object clientProxy) throws MetamacException {
        logger.error("Error", e);

        if (e instanceof ServerWebApplicationException) {
            return manageMetamacServerWebApplicationException((ServerWebApplicationException) e, apiName, clientProxy);
        } else {
            return new MetamacException(e, ServiceExceptionType.UNKNOWN, e.getMessage());
        }
    }

    private static MetamacException manageMetamacServerWebApplicationException(ServerWebApplicationException e, String apiName, Object clientProxy) throws MetamacException {
        org.siemac.metamac.rest.common.v1_0.domain.Exception exception = e.toErrorObject(WebClient.client(clientProxy), org.siemac.metamac.rest.common.v1_0.domain.Exception.class);
        MetamacException metamacException;

        if (exception == null) {
            if (Status.NOT_FOUND.getStatusCode() == e.getResponse().getStatus()) {
                metamacException = new MetamacException(ServiceExceptionType.REST_API_INVOCATION_ERROR_NOT_FOUND_API, apiName);
            } else {
                metamacException = new MetamacException(ServiceExceptionType.REST_API_INVOCATION_ERROR_UNKNOWN_API, e);
            }
        } else {
            if (Status.NOT_FOUND.getStatusCode() == e.getResponse().getStatus()) {
                metamacException = new MetamacException(ServiceExceptionType.REST_API_INVOCATION_ERROR_NOT_FOUND_RESOURCE, exception.getMessage());
            } else {
                metamacException = new MetamacException(ServiceExceptionType.REST_API_INVOCATION_ERROR_UNKNOWN_RESOURCE, exception.getMessage());
            }
        }

        return metamacException;
    }
}
