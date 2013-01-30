package org.siemac.metamac.srm.web.client.code.view.handlers;

import org.siemac.metamac.srm.core.code.dto.VariableElementDto;

public interface VariableElementUiHandlers extends BaseVariableUiHandlers {

    void saveVariableElement(VariableElementDto variableElementDto);

    void retrieveVariableElementsByVariableForReplaceTo(int firstResult, int maxResults, final String criteria, String variableUrn);
    void retrieveVariableElementsByVariableForSegregationOperation(int firstResult, int maxResults, final String criteria, String variableUrn);
}
