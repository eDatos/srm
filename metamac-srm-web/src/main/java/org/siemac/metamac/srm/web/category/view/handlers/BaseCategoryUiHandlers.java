package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface BaseCategoryUiHandlers extends UiHandlers {

    void saveCategory(CategoryMetamacDto categoryDto);
    void deleteCategory(ItemDto itemDto);
    void goToCategory(String urn);

}