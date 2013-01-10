package org.siemac.metamac.srm.web.client.code.view.handlers;

import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CodelistFamilyUiHandlers extends UiHandlers {

    void retrieveCodelistFamilyByUrn(String identifier);
    void retrieveCodelistsByFamily(int firstResult, int maxResults, final String criteria, String familyUrn);
    void saveCodelistFamily(CodelistFamilyDto codelistFamilyDto);
    void goToCodelist(String urn);
}
