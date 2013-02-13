package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDoMocks;

public class CategoriesMetamacDoMocks extends CategoriesDoMocks {

    public static CategorySchemeVersionMetamac mockCategoryScheme(OrganisationMetamac maintainer) {
        CategorySchemeVersionMetamac categorySchemeVersion = new CategorySchemeVersionMetamac();
        mockCategoryScheme(categorySchemeVersion, maintainer);
        return categorySchemeVersion;
    }

    public static CategorySchemeVersionMetamac mockCategorySchemeFixedValues(String agencyID, String resourceID, String version) {
        CategorySchemeVersionMetamac target = new CategorySchemeVersionMetamac();
        CategoriesDoMocks.mockCategorySchemeFixedValues(target, agencyID, resourceID, version);

        // metamac
        target.setLifeCycleMetadata(BaseDoMocks.mockLifeCycleExternallyPublished());
        return target;
    }

    public static CategoryMetamac mockCategory() {
        CategoryMetamac category = new CategoryMetamac();
        mockCategory(category);
        return category;
    }

    public static CategoryMetamac mockCategoryFixedValues(String resourceID, CategorySchemeVersionMetamac itemSchemeVersion, CategoryMetamac parent) {
        CategoryMetamac target = new CategoryMetamac();
        CategoriesDoMocks.mockCategoryFixedValues(target, resourceID, itemSchemeVersion, parent);
        return target;
    }
}
