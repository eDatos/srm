package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.code.widgets.SearchCodeItem;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public class SearchCodeForQuantityBaseLocationItem extends SearchCodeItem {

    private ConceptUiHandlers uiHandlers;

    public SearchCodeForQuantityBaseLocationItem(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
    }

    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria, boolean isLastVersion) {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setCriteria(filterListCriteria);
        codelistWebCriteria.setIsLastVersion(isLastVersion);
        uiHandlers.retrieveCodelistsForQuantityBaseLocationFilter(firstResult, maxResults, codelistWebCriteria);
    }

    @Override
    protected void retrieveItems(int firstResult, int maxResults, String codeCriteria, String schemeUrnAsFilter, boolean isLastVersion) {
        CodeWebCriteria codeWebCriteria = new CodeWebCriteria();
        codeWebCriteria.setCriteria(codeCriteria);
        codeWebCriteria.setItemSchemeUrn(schemeUrnAsFilter);
        codeWebCriteria.setIsLastVersion(isLastVersion);
        uiHandlers.retrieveCodesForQuantityBaseLocation(firstResult, maxResults, codeWebCriteria);
    }

    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
