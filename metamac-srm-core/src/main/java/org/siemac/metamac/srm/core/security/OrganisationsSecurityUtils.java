package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.security.shared.SharedOrganisationsSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationsSecurityUtils extends ItemsSecurityUtils {

    //
    // ORGANISATION SCHEMES
    //

    public static void canRetrieveOrganisationSchemeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveOrganisationSchemeVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canCreateOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canCreateOrganisationScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateOrganisationScheme(ServiceContext ctx, ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canUpdateOrganisationScheme(getMetamacPrincipal(ctx), procStatus, type)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }
    public static void canDeleteOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canDeleteOrganisationScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canFindOrganisationSchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendOrganisationSchemeToProductionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToProductionValidation(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canSendOrganisationSchemeToDiffusionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToDiffusionValidation(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRejectOrganisationSchemeValidation(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(getMetamacPrincipal(ctx), procStatus)) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishOrganisationSchemeInternally(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canPublishOrganisationSchemeExternally(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeExternally(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canVersioningOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canVersioningOrganisationScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canAnnounceOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canAnnounceOrganisationScheme(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canEndOrganisationSchemeValidity(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canEndOrganisationSchemeValidity(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    //
    // ORGANISATIONS
    //

    public static void canCreateOrganisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canUpdateOrganisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveOrganisationByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canDeleteOrganisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canRetrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    public static void canFindOrganisationsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrFindResource(getMetamacPrincipal(ctx))) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }

    //
    // CATEGORISATIONS
    //
    public static void canModifyCategorisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyCategorisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throw new MetamacException(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
        }
    }
}