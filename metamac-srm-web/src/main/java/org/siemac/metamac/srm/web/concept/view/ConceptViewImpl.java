package org.siemac.metamac.srm.web.concept.view;

import org.siemac.metamac.srm.web.concept.presenter.ConceptPresenter;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.layout.VLayout;

public class ConceptViewImpl extends ViewImpl implements ConceptPresenter.ConceptView {

    private ConceptUiHandlers uiHandlers;

    private VLayout           panel;

    @Inject
    public ConceptViewImpl() {
        super();
        panel = new VLayout();
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

}
