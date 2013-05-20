package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;

public interface CategoryUiHandlers extends BaseCategoryUiHandlers {

    void retrieveCategory(String categoryUrn);
    void retrieveCategoryListByScheme(String categorySchemeUrn);
    void deleteCategory(CategoryMetamacDto categoryMetamacDto);
}
