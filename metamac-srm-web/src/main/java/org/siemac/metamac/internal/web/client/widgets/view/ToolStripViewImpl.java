package org.siemac.metamac.internal.web.client.widgets.view;

import org.siemac.metamac.internal.web.client.MetamacInternalWeb;
import org.siemac.metamac.internal.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.internal.web.client.widgets.CustomToolStripButton;
import org.siemac.metamac.internal.web.client.widgets.presenter.ToolStripPresenterWidget;

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

    @Inject
    public ToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setAlign(Alignment.LEFT);

        // ToolStripButton
        dsdListButton = new CustomToolStripButton(MetamacInternalWeb.getConstants().dsds());
        dsdListButton.setID(ToolStripButtonEnum.DSD_LIST.getValue());

        conceptsButton = new CustomToolStripButton(MetamacInternalWeb.getConstants().conceptSchemes());
        conceptsButton.setID(ToolStripButtonEnum.CONCEPTS.getValue());

        organisationsButton = new CustomToolStripButton(MetamacInternalWeb.getConstants().organisationSchemes());
        organisationsButton.setID(ToolStripButtonEnum.ORGANISATIONS.getValue());

        classificationsButton = new CustomToolStripButton(MetamacInternalWeb.getConstants().classifications());
        classificationsButton.setID(ToolStripButtonEnum.CLASSIFICATIONS.getValue());

        categoriesButton = new CustomToolStripButton(MetamacInternalWeb.getConstants().categorySchemes());
        categoriesButton.setID(ToolStripButtonEnum.CATEGORIES.getValue());

        // Add buttons to toolStrip
        toolStrip.addButton(dsdListButton);
        toolStrip.addButton(conceptsButton);
        toolStrip.addButton(organisationsButton);
        toolStrip.addButton(classificationsButton);
        toolStrip.addButton(categoriesButton);
    }

    @Override
    public void addToSlot(Object slot, Widget content) {
        System.out.println();
    }

    @Override
    public Widget asWidget() {
        return toolStrip;
    }

    @Override
    public void removeFromSlot(Object slot, Widget content) {

    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        System.out.println();
    }

    @Override
    public HasClickHandlers getDsdButton() {
        return dsdListButton;
    }

    @Override
    public HasClickHandlers getConceptSchemeButton() {
        return conceptsButton;
    }

}
