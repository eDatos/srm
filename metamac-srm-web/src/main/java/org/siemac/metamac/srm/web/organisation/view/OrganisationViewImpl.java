package org.siemac.metamac.srm.web.organisation.view;

import org.siemac.metamac.srm.web.organisation.presenter.OrganisationPresenter;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationUiHandlers;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.VLayout;

public class OrganisationViewImpl extends ViewWithUiHandlers<OrganisationUiHandlers> implements OrganisationPresenter.OrganisationView {

    private VLayout panel;

    @Inject
    public OrganisationViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

}
