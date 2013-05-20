package org.siemac.metamac.srm.web.code.view.handlers;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;

public interface CodeUiHandlers extends BaseCodeUiHandlers {

    void retrieveCode(String codeUrn);
    void retrieveCodesByCodelist(String codelistUrn);
    void updateVariableElement(String codeUrn, String variableElementUrn);
    void deleteCode(CodeMetamacDto codeMetamacDto);

    void retrieveVariableElements(int firstResult, int maxResults, String criteria, String codelistUrn);
}
