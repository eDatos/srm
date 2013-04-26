package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.utils.ImportationClientSecurityUtils;
import org.siemac.metamac.srm.web.client.view.handlers.MainPageUiHandlers;
import org.siemac.metamac.srm.web.dsd.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.RadioToolStripButton;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class StructuralResourcesMenu extends ToolStrip {

    private RadioToolStripButton  dsdListButton;
    private RadioToolStripButton  conceptsButton;
    private RadioToolStripButton  organisationsButton;
    private RadioToolStripButton  categoriesButton;
    private RadioToolStripButton  codelistsButton;
    private CustomToolStripButton importSDMXResourceButton;

    private MainPageUiHandlers    uiHandlers;

    public StructuralResourcesMenu() {
        super();
        setWidth100();
        setAlign(Alignment.LEFT);

        // CONCEPTS

        conceptsButton = new RadioToolStripButton(getConstants().concepts());
        conceptsButton.setID(ToolStripButtonEnum.CONCEPTS.getValue());
        conceptsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().goToConcepts();
            }
        });
        addButton(conceptsButton);

        // CODELISTS

        codelistsButton = new RadioToolStripButton(getConstants().codelists());
        codelistsButton.setID(ToolStripButtonEnum.CODELISTS.getValue());
        codelistsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().goToCodelists();
            }
        });
        addButton(codelistsButton);

        // DSDS

        dsdListButton = new RadioToolStripButton(getConstants().dsds());
        dsdListButton.setID(ToolStripButtonEnum.DSD_LIST.getValue());
        dsdListButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().goToDsds();
            }
        });
        addButton(dsdListButton);

        addSeparator();

        // ORGANISATIONS

        organisationsButton = new RadioToolStripButton(getConstants().organisations());
        organisationsButton.setID(ToolStripButtonEnum.ORGANISATIONS.getValue());
        organisationsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().goToOrganisations();
            }
        });
        addButton(organisationsButton);

        // CATEGORIES

        categoriesButton = new RadioToolStripButton(getConstants().categories());
        categoriesButton.setID(ToolStripButtonEnum.CATEGORIES.getValue());
        categoriesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().goToCategories();
            }
        });
        addButton(categoriesButton);

        addSeparator();
        addSeparator();

        // IMPORTATION

        // Window

        final ImportSDMXStructureWindow importSDMXStructureWindow = new ImportSDMXStructureWindow();
        importSDMXStructureWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String fileName) {
                getUiHandlers().sDMXResourceImportationFailed(fileName);
            }
            @Override
            public void uploadComplete(String fileName) {
                getUiHandlers().sDMXResourceImportationSucceed(fileName);
            }
        });

        // Button

        importSDMXResourceButton = new CustomToolStripButton(getConstants().actionImportSDMXResource(), GlobalResources.RESOURCE.importResource().getURL());
        importSDMXResourceButton.setVisibility(ImportationClientSecurityUtils.canImportStructure() ? Visibility.VISIBLE : Visibility.HIDDEN);
        importSDMXResourceButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importSDMXStructureWindow.show();
            }
        });
        addButton(importSDMXResourceButton);
    }

    public void setUiHandlers(MainPageUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public MainPageUiHandlers getUiHandlers() {
        return this.uiHandlers;
    }
}
