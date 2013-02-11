package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;

public interface BaseCategoryUiHandlers extends CategorisationUiHandlers {

    void saveCategory(CategoryMetamacDto categoryDto);
    void deleteCategory(ItemDto itemDto);
    void goToCategory(String urn);
}
