package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;


public class SearchConceptForQuantityBaseItem extends SearchConceptItem {
    private ConceptUiHandlers uiHandlers;
    
    public SearchConceptForQuantityBaseItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
    }
    
    @Override
    protected void retrieveItems(int firstResult, int maxResults, String conceptCriteria, String schemeUrnAsFilter) {
        uiHandlers.retrieveConceptsForQuantityBase(firstResult, maxResults, conceptCriteria, schemeUrnAsFilter);
    }
    
    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria) {
        uiHandlers.retrieveConceptSchemesForQuantityBaseFilter(firstResult, maxResults, filterListCriteria);
    }
    
    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
