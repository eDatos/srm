package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.code.widgets.SearchCodeItem;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public class SearchCodeForQuantityUnitItem extends SearchCodeItem {

    private ConceptUiHandlers uiHandlers;

    public SearchCodeForQuantityUnitItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
    }

    @Override
    protected void retrieveItems(int firstResult, int maxResults, String codeCriteria, String schemeUrnAsFilter) {
        uiHandlers.retrieveCodesForQuantityUnit(firstResult, maxResults, codeCriteria, schemeUrnAsFilter);
    }

    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria) {
        uiHandlers.retrieveCodeListsForQuantityUnitFilter(firstResult, maxResults, filterListCriteria);
    }

    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
