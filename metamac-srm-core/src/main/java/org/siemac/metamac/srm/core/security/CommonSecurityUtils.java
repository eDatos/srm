package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.security.shared.SharedSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class CommonSecurityUtils extends SecurityUtils {

    protected static void canRetrieveOrFindResource(ServiceContext ctx) throws MetamacException {
        if (!SharedSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

}
