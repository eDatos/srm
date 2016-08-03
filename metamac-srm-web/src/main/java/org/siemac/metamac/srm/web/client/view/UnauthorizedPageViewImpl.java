package org.siemac.metamac.srm.web.client.view;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.presenter.UnauthorizedPagePresenter;
import org.siemac.metamac.srm.web.client.view.handlers.UnauthorizedPageUiHandlers;
import org.siemac.metamac.web.common.client.MetamacWebCommon;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class UnauthorizedPageViewImpl extends ViewWithUiHandlers<UnauthorizedPageUiHandlers> implements UnauthorizedPagePresenter.UnauthorizedPageView {

    private VLayout panel;

    public UnauthorizedPageViewImpl() {
        super();
        panel = new VLayout();

        HLayout hLayout = new HLayout();
        hLayout.setAlign(Alignment.CENTER);
        hLayout.setMargin(20);

        HTMLFlow htmlFlow = new HTMLFlow();
        htmlFlow.setWidth(430);
        htmlFlow.setMargin(40);
        htmlFlow.setStyleName("exampleTextBlock");
        StringBuffer contents = new StringBuffer();
        contents.append("<hr>").append(MetamacWebCommon.getMessages().applicationAccessDenied(MetamacSrmWeb.getCurrentUser().getUserId())).append("<hr>");
        htmlFlow.setContents(contents.toString());
        htmlFlow.draw();

        hLayout.addMember(htmlFlow);

        panel.addMember(hLayout);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

}
