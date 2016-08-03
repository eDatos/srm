package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.ConceptWebCriteria;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public class SearchConceptForQuantityDenominatorItem extends SearchConceptItem {

    private ConceptUiHandlers uiHandlers;

    public SearchConceptForQuantityDenominatorItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
    }

    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria, boolean isLastVersion) {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = new ConceptSchemeWebCriteria();
        conceptSchemeWebCriteria.setCriteria(filterListCriteria);
        conceptSchemeWebCriteria.setIsLastVersion(isLastVersion);
        uiHandlers.retrieveConceptSchemesForQuantityDenominatorFilter(firstResult, maxResults, conceptSchemeWebCriteria);
    }

    @Override
    protected void retrieveItems(int firstResult, int maxResults, String conceptCriteria, String schemeUrnAsFilter, boolean isLastVersion) {
        ConceptWebCriteria conceptWebCriteria = new ConceptWebCriteria();
        conceptWebCriteria.setCriteria(conceptCriteria);
        conceptWebCriteria.setItemSchemeUrn(schemeUrnAsFilter);
        conceptWebCriteria.setIsLastVersion(isLastVersion);
        uiHandlers.retrieveConceptsForQuantityDenominator(firstResult, maxResults, conceptWebCriteria);
    }

    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
