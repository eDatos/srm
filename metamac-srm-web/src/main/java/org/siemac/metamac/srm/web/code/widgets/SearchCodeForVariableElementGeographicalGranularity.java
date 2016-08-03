package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.code.view.handlers.BaseVariableUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public class SearchCodeForVariableElementGeographicalGranularity extends SearchCodeItem {

    private BaseVariableUiHandlers uiHandlers;

    public SearchCodeForVariableElementGeographicalGranularity(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
        setRequired(false);
        setInformationLabelMessage(getConstants().variableElementGeographicalGranularityInfoMessage());
        setIsLastVersionItemVisible(false); // Do not show isLastVersion item (the codelist is specified in the data directory)
        setHideItemSchemeFilter(true); // Do not show the item scheme filter because the codes will always belong to the same codelist (it is specified in the data directory)
    }

    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria, boolean isLastVersion) {
        CodelistWebCriteria codelistWebCriteria = new CodelistWebCriteria();
        codelistWebCriteria.setCriteria(filterListCriteria);
        // isLastVersion is not taked into account (the codelist is specified in the data directory)
        uiHandlers.retrieveCodelistsForVariableElementGeographicalGranularity(firstResult, maxResults, codelistWebCriteria);
    }

    @Override
    protected void retrieveItems(int firstResult, int maxResults, String codeCriteria, String schemeUrnAsFilter, boolean isLastVersion) {
        CodeWebCriteria codeWebCriteria = new CodeWebCriteria();
        codeWebCriteria.setCriteria(codeCriteria);
        codeWebCriteria.setItemSchemeUrn(schemeUrnAsFilter);
        // isLastVersion is not taked into account (the codelist shown is specified in the data directory)
        uiHandlers.retrieveCodesForVariableElementGeographicalGranularity(firstResult, maxResults, codeWebCriteria);
    }

    public void setUiHandlers(BaseVariableUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
