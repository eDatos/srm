package org.siemac.metamac.srm.core.security.shared;

import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedCategoriesSecurityUtils extends SharedItemsSecurityUtils {

    public static boolean canCreateCategorySchemeTemporalVersion(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canCopyCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canCopyItemScheme(metamacPrincipal);
    }

}