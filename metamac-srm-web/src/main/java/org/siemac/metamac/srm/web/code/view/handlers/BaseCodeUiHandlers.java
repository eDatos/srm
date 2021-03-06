package org.siemac.metamac.srm.web.code.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopy;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;

public interface BaseCodeUiHandlers extends CategorisationUiHandlers {

    void saveCode(CodeMetamacDto codeDto);
    void deleteCode(String codelistUrn, CodeMetamacVisualisationResult code);
    void copyCodesInCodelist(String codelistSourceUrn, String codelistTargetUrn, List<CodeToCopy> codesToCopy);
    void goToCodelist(String urn);
    void goToCode(String urn);

    void retrieveCodelistsForCreateComplexCodelists(int firstResult, int maxResults, CodelistWebCriteria criteria);
    void retrieveCodesForCreateComplexCodelists(String codelistUrn);

    void updateCodeInOrder(String codeUrn, String codelistOrderIdentifier, Integer newCodeIndex);
    void updateCodeParent(String codeUrn, String newParentUrn);
}
