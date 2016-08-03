package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CodelistFamilyUiHandlers extends UiHandlers {

    void retrieveCodelistFamilyByUrn(String identifier);
    void retrieveCodelists(int firstResult, int maxResults, final String criteria, boolean isLastVersion);
    void retrieveCodelistsByFamily(int firstResult, int maxResults, final String criteria, String familyUrn);
    void saveCodelistFamily(CodelistFamilyDto codelistFamilyDto);
    void addCodelistsToFamily(List<String> codelists, String familyUrn);
    void removeCodelistsFromFamily(List<String> codelistUrns, String familyUrn);
    void goToCodelist(String urn);
    void deleteCodelistFamily(String urn);
}
