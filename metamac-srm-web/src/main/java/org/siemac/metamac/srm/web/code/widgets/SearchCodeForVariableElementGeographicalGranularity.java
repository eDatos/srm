package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.code.view.handlers.BaseVariableUiHandlers;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

public class SearchCodeForVariableElementGeographicalGranularity extends SearchCodeItem {

    private BaseVariableUiHandlers uiHandlers;

    public SearchCodeForVariableElementGeographicalGranularity(String name, String title, CustomLinkItemNavigationClickHandler navigationHandler) {
        super(name, title, navigationHandler);
        setRequired(true);
        setInformationLabelMessage(getConstants().variableElementGeographicalGranularityInfoMessage());
    }

    @Override
    protected void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria) {
        uiHandlers.retrieveCodelistsForVariableElementGeographicalGranularity(firstResult, maxResults, filterListCriteria);
    }

    @Override
    protected void retrieveItems(int firstResult, int maxResults, String codeCriteria, String schemeUrnAsFilter) {
        uiHandlers.retrieveCodesForVariableElementGeographicalGranularity(firstResult, maxResults, codeCriteria, schemeUrnAsFilter);
    }

    public void setUiHandlers(BaseVariableUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
