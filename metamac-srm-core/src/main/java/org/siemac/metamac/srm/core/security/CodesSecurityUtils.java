package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.security.shared.SharedCodesSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class CodesSecurityUtils extends SecurityUtils {

    //
    // NOTE: Only to related entities
    //

    public static void canRetrieveCodelistFamilyUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveCodelistFamilyByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistFamilyUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}
