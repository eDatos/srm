package org.siemac.metamac.srm.web.client.code.view.handlers;

public interface CodeUiHandlers extends BaseCodeUiHandlers {

    void retrieveCode(String codeUrn);
    void retrieveCodesByCodelist(String codelistUrn);

    void retrieveVariableElements(int firstResult, int maxResults, String criteria, String codelistUrn);
}
