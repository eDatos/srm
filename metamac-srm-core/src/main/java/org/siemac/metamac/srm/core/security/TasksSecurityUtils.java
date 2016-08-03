package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.security.shared.SharedTasksSecurityUtils;

public class TasksSecurityUtils extends CommonSecurityUtils {

    public static void canFindTasksByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedTasksSecurityUtils.canFindTasksByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canImportStructure(ServiceContext ctx) throws MetamacException {
        if (!SharedTasksSecurityUtils.canImportStructure(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canExportStructure(ServiceContext ctx) throws MetamacException {
        if (!SharedTasksSecurityUtils.canExportStructure(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}