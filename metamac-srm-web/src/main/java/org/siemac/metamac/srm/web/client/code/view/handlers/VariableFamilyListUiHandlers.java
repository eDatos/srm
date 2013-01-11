package org.siemac.metamac.srm.web.client.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface VariableFamilyListUiHandlers extends UiHandlers {

    void goToVariableFamily(String urn);
    void createVariableFamily(VariableFamilyDto variableFamilyDto);
    void deleteVariableFamilies(List<String> urns);
    void retrieveVariableFamilies(int firstResult, int maxResults, String criteria);
}
