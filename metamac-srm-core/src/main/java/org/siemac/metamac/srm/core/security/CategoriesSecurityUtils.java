package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.security.shared.SharedCategoriesSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class CategoriesSecurityUtils extends SecurityUtils {

    //
    // NOTE: Only to specific actions. Security about the others actions is in ItemSecurityUtils
    //

    public static void canCreateCategorySchemeTemporalVersion(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canCreateCategorySchemeTemporalVersion(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}
