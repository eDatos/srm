package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;

public class DataStructureDefinitionSecurityUtils extends BaseSecurityUtils {

    /**
     * DSD
     */

    public static void canCreateDataStructureDefinition(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canCreateDataStructureDefinition(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canUpdateDsd(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canDeleteDsd(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canFindDataStructureDefinitionByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canFindDataStructureDefinitionByCondition(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveDataStructureDefinitionByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canRetrieveDataStructureDefinitionByUrn(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDataStructureDefinitionVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canRetrieveDataStructureDefinitionVersions(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canImportDataStructureDefinition(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canImportDsd(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    /**
     * LIFECYCLE
     */

    public static void canSendDataStructureDefinitionToProductionValidation(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSendDsdToProductionValidation(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSendDsdToDiffusionValidation(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRejectDataStructureDefinitionValidation(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canRejectDsdValidation(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishDataStructureDefinitionInternally(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canPublishDsdInternally(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishDataStructureDefinitionExternally(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canPublishDsdExternally(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canVersioningDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canVersioningDsd(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canAnnounceDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canAnnounceDsd(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canEndDataStructureDefinitionValidity(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canEndDsdValidity(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    /**
     * ARTEFACTS IN DSD
     */

    public static void canFindDescriptorsForDataStructureDefinition(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canFindDescriptorsForDataStructureDefinition(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSaveDescriptorForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSaveDescriptorForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteDescriptorForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canDeleteDescriptorForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSaveComponentForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSaveComponentForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteComponentForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canDeleteComponentForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED, ctx.getUserId());
        }
    }

}
