package org.siemac.metamac.srm.web.server;

import javax.servlet.http.HttpServletRequest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServiceContextHolder {

    private static String SESSION_ATTRIBUTE = "_serviceContext_";

    public static ServiceContext getCurrentServiceContext() {
        ServiceContext serviceContext = (ServiceContext) getCurrentRequest().getSession().getAttribute(ServiceContextHolder.SESSION_ATTRIBUTE);
        if (serviceContext == null) {
            serviceContext = new ServiceContext("ANNONIMOUS", "NO_SESSION", SrmConstants.SECURITY_APPLICATION_ID); // TODO
            putCurrentServiceContext(serviceContext);
        }
        return serviceContext;
    }

    public static void putCurrentServiceContext(ServiceContext serviceContext) {
        getCurrentRequest().getSession().setAttribute(SESSION_ATTRIBUTE, serviceContext);
    }

    private static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

}
