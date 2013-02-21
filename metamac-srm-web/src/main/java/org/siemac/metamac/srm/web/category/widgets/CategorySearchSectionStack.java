package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.web.category.presenter.CategoriesPresenter;
import org.siemac.metamac.srm.web.category.view.handlers.CategoriesUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.ItemSearchSectionStack;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;

public class CategorySearchSectionStack extends ItemSearchSectionStack {

    private CategoriesUiHandlers uiHandlers;

    public CategorySearchSectionStack() {
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCategories(CategoriesPresenter.ITEM_LIST_FIRST_RESULT, CategoriesPresenter.ITEM_LIST_MAX_RESULTS, getCategoryWebCriteria());
    }

    public CategoryWebCriteria getCategoryWebCriteria() {
        return (CategoryWebCriteria) getItemWebCriteria(new CategoryWebCriteria());
    }

    public void setUiHandlers(CategoriesUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategoriesUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}