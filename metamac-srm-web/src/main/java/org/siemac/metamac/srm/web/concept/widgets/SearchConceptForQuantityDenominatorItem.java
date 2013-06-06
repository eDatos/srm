package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public class SearchConceptForQuantityDenominatorItem extends SearchConceptItem {

    private ConceptUiHandlers uiHandlers;

    public SearchConceptForQuantityDenominatorItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
    }

    @Override
    protected void retrieveItems(int firstResult, int maxResults, String conceptCriteria, String schemeUrnAsFilter) {
        uiHandlers.retrieveConceptsForQuantityDenominator(firstResult, maxResults, conceptCriteria, schemeUrnAsFilter);
    }

    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria) {
        uiHandlers.retrieveConceptSchemesForQuantityDenominatorFilter(firstResult, maxResults, filterListCriteria);
    }

    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
