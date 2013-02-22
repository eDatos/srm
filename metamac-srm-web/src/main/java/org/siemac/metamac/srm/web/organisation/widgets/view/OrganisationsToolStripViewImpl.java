package org.siemac.metamac.srm.web.organisation.widgets.view;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.organisation.enums.OrganisationsToolStripButtonEnum;
import org.siemac.metamac.srm.web.organisation.widgets.presenter.OrganisationsToolStripPresenterWidget;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class OrganisationsToolStripViewImpl implements OrganisationsToolStripPresenterWidget.OrganisationsToolStripView {

    private final static String   RADIO_GROUP = "org-radio-group";

    private ToolStrip             toolStrip;

    private CustomToolStripButton organisationSchemesButton;
    private CustomToolStripButton organisationsButton;

    @Inject
    public OrganisationsToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setHeight(25);
        toolStrip.setAlign(Alignment.LEFT);

        organisationSchemesButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().organisationSchemes());
        organisationSchemesButton.setID(OrganisationsToolStripButtonEnum.ORGANISATION_SCHEMES.getValue());
        organisationSchemesButton.setActionType(SelectionType.RADIO);
        organisationSchemesButton.setRadioGroup(RADIO_GROUP);

        organisationsButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().organisations());
        organisationsButton.setID(OrganisationsToolStripButtonEnum.ORGANISATIONS.getValue());
        organisationsButton.setActionType(SelectionType.RADIO);
        organisationsButton.setRadioGroup(RADIO_GROUP);

        toolStrip.addButton(organisationSchemesButton);
        toolStrip.addButton(organisationsButton);
    }

    @Override
    public Widget asWidget() {
        return toolStrip;
    }

    @Override
    public void selectButton(OrganisationsToolStripButtonEnum button) {
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
    public HasClickHandlers getOrganisationSchemesButton() {
        return organisationSchemesButton;
    }

    @Override
    public HasClickHandlers getOrganisationButton() {
        return organisationsButton;
    }
}
