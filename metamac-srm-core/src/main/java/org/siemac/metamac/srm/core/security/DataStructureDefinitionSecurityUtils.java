package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.security.shared.SharedDsdSecurityUtils;

public class DataStructureDefinitionSecurityUtils extends CommonSecurityUtils {

    //
    // DSD
    //

    public static void canRetrieveDataStructureDefinitionByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canRetrieveDataStructureDefinitionByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDataStructureDefinitionVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canRetrieveDataStructureDefinitionVersions(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canCreateDataStructureDefinition(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamacOld, String operationNew)
            throws MetamacException {
        if (!SharedDsdSecurityUtils.canUpdateDsd(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamacOld.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamacOld), operationNew)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canDeleteDsd(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindDataStructureDefinitionByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canFindDataStructureDefinitionByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCopyDataStructureDefinition(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canCopyDataStructureDefinition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // LIFECYCLE
    //

    public static void canSendDataStructureDefinitionToProductionValidation(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSendDsdToProductionValidation(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendDataStructureDefinitionToDiffusionValidation(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSendDsdToDiffusionValidation(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRejectDataStructureDefinitionValidation(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canRejectDsdValidation(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishDataStructureDefinitionInternally(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canPublishDsdInternally(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishDataStructureDefinitionExternally(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canPublishDsdExternally(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canResendDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canResendDsd(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersioningDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canVersioningDsd(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateDataStructureDefinitionTemporalVersion(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canCreateDsdTemporalVersion(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAnnounceDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canAnnounceDsd(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canEndDataStructureDefinitionValidity(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canEndDsdValidity(getMetamacPrincipal(ctx), getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // ARTEFACTS IN DSD
    //

    public static void canFindDescriptorsForDataStructureDefinition(ServiceContext ctx) throws MetamacException {
        if (!SharedDsdSecurityUtils.canFindDescriptorsForDataStructureDefinition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSaveDescriptorForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSaveDescriptorForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteDescriptorForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canDeleteDescriptorForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSaveComponentForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canSaveComponentForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteComponentForDataStructureDefinition(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canDeleteComponentForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // CATEGORISATION
    //
    public static void canModifyCategorisation(ServiceContext ctx, DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) throws MetamacException {
        if (!SharedDsdSecurityUtils.canModifyCategorisationForDataStructureDefinition(getMetamacPrincipal(ctx), dataStructureDefinitionVersionMetamac.getLifeCycleMetadata().getProcStatus(),
                getOperationCode(dataStructureDefinitionVersionMetamac))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // PRIVATE METHODS
    //
    private static String getOperationCode(DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac) {
        return dataStructureDefinitionVersionMetamac.getStatisticalOperation() != null ? dataStructureDefinitionVersionMetamac.getStatisticalOperation().getCode() : null;
    }
}