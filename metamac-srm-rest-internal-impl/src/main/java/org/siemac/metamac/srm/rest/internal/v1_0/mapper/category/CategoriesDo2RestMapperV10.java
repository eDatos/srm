package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Category;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public interface CategoriesDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemes toCategorySchemes(PagedResult<CategorySchemeVersionMetamac> sources, String agencyID, String resourceID,
            String query, String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme toCategoryScheme(CategorySchemeVersionMetamac source);
    public void toCategoryScheme(CategorySchemeVersionMetamac source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryScheme target);

    public Categories toCategories(PagedResult<CategoryMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit);
    public Categories toCategories(List<ItemResult> sources, CategorySchemeVersionMetamac categorySchemeVersion);
    public Category toCategory(CategoryMetamac source);

    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisations toCategorisations(PagedResult<Categorisation> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation toCategorisation(Categorisation source);
    public void toCategorisation(Categorisation source, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Categorisation target);
}