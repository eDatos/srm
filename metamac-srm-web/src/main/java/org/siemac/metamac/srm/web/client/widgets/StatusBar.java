package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;

public class StatusBar extends HLayout {

    private static final String STATUSBAR_HEIGHT = "23px";

    private static final int    BUTTON_SIZE      = 12;
    private static final int    LABEL_WIDTH      = 65;

    private final Label         selectedLabel;
    private final ImgButton     resultSetFirstButton;
    private final ImgButton     resultSetLastButton;
    private final ImgButton     resultSetPreviousButton;
    private final Label         pageNumberLabel;
    private final ImgButton     resultSetNextButton;

    public StatusBar() {
        super();

        // initialize the StatusBar layout container
        this.setStyleName("statusBar");
        this.setHeight(STATUSBAR_HEIGHT);

        // initialize the Selected label
        selectedLabel = new Label();
        selectedLabel.setStyleName("statusBar-Label");
        selectedLabel.setContents(MetamacSrmWeb.getMessages().selected(String.valueOf(0), String.valueOf(50)));
        selectedLabel.setAlign(Alignment.LEFT);
        selectedLabel.setOverflow(Overflow.HIDDEN);

        // initialize the Result Set First button
        resultSetFirstButton = new ImgButton();
        resultSetFirstButton.setShowRollOver(false);
        resultSetFirstButton.setShowDisabled(true);
        resultSetFirstButton.setShowDown(false);
        resultSetFirstButton.setSize(BUTTON_SIZE);
        resultSetFirstButton.setLayoutAlign(VerticalAlignment.CENTER);
        resultSetFirstButton.setSrc(GlobalResources.RESOURCE.resultSetFirst().getURL());
        // requires resultsetfirst_Disabled.png
        resultSetFirstButton.disable();
        resultSetFirstButton.setAlign(Alignment.RIGHT);

        // initialize the Result Set First button
        resultSetLastButton = new ImgButton();
        resultSetLastButton.setShowRollOver(false);
        resultSetLastButton.setShowDisabled(true);
        resultSetLastButton.setShowDown(false);
        resultSetLastButton.setSize(BUTTON_SIZE);
        resultSetLastButton.setLayoutAlign(VerticalAlignment.CENTER);
        resultSetLastButton.setSrc(GlobalResources.RESOURCE.resultSetLast().getURL());
        // requires resultsetlast_Disabled.png
        resultSetLastButton.disable();
        resultSetLastButton.setAlign(Alignment.RIGHT);

        // initialize the Result Set Previous button
        resultSetPreviousButton = new ImgButton();
        resultSetPreviousButton.setShowRollOver(false);
        resultSetPreviousButton.setShowDisabled(true);
        resultSetPreviousButton.setShowDown(false);
        resultSetPreviousButton.setSize(BUTTON_SIZE);
        resultSetPreviousButton.setLayoutAlign(VerticalAlignment.CENTER);
        resultSetPreviousButton.setSrc(GlobalResources.RESOURCE.resultSetPrevious().getURL());
        // requires resultsetprevious_Disabled.png
        resultSetPreviousButton.disable();
        resultSetPreviousButton.setAlign(Alignment.RIGHT);

        // initialize the Page Number label
        pageNumberLabel = new Label();
        pageNumberLabel.setStyleName("statusBar-Label");
        pageNumberLabel.setContents(MetamacSrmWeb.getMessages().page(String.valueOf(1)));

        // TO DO: fix this
        pageNumberLabel.setWidth(LABEL_WIDTH);
        // pageNumberLabel.setWidth("*");
        pageNumberLabel.setAlign(Alignment.RIGHT);
        pageNumberLabel.setOverflow(Overflow.HIDDEN);

        // initialize the Result Set Next button
        resultSetNextButton = new ImgButton();
        resultSetNextButton.setShowRollOver(false);
        resultSetNextButton.setShowDisabled(true);
        resultSetNextButton.setShowDown(false);
        resultSetNextButton.setSize(BUTTON_SIZE);
        resultSetNextButton.setLayoutAlign(VerticalAlignment.CENTER);
        resultSetNextButton.setSrc(GlobalResources.RESOURCE.resultSetNext().getURL());
        // requires resultsetnext_Disabled.png
        resultSetNextButton.disable();
        resultSetNextButton.setAlign(Alignment.RIGHT);

        // add the widgets to the StatusBar layout container
        this.addMember(selectedLabel);
        // force right alignment
        Label alignRight = new Label("&nbsp;");
        alignRight.setAlign(Alignment.RIGHT);
        alignRight.setOverflow(Overflow.HIDDEN);
        this.addMember(alignRight);
        this.addMember(resultSetFirstButton);
        this.addMember(resultSetPreviousButton);
        this.addMember(pageNumberLabel);
        this.addMember(resultSetNextButton);
        this.addMember(resultSetLastButton);
        // add some padding
        LayoutSpacer paddingRight = new LayoutSpacer();
        paddingRight.setWidth(8);
        this.addMember(paddingRight);
    }

    public Label getSelectedLabel() {
        return selectedLabel;
    }

    public ImgButton getResultSetFirstButton() {
        return resultSetFirstButton;
    }

    public ImgButton getResultSetLastButton() {
        return resultSetLastButton;
    }

    public ImgButton getResultSetPreviousButton() {
        return resultSetPreviousButton;
    }

    public ImgButton getResultSetNextButton() {
        return resultSetNextButton;
    }

    public Label getPageNumberLabel() {
        return pageNumberLabel;
    }

}
