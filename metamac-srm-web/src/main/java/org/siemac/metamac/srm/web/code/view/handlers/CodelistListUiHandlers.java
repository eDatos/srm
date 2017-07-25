package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public interface CodelistListUiHandlers extends BaseUiHandlers {

    void goToCodelist(String urn);
    void createCodelist(CodelistMetamacDto codelistMetamacDto);
    void deleteCodelists(List<String> urns);
    void retrieveCodelists(int firstResult, int maxResults, CodelistWebCriteria criteria);
    void exportCodelists(List<String> urns, ExportDetailEnum infoAmount, ExportReferencesEnum references);
    void cancelValidity(List<String> urns);

    void retrieveVariables(int firstResult, int maxResults, VariableWebCriteria criteria);

    void retrieveVariablesForSearch(int firstResult, int maxResults, VariableWebCriteria criteria);
    void retrieveVariableElementsForSearch(int firstResult, int maxResults, MetamacWebCriteria criteria);

    void retrieveVariableFamiliesForSearch(int firstResult, int maxResults, String criteria);
}
