package org.siemac.metamac.srm.web.client.code.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

public interface BaseVariableUiHandlers extends UiHandlers {

    void createSegregation(String variableElementUrn, List<String> variableElementUrns);
    void createFusion(List<String> variableElementUrns, String variableElementUrn);
    void deleteVariableElementOperations(List<String> codes);
}
