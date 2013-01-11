package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.security.shared.SharedCodesSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class CodesSecurityUtils extends SecurityUtils {

    //
    // NOTE: Only to related entities. Security about codelists and codes is in ItemSecurityUtils
    //

    public static void canRetrieveOrFindCodelistFamilyUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindCodelistFamilyByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistFamilyUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariableFamilyUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariableFamilyByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariableFamilyUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariableUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariableByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariableUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariable(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

}
