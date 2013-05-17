package org.siemac.metamac.srm.web.code.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface CodesUiHandlers extends BaseUiHandlers {

    void goToCode(String codelistUrn, String codeUrn);
    void retrieveCodes(int firstResult, int maxResults, CodeWebCriteria criteria);
}
