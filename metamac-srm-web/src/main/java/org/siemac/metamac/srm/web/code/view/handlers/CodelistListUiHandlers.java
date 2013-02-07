package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CodelistListUiHandlers extends UiHandlers {

    void goToCodelist(String urn);
    void createCodelist(CodelistMetamacDto codelistMetamacDto);
    void deleteCodelists(List<String> urns);
    void retrieveCodelists(int firstResult, int maxResults, String criteria);
    void cancelValidity(List<String> urns);

    void retrieveVariables(int firstResult, int maxResults, String criteria);
}
