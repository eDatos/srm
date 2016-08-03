package org.siemac.metamac.srm.web.code.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface VariableElementsUiHandlers extends BaseUiHandlers {

    void goToVariableElement(String variableCode, String variableElementCode);
    void retrieveVariableElements(int firstResult, int maxResults, VariableElementWebCriteria criteria);
    void retrieveCodesForVariableElementGeographicalGranularity(int firstResult, int maxResults, CodeWebCriteria webCriteria);
}
