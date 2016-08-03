package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;

public interface VariableUiHandlers extends BaseVariableUiHandlers {

    // Variables

    void retrieveVariableByUrn(String identifier);
    void retrieveVariableFamilies(int firstResult, int maxResults, final String criteria);
    void retrieveVariables(int firstResult, int maxResults, final VariableWebCriteria criteria);
    void saveVariable(VariableDto variableDto);
    void deleteVariable(String urn);

    // Variable elements

    void retrieveVariableElementsByVariable(int firstResult, int maxResults, final String criteria, String variableUrn);
    void createVariableElement(VariableElementDto variableElementDto);
    void deleteVariableElements(List<String> urns);
    void goToVariableElement(String urn);

    void retrieveVariableElementsByVariableForFusionOperation(int firstResult, int maxResults, final String criteria, String variableUrn);
    void retrieveVariableElementsByVariableForSegregationOperation(int firstResult, int maxResults, final String criteria, String variableUrn);

    // Importation

    void resourceImportationFailed(String errorMessage);
    void resourceImportationSucceed(String fileName);

    // Exportation

    void exportVariableElements(String variableUrn);

    void showWaitPopup();
}
