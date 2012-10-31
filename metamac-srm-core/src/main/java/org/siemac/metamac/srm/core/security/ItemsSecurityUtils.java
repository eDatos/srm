package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedItemsSecurityUtils;

public class ItemsSecurityUtils extends BaseSecurityUtils {

    //
    // ITEM SCHEMES
    //

    public static void canRetrieveItemSchemeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveItemSchemeVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canCreateItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canCreateItemScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateItemScheme(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canUpdateItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }
    public static void canDeleteItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canDeleteItemScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canFindItemSchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendItemSchemeToProductionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canSendItemSchemeToProductionValidation(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendItemSchemeToDiffusionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canSendItemSchemeToDiffusionValidation(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRejectItemSchemeValidation(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRejectItemSchemeValidation(getMetamacPrincipal(ctx), procStatus)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishItemSchemeInternally(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canPublishItemSchemeInternally(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishItemSchemeExternally(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canPublishItemSchemeExternally(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canVersioningItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canVersioningItemScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canAnnounceItemScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canAnnounceItemScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canEndItemSchemeValidity(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canEndItemSchemeValidity(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    //
    // ITEMS
    //

    public static void canCreateItem(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canModifyItemFromItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateItem(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canModifyItemFromItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteItem(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedItemsSecurityUtils.canModifyItemFromItemScheme(getMetamacPrincipal(ctx), procStatus)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveOrFindResource(ServiceContext ctx) throws MetamacException {
        if (!SharedItemsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }
}
