package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface BaseVariableUiHandlers extends BaseUiHandlers {

    void createSegregation(String variableElementUrn, List<String> variableElementUrns);
    void createFusion(List<String> variableElementUrns, String variableElementUrn);
    void deleteVariableElementOperations(List<String> codes);
}
