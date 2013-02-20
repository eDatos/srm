package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CategoriesUiHandlers extends UiHandlers {

    void goToCategory(String categorySchemeUrn, String categoryUrn);
    void retrieveCategories(int firstResult, int maxResults, CategoryWebCriteria criteria);
}
