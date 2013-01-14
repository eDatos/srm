package org.siemac.metamac.srm.web.client.code.view.handlers;

import org.siemac.metamac.srm.core.code.dto.VariableDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableUiHandlers extends UiHandlers {

    void retrieveVariableByUrn(String identifier);
    void retrieveVariableFamilies(int firstResult, int maxResults, final String criteria);
    void retrieveVariables(int firstResult, int maxResults, final String criteria);
    void saveVariable(VariableDto variableDto);
}
