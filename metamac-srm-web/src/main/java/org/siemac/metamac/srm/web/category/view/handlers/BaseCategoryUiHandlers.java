package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

public interface BaseCategoryUiHandlers extends CategorisationUiHandlers {

    void saveCategory(CategoryMetamacDto categoryDto);
    void deleteCategory(ItemVisualisationResult itemVisualisationResult);
    void goToCategoryScheme(String urn);
    void goToCategory(String urn);
}
