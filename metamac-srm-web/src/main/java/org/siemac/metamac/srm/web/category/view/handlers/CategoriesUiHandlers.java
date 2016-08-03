package org.siemac.metamac.srm.web.category.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface CategoriesUiHandlers extends BaseUiHandlers {

    void goToCategory(String categorySchemeUrn, String categoryUrn);
    void retrieveCategories(int firstResult, int maxResults, CategoryWebCriteria criteria);
}
