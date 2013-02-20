package org.siemac.metamac.srm.web.code.view.handlers;

import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CodesUiHandlers extends UiHandlers {

    void goToCode(String codelistUrn, String codeUrn);
    void retrieveCodes(int firstResult, int maxResults, CodeWebCriteria criteria);
}
