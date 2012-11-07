package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedItemsSecurityUtils;
import org.siemac.metamac.sso.utils.SecurityUtils;

public class ItemsSecurityUtils extends SecurityUtils {

    //
    // ITEM SCHEMES
    //

    public static void canRetrieveItemSchemeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveItemSchemeVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canCreateItemScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateItemScheme(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canUpdateItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
    public static void canDeleteItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canDeleteItemScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindItemSchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendItemSchemeToProductionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canSendItemSchemeToProductionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendItemSchemeToDiffusionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canSendItemSchemeToDiffusionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRejectItemSchemeValidation(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRejectItemSchemeValidation(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishItemSchemeInternally(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canPublishItemSchemeInternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishItemSchemeExternally(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canPublishItemSchemeExternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersioningItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canVersioningItemScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAnnounceItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canAnnounceItemScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canEndItemSchemeValidity(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canEndItemSchemeValidity(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // ITEMS
    //

    public static void canCreateItem(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canModifyItemFromItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateItem(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canModifyItemFromItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteItem(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canModifyItemFromItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrFindResource(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}
