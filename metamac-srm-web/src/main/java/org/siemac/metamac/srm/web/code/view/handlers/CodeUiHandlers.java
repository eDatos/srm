package org.siemac.metamac.srm.web.code.view.handlers;

public interface CodeUiHandlers extends BaseCodeUiHandlers {

    void retrieveCode(String codeUrn);
    void retrieveCodesByCodelist(String codelistUrn);
    void updateVariableElement(String codeUrn, String variableElementUrn);

    void retrieveVariableElements(int firstResult, int maxResults, String criteria, String codelistUrn);
}
