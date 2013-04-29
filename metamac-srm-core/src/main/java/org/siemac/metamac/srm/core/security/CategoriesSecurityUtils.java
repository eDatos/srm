package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCategoriesSecurityUtils;

public class CategoriesSecurityUtils extends CommonSecurityUtils {

    public static void canRetrieveCategorySchemeByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canRetrieveCategorySchemeByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCategorySchemeVersions(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canRetrieveCategorySchemeVersions(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateCategoryScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canCreateCategoryScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateCategoryScheme(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canUpdateCategoryScheme(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
    public static void canDeleteCategoryScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canDeleteCategoryScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindCategorySchemesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canFindCategorySchemesByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendCategorySchemeToProductionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canSendCategorySchemeToProductionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canSendCategorySchemeToDiffusionValidation(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canSendCategorySchemeToDiffusionValidation(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRejectCategorySchemeValidation(ServiceContext ctx, ProcStatusEnum procStatus) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canRejectCategorySchemeValidation(getMetamacPrincipal(ctx), procStatus)) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishCategorySchemeInternally(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canPublishCategorySchemeInternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canPublishCategorySchemeExternally(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canPublishCategorySchemeExternally(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCopyCategoryScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canCopyCategoryScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canVersioningCategoryScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canVersioningCategoryScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateCategorySchemeTemporalVersion(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canCreateCategorySchemeTemporalVersion(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canAnnounceCategoryScheme(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canAnnounceCategoryScheme(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canEndCategorySchemeValidity(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canEndCategorySchemeValidity(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // CATEGORIES

    public static void canCreateCategory(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canModifyCategoryFromCategoryScheme(getMetamacPrincipal(ctx), categorySchemeVersion.getLifeCycleMetadata().getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateCategory(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canModifyCategoryFromCategoryScheme(getMetamacPrincipal(ctx), categorySchemeVersion.getLifeCycleMetadata().getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCategoryByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canRetrieveCategoryByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteCategory(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canModifyCategoryFromCategoryScheme(getMetamacPrincipal(ctx), categorySchemeVersion.getLifeCycleMetadata().getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canRetrieveCategoriesByCategorySchemeUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canRetrieveCategoriesByCategorySchemeUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canFindCategoriesByCondition(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canFindCategoriesByCondition(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    // CATEGORISATIONS

    public static void canRetrieveCategorisationByUrn(ServiceContext ctx) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canRetrieveCategorisationByUrn(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canModifyCategorisation(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        if (!SharedCategoriesSecurityUtils.canModifyCategorisation(getMetamacPrincipal(ctx), categorySchemeVersion.getLifeCycleMetadata().getProcStatus())) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }
}
