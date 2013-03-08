package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCodesSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class CodesSecurityUtils extends SecurityUtils {

    //
    // NOTE: Only to related entities. Security about codelists and codes is in ItemSecurityUtils
    //

    public static void canRetrieveOrFindCodelistOrderVisualisation(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindCodelistOrderVisualisation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistOrderVisualisation(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistOrderVisualisation(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindCodelistOpennessVisualisation(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindCodelistOpennessVisualisation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistOpennessVisualisation(ServiceContext ctx, ProcStatusEnum codelistProcStatus) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistOpennessVisualisation(getMetamacPrincipal(ctx), codelistProcStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindCodelistFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindCodelistFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudCodelistFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudCodelistFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariable(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariable(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariable(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariable(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindVariableElement(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRetrieveOrFindVariableElement(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCrudVariableElement(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canCrudVariableElement(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAddVariablesToVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canAddVariablesToVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRemoveVariableFromVariableFamily(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canRemoveVariableFromVariableFamily(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAddVariableElementsToVariable(ServiceContext ctx) throws MetamacException {
        if (!SharedCodesSecurityUtils.canAddVariableElementsToVariable(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}
