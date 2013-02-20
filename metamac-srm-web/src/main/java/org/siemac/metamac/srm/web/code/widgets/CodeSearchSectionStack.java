package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.web.client.widgets.ItemSearchSectionStack;
import org.siemac.metamac.srm.web.code.presenter.CodesPresenter;
import org.siemac.metamac.srm.web.code.view.handlers.CodesUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;

public class CodeSearchSectionStack extends ItemSearchSectionStack {

    private CodesUiHandlers uiHandlers;

    public CodeSearchSectionStack() {
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCodes(CodesPresenter.ITEM_LIST_FIRST_RESULT, CodesPresenter.ITEM_LIST_MAX_RESULTS, getCodeWebCriteria());
    }

    public CodeWebCriteria getCodeWebCriteria() {
        return (CodeWebCriteria) getItemWebCriteria(new CodeWebCriteria());
    }

    public void setUiHandlers(CodesUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodesUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
