package org.siemac.metamac.internal.web.server;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;

public class ServiceContextHelper {

    private static ServiceContext serviceContext;

    public static ServiceContext getServiceContext() {
        if (serviceContext == null) {
            serviceContext = new ServiceContext("user", "12345", "metamac");
            serviceContext.setProperty("locale", "en");
        }
        return serviceContext;
    }

}
