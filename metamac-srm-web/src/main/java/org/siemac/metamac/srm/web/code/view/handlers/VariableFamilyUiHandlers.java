package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableFamilyUiHandlers extends UiHandlers {

    void retrieveVariableFamilyByUrn(String identifier);
    void retrieveVariables(int firstResult, int maxResults, final String criteria);
    void retrieveVariablesByFamily(int firstResult, int maxResults, final String criteria, String familyUrn);
    void saveVariableFamily(VariableFamilyDto variableFamilyDto);
    void addVariablesToFamily(List<String> variableUrns, String familyUrn);
    void removeVariablesFromFamily(List<String> variableUrns, String familyUrn);
    void goToVariable(String urn);
}
