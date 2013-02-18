package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;

public class ConceptSchemeSearchSectionStack extends VersionableResourceSearchSectionStack {

    private ConceptSchemeListUiHandlers uiHandlers;

    public ConceptSchemeSearchSectionStack() {
    }

    public ConceptSchemeWebCriteria getConceptSchemeWebCriteria() {
        return (ConceptSchemeWebCriteria) getVersionableResourceWebCriteria(new ConceptSchemeWebCriteria());
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveConceptSchemes(ConceptSchemeListPresenter.SCHEME_LIST_FIRST_RESULT, ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, getConceptSchemeWebCriteria());
    }

    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public ConceptSchemeListUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
