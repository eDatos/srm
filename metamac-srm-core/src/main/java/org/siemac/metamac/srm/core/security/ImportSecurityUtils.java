package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.security.shared.SharedImportSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class ImportSecurityUtils extends SecurityUtils {

    public static void canImportStructure(ServiceContext ctx) throws MetamacException {
        if (!SharedImportSecurityUtils.canImportStructure(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canExportStructure(ServiceContext ctx) throws MetamacException {
        if (!SharedImportSecurityUtils.canExportStructure(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}