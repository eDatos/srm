package org.siemac.metamac.srm.web.category.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface CategorySchemeListUiHandlers extends BaseUiHandlers {

    void goToCategoryScheme(String urn);
    void createCategoryScheme(CategorySchemeMetamacDto categorySchemeDto);
    void deleteCategorySchemes(List<String> urns);
    void retrieveCategorySchemes(int firstResult, int maxResults, CategorySchemeWebCriteria criteria);
    void cancelValidity(List<String> urn);
}
