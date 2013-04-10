package org.siemac.metamac.srm.rest.internal.v1_0.category.utils;

import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemScheme;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;

public class CategoriesDoMocks {

    public static CategorySchemeVersionMetamac mockCategoryScheme(String agencyID, String resourceID, String version) {
        CategorySchemeVersionMetamac target = CategoriesMetamacDoMocks.mockCategorySchemeFixedValues(agencyID, resourceID, version);
        target.setItemScheme(new ItemScheme());
        return target;
    }

    public static CategoryMetamac mockCategory(String resourceID, CategorySchemeVersionMetamac categoryScheme, CategoryMetamac parent) {
        return CategoriesMetamacDoMocks.mockCategoryFixedValues(resourceID, categoryScheme, parent);
    }

    public static CategorySchemeVersionMetamac mockCategorySchemeWithCategories(String agencyID, String resourceID, String version) {

        CategorySchemeVersionMetamac categorySchemeVersion = mockCategoryScheme(agencyID, resourceID, version);

        // categorys
        CategoryMetamac category1 = mockCategory("category1", categorySchemeVersion, null);
        CategoryMetamac category2 = mockCategory("category2", categorySchemeVersion, null);
        CategoryMetamac category2A = mockCategory("category2A", categorySchemeVersion, category2);
        CategoryMetamac category2B = mockCategory("category2B", categorySchemeVersion, category2);

        // categorys hierarchy
        categorySchemeVersion.addItem(category1);
        categorySchemeVersion.addItemsFirstLevel(category1);
        categorySchemeVersion.addItem(category2);
        categorySchemeVersion.addItemsFirstLevel(category2);
        categorySchemeVersion.addItem(category2A);
        categorySchemeVersion.addItem(category2B);
        category2.addChildren(category2A);
        category2.addChildren(category2B);

        return categorySchemeVersion;
    }

    public static Categorisation mockCategorisation(String agencyID, String resourceID, String version) {
        Categorisation target = new Categorisation();
        target.setMaintainableArtefact(new MaintainableArtefact());
        CategoriesMetamacDoMocks.mockMaintainableArtefactFixedValues(target.getMaintainableArtefact(), agencyID, resourceID, version);

        CategorySchemeVersionMetamac categorySchemeVersionMetamac = mockCategoryScheme(agencyID, "categoryScheme-" + resourceID, version);
        target.setArtefactCategorised(categorySchemeVersionMetamac.getMaintainableArtefact());
        target.setCategory(mockCategory("category-" + resourceID, categorySchemeVersionMetamac, null));
        return target;
    }

}