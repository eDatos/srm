package org.siemac.metamac.srm.web.client.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableListUiHandlers extends UiHandlers {

    void goToVariable(String urn);
    void createVariable(VariableDto variableDto);
    void deleteVariables(List<String> urns);
    void retrieveVariables(int firstResult, int maxResults, String criteria);
}
