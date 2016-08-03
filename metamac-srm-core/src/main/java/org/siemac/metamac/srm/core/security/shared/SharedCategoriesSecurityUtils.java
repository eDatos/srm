package org.siemac.metamac.srm.core.security.shared;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedCategoriesSecurityUtils extends SharedItemsSecurityUtils {

    //
    // CATEGORY SCHEMES
    //

    public static boolean canRetrieveCategorySchemeByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindCategorySchemesByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveCategorySchemeVersions(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCreateCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canCreateItemScheme(metamacPrincipal);
    }

    public static boolean canUpdateCategoryScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canUpdateItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canDeleteCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canDeleteItemScheme(metamacPrincipal);
    }

    public static boolean canSendCategorySchemeToProductionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToProductionValidation(metamacPrincipal);
    }

    public static boolean canSendCategorySchemeToDiffusionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToDiffusionValidation(metamacPrincipal);
    }

    public static boolean canRejectCategorySchemeValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canRejectItemSchemeValidation(metamacPrincipal, procStatus);
    }

    public static boolean canPublishCategorySchemeInternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeInternally(metamacPrincipal);
    }

    public static boolean canPublishCategorySchemeExternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeExternally(metamacPrincipal);
    }

    public static boolean canCopyCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canCopyItemScheme(metamacPrincipal);
    }

    public static boolean canVersioningCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canCreateCategorySchemeTemporalVersion(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canAnnounceCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canAnnounceItemScheme(metamacPrincipal);
    }

    public static boolean canEndCategorySchemeValidity(MetamacPrincipal metamacPrincipal) {
        return canEndItemSchemeValidity(metamacPrincipal);
    }

    //
    // ITEMS
    //

    public static boolean canRetrieveCategoryByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveCategoriesByCategorySchemeUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindCategoriesByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    /**
     * Determines if items from an category scheme can be created, deleted or updated
     */
    public static boolean canModifyCategoryFromCategoryScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canRetrieveCategorisationByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    /**
     * Determines if categorisations from an category scheme can be created or deleted
     */
    public static boolean canModifyCategorisationFromCategoryScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(procStatus) || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(procStatus)) {
            return canPublishCategorySchemeExternally(metamacPrincipal);
        } else {
            return canUpdateCategoryScheme(metamacPrincipal, procStatus);
        }
    }

    public static boolean canExportCategoriesTsv(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canImportCategoriesTsv(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyCategoryFromCategoryScheme(metamacPrincipal, procStatus);
    }
}