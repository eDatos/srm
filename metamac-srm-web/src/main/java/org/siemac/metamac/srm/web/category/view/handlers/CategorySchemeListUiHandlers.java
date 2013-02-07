package org.siemac.metamac.srm.web.category.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CategorySchemeListUiHandlers extends UiHandlers {

    void goToCategoryScheme(String urn);
    void createCategoryScheme(CategorySchemeMetamacDto categorySchemeDto);
    void deleteCategorySchemes(List<String> urns);
    void retrieveCategorySchemes(int firstResult, int maxResults, String criteria);
    void cancelValidity(List<String> urn);

}
