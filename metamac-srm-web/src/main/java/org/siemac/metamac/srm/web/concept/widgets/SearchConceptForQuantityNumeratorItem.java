package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;


public class SearchConceptForQuantityNumeratorItem extends SearchConceptItem {
    private ConceptUiHandlers uiHandlers;
    
    public SearchConceptForQuantityNumeratorItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
    }
    
    @Override
    protected void retrieveItems(int firstResult, int maxResults, String conceptCriteria, String schemeUrnAsFilter) {
        uiHandlers.retrieveConceptsForQuantityNumerator(firstResult, maxResults, conceptCriteria, schemeUrnAsFilter);
    }
    
    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria) {
        uiHandlers.retrieveConceptSchemesForQuantityNumeratorFilter(firstResult, maxResults, filterListCriteria);
    }
    
    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
