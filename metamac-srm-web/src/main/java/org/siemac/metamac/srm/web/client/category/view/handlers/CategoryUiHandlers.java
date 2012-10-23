package org.siemac.metamac.srm.web.client.category.view.handlers;

public interface CategoryUiHandlers extends BaseCategoryUiHandlers {

    void retrieveCategory(String categoryUrn);
    void retrieveCategoryListByScheme(String categorySchemeUrn);

}
