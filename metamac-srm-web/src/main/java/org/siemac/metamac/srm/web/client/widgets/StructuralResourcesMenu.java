package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.web.common.client.widgets.RadioToolStripButton;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class StructuralResourcesMenu extends ToolStrip {

    private RadioToolStripButton dsdListButton;
    private RadioToolStripButton conceptsButton;
    private RadioToolStripButton organisationsButton;
    private RadioToolStripButton categoriesButton;
    private RadioToolStripButton codelistsButton;

    public StructuralResourcesMenu() {
        super();
        setWidth100();
        setAlign(Alignment.LEFT);

        conceptsButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().concepts());
        conceptsButton.setID(ToolStripButtonEnum.CONCEPTS.getValue());

        codelistsButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().codelists());
        codelistsButton.setID(ToolStripButtonEnum.CODELISTS.getValue());

        dsdListButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().dsds());
        dsdListButton.setID(ToolStripButtonEnum.DSD_LIST.getValue());

        organisationsButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().organisations());
        organisationsButton.setID(ToolStripButtonEnum.ORGANISATIONS.getValue());

        categoriesButton = new RadioToolStripButton(MetamacSrmWeb.getConstants().categories());
        categoriesButton.setID(ToolStripButtonEnum.CATEGORIES.getValue());

        addButton(conceptsButton);
        addButton(codelistsButton);
        addButton(dsdListButton);
        addSeparator();
        addButton(organisationsButton);
        addButton(categoriesButton);
    }

    public HasClickHandlers getDsdsButton() {
        return dsdListButton;
    }

    public HasClickHandlers getConceptSchemesButton() {
        return conceptsButton;
    }

    public HasClickHandlers getOrganisationSchemesButton() {
        return organisationsButton;
    }

    public HasClickHandlers getCategorySchemesButton() {
        return categoriesButton;
    }

    public HasClickHandlers getCodelistButton() {
        return codelistsButton;
    }
}
