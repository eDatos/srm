package org.siemac.metamac.srm.web.client.widgets;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CustomToolStripButton extends ToolStripButton {

    private static final String OPERATION_RADIO_GROUP = "operation_radio";

    public CustomToolStripButton() {
        super();
        common();
    }

    public CustomToolStripButton(String title) {
        super(title);
        common();
    }

    private void common() {
        setAutoFit(true);
        setActionType(SelectionType.RADIO);
        setRadioGroup(OPERATION_RADIO_GROUP);
    }

}
