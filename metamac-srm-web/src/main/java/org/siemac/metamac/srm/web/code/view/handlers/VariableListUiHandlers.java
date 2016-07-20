package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableListUiHandlers extends UiHandlers {

    void goToVariable(String urn);
    void createVariable(VariableDto variableDto);
    void deleteVariables(List<String> urns);
    void retrieveVariables(int firstResult, int maxResults, VariableWebCriteria criteria);

    void retrieveVariableFamilies(int firstResult, int maxResults, String criteria);
}
