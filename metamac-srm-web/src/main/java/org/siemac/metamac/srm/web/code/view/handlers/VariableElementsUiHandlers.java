package org.siemac.metamac.srm.web.code.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableElementsUiHandlers extends UiHandlers {

    void goToVariableElement(String variableCode, String variableElementCode);
    void retrieveVariableElements(int firstResult, int maxResults, String criteria);
}
