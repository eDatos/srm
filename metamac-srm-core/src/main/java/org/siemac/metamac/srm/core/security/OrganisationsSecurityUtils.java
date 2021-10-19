package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.security.shared.SharedOrganisationsSecurityUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class OrganisationsSecurityUtils extends CommonSecurityUtils {

    //
    // ORGANISATION SCHEMES
    //

    public static void canRetrieveOrganisationSchemeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrganisationSchemeByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrganisationSchemeVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrganisationSchemeVersions(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canCreateOrganisationScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateOrganisationScheme(ServiceContext ctx, ProcStatusEnum procStatus, OrganisationSchemeTypeEnum type) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canUpdateOrganisationScheme(getMetamacPrincipal(ctx), procStatus, type)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
    public static void canDeleteOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canDeleteOrganisationScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindOrganisationSchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canFindOrganisationSchemesByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendOrganisationSchemeToProductionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToProductionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendOrganisationSchemeToDiffusionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canSendOrganisationSchemeToDiffusionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRejectOrganisationSchemeValidation(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRejectOrganisationSchemeValidation(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishOrganisationSchemeInternally(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeInternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishOrganisationSchemeExternally(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canPublishOrganisationSchemeExternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canResendOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canResendOrganisationScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCopyOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canCopyOrganisationScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersioningOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canVersioningOrganisationScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateOrganisationSchemeTemporalVersion(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canCreateOrganisationSchemeTemporalVersion(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAnnounceOrganisationScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canAnnounceOrganisationScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canEndOrganisationSchemeValidity(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canEndOrganisationSchemeValidity(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    //
    // ORGANISATIONS
    //

    public static void canCreateOrganisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateOrganisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrganisationByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrganisationByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteOrganisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyOrganisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveOrganisationsByOrganisationSchemeUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canRetrieveOrganisationsByOrganisationSchemeUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindOrganisationsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canFindOrganisationsByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindOrganisationContactsByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canFindOrganisationsByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canExportOrganisationsTsv(ServiceContext ctx) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canExportOrganisationsTsv(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canImportOrganisationsTsv(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        canCreateOrganisation(ctx, organisationSchemeVersion);
    }

    //
    // CATEGORISATIONS
    //
    public static void canModifyCategorisation(ServiceContext ctx, OrganisationSchemeVersionMetamac organisationSchemeVersion) throws MetamacException {
        if (!SharedOrganisationsSecurityUtils.canModifyCategorisationFromOrganisationScheme(getMetamacPrincipal(ctx), organisationSchemeVersion.getLifeCycleMetadata().getProcStatus(),
                organisationSchemeVersion.getOrganisationSchemeType())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}