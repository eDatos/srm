package org.siemac.metamac.srm.web.dsd.widgets;

import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;

public class DsdSearchSectionStack extends VersionableResourceSearchSectionStack {

    private DsdListUiHandlers uiHandlers;

    public DsdSearchSectionStack() {
    }

    public DataStructureDefinitionWebCriteria getDataStructureDefinitionWebCriteria() {
        return (DataStructureDefinitionWebCriteria) getVersionableResourceWebCriteria(new DataStructureDefinitionWebCriteria());
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveDsdList(DsdListPresenter.DSD_LIST_FIRST_RESULT, DsdListPresenter.DSD_LIST_MAX_RESULTS, getDataStructureDefinitionWebCriteria());
    }

    public void setUiHandlers(DsdListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public DsdListUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
