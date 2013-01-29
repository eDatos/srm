package org.siemac.metamac.srm.web.client.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableUiHandlers extends UiHandlers {

    // Variables

    void retrieveVariableByUrn(String identifier);
    void retrieveVariableFamilies(int firstResult, int maxResults, final String criteria);
    void retrieveVariables(int firstResult, int maxResults, final String criteria);
    void saveVariable(VariableDto variableDto);

    // Variable elements

    void retrieveVariableElementsByVariable(int firstResult, int maxResults, final String criteria, String variableUrn);
    void createVariableElement(VariableElementDto variableElementDto);
    void deleteVariableElements(List<String> urns);
    void goToVariableElement(String urn);

    void createSegregation(String variableElementUrn, List<String> variableElementUrns);
    void createFusion(List<String> variableElementUrn, String variableElementUrns);
    void deleteVariableElementOperations(List<String> codes);
}
