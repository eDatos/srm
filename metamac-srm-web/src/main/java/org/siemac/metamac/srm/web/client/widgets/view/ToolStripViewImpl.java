package org.siemac.metamac.srm.web.client.widgets.view;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class ToolStripViewImpl implements ToolStripPresenterWidget.ToolStripView {

    private ToolStrip             toolStrip;

    private CustomToolStripButton dsdListButton;
    private CustomToolStripButton conceptsButton;
    private CustomToolStripButton organisationsButton;
    private CustomToolStripButton classificationsButton;
    private CustomToolStripButton categoriesButton;
    private CustomToolStripButton codelistsButton;

    @Inject
    public ToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setAlign(Alignment.LEFT);

        conceptsButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().concepts());
        conceptsButton.setID(ToolStripButtonEnum.CONCEPTS.getValue());

        classificationsButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().codelists());
        classificationsButton.setID(ToolStripButtonEnum.CODELISTS.getValue());

        dsdListButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().dsds());
        dsdListButton.setID(ToolStripButtonEnum.DSD_LIST.getValue());

        organisationsButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().organisations());
        organisationsButton.setID(ToolStripButtonEnum.ORGANISATIONS.getValue());

        categoriesButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().categories());
        categoriesButton.setID(ToolStripButtonEnum.CATEGORIES.getValue());

        codelistsButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().codelists());
        codelistsButton.setID(ToolStripButtonEnum.CODELISTS.getValue());

        toolStrip.addButton(conceptsButton);
        toolStrip.addButton(classificationsButton);
        toolStrip.addButton(dsdListButton);
        toolStrip.addSeparator();
        toolStrip.addButton(organisationsButton);
        toolStrip.addButton(categoriesButton);
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
    public HasClickHandlers getDsdsButton() {
        return dsdListButton;
    }

    @Override
    public HasClickHandlers getConceptSchemesButton() {
        return conceptsButton;
    }

    @Override
    public HasClickHandlers getOrganisationSchemesButton() {
        return organisationsButton;
    }

    @Override
    public HasClickHandlers getCategorySchemesButton() {
        return categoriesButton;
    }

    @Override
    public HasClickHandlers getCodelistButton() {
        return codelistsButton;
    }
}
