package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class ConfirmationWindow extends CustomWindow {

    private IButton yesButton;
    private IButton noButton;

    public ConfirmationWindow(String title, String questionMessage) {
        super(title);
        setHeight(170);

        Label label = new Label(questionMessage);
        label.setAutoHeight();
        label.setIcon(GlobalResources.RESOURCE.ask().getURL());
        label.setIconSize(32);
        label.setIconSpacing(10);

        yesButton = new IButton(MetamacWebCommon.getConstants().yes());
        yesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConfirmationWindow.this.destroy();
            }
        });

        noButton = new IButton(MetamacWebCommon.getConstants().no());
        noButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConfirmationWindow.this.destroy();
            }
        });

        HLayout buttonsLayout = new HLayout(2);
        buttonsLayout.addMember(yesButton);
        buttonsLayout.addMember(noButton);
        buttonsLayout.setAlign(Alignment.CENTER);

        VLayout layout = new VLayout();
        layout.setMembersMargin(20);
        layout.addMember(label);
        layout.addMember(buttonsLayout);
        layout.setMargin(10);

        addItem(layout);
        show();
    }

    public HasClickHandlers getYesButton() {
        return yesButton;
    }

    public HasClickHandlers getNoButton() {
        return noButton;
    }
}
