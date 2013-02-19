package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.code.utils.CodesToolStripButtonEnum;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.RadioToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodesToolStripViewImpl implements CodesToolStripPresenterWidget.CodesToolStripView {

    private final static String   RADIO_GROUP = "codes-radio-group";

    private ToolStrip             toolStrip;

    private CustomToolStripButton codelistFamiliesButton;
    private CustomToolStripButton codelistsButton;
    private CustomToolStripButton variableFamiliesButton;
    private CustomToolStripButton variablesButton;

    @Inject
    public CodesToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setHeight(25);
        toolStrip.setAlign(Alignment.LEFT);

        codelistFamiliesButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().codelistFamilies());
        codelistFamiliesButton.setID(CodesToolStripButtonEnum.CODELIST_FAMILIES.getValue());
        codelistFamiliesButton.setActionType(SelectionType.RADIO);
        codelistFamiliesButton.setRadioGroup(RADIO_GROUP);

        codelistsButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().codelists());
        codelistsButton.setID(CodesToolStripButtonEnum.CODELISTS.getValue());
        codelistsButton.setActionType(SelectionType.RADIO);
        codelistsButton.setRadioGroup(RADIO_GROUP);

        variableFamiliesButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().variableFamilies());
        variableFamiliesButton.setID(CodesToolStripButtonEnum.VARIABLE_FAMILIES.getValue());
        variableFamiliesButton.setActionType(SelectionType.RADIO);
        variableFamiliesButton.setRadioGroup(RADIO_GROUP);

        variablesButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().variables());
        variablesButton.setID(CodesToolStripButtonEnum.VARIABLES.getValue());
        variablesButton.setActionType(SelectionType.RADIO);
        variablesButton.setRadioGroup(RADIO_GROUP);

        toolStrip.addButton(codelistFamiliesButton);
        toolStrip.addButton(codelistsButton);
        toolStrip.addButton(variableFamiliesButton);
        toolStrip.addButton(variablesButton);
    }

    @Override
    public Widget asWidget() {
        return toolStrip;
    }

    @Override
    public void selectButton(CodesToolStripButtonEnum button) {
        Canvas[] canvas = toolStrip.getMembers();
        for (int i = 0; i < canvas.length; i++) {
            if (canvas[i] instanceof ToolStripButton) {
                if (button != null && button.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                    ((ToolStripButton) canvas[i]).select();
                } else {
                    ((ToolStripButton) canvas[i]).deselect();
                }
            }
        }
    }

    @Override
    public void addToSlot(Object slot, Widget content) {
    }

    @Override
    public void removeFromSlot(Object slot, Widget content) {
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
    }

    @Override
    public HasClickHandlers getCodelistFamiliesButton() {
        return codelistFamiliesButton;
    }

    @Override
    public HasClickHandlers getCodelistsButton() {
        return codelistsButton;
    }

    @Override
    public HasClickHandlers getVariableFamiliesButton() {
        return variableFamiliesButton;
    }

    @Override
    public HasClickHandlers getVariablesButton() {
        return variablesButton;
    }
}
