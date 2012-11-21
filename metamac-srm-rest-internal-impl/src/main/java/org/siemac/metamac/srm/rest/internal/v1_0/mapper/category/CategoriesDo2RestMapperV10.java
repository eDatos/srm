package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Category;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;

public interface CategoriesDo2RestMapperV10 {

    public org.siemac.metamac.rest.srm_internal.v1_0.domain.CategorySchemes toCategorySchemes(PagedResult<CategorySchemeVersionMetamac> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme toCategoryScheme(CategorySchemeVersionMetamac source);
    public void toCategoryScheme(CategorySchemeVersionMetamac source, org.siemac.metamac.rest.srm_internal.v1_0.domain.CategoryScheme target);

    public Categories toCategories(PagedResult<CategoryMetamac> categoriesEntitiesResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Category toCategory(CategoryMetamac source);
}