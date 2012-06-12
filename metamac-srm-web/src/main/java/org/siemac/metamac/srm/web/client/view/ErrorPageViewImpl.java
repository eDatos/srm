package org.siemac.metamac.srm.web.client.view;

import org.siemac.metamac.srm.web.client.presenter.ErrorPagePresenter;
import org.siemac.metamac.srm.web.client.view.handlers.ErrorPageUiHandlers;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

public class ErrorPageViewImpl extends ViewWithUiHandlers<ErrorPageUiHandlers> implements ErrorPagePresenter.ErrorPageView {

    private VLayout panel;

    public ErrorPageViewImpl() {
        super();
        panel = new VLayout();
        panel.addMember(new Label("Error"));
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

}
