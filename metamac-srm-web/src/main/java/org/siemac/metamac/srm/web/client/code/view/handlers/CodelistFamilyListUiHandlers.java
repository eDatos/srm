package org.siemac.metamac.srm.web.client.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CodelistFamilyListUiHandlers extends UiHandlers {

    void goToCodelistFamily(String urn);
    void createCodelistFamily(CodelistFamilyDto codelistFamilyDto);
    void deleteCodelistFamilies(List<String> urns);
    void retrieveCodelistFamilies(int firstResult, int maxResults, String criteria);
}
