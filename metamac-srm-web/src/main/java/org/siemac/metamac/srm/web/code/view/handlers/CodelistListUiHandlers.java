package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface CodelistListUiHandlers extends BaseUiHandlers {

    void goToCodelist(String urn);
    void createCodelist(CodelistMetamacDto codelistMetamacDto);
    void deleteCodelists(List<String> urns);
    void retrieveCodelists(int firstResult, int maxResults, CodelistWebCriteria criteria);
    void cancelValidity(List<String> urns);

    void retrieveVariables(int firstResult, int maxResults, String criteria);
}
