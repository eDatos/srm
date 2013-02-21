package org.siemac.metamac.srm.web.code.view.handlers;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.web.client.view.handlers.CategorisationUiHandlers;

public interface BaseCodeUiHandlers extends CategorisationUiHandlers {

    void saveCode(CodeMetamacDto codeDto);
    void deleteCode(String codelistUrn, CodeMetamacVisualisationResult code);
    void goToCode(String urn);

    void updateCodeInOrder(String codeUrn, String codelistOrderIdentifier, Long newCodeIndex);
    void updateCodeParent(String codeUrn, String newParentUrn, String codelistOrderIdentifier);
}
