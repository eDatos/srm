package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.srm.web.category.presenter.CategorySchemeListPresenter;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeListUiHandlers;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;

public class CategorySchemeSearchSectionStack extends VersionableResourceSearchSectionStack {

    private CategorySchemeListUiHandlers uiHandlers;

    public CategorySchemeSearchSectionStack() {
    }

    public CategorySchemeWebCriteria getCategorySchemeWebCriteria() {
        return (CategorySchemeWebCriteria) getVersionableResourceWebCriteria(new CategorySchemeWebCriteria());
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCategorySchemes(CategorySchemeListPresenter.SCHEME_LIST_FIRST_RESULT, CategorySchemeListPresenter.SCHEME_LIST_MAX_RESULTS, getCategorySchemeWebCriteria());
    }

    public void setUiHandlers(CategorySchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategorySchemeListUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
