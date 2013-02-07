package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.widgets.RadioToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class CodesToolStripViewImpl implements CodesToolStripPresenterWidget.CodesToolStripView {

    private ToolStrip            toolStrip;

    private RadioToolStripButton codelistFamiliesButton;
    private RadioToolStripButton codelistsButton;
    private RadioToolStripButton variableFamiliesButton;
    private RadioToolStripButton variablesButton;

    @Inject
    public CodesToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setHeight(25);
        toolStrip.setAlign(Alignment.LEFT);

        codelistFamiliesButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().codelistFamilies());

        codelistsButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().codelists());

        variableFamiliesButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().variableFamilies());

        variablesButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().variables());

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
