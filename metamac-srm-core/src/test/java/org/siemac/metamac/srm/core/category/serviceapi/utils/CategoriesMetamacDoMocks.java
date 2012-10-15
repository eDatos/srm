package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDoMocks;
import com.arte.statistic.sdmx.v2_1.domain.jaxb.structure.CategoryType;

public class CategoriesMetamacDoMocks extends CategoriesDoMocks {

    public static CategorySchemeVersionMetamac mockCategoryScheme() {

        CategorySchemeVersionMetamac categorySchemeVersion = new CategorySchemeVersionMetamac();

        mockCategoryScheme(categorySchemeVersion);
        return categorySchemeVersion;
    }

    public static void fillCategorySchemeAutogeneratedMetadata(CategorySchemeVersionMetamac entity) {
        entity.setLifeCycleMetadata(BaseDoMocks.mockLifeCycle());
        CategoriesDoMocks.fillCategorySchemeAutogeneratedMetadata(entity);
    }

    public static CategoryMetamac mockCategory(CategoryType categoryType) {

        CategoryMetamac category = new CategoryMetamac();

        mockCategory(category);
        return category;
    }

    public static void fillCategoryAutogeneratedMetadata(CategoryMetamac entity) {
        CategoriesDoMocks.fillCategoryAutogeneratedMetadata(entity);
    }
}
