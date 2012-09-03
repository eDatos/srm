package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public class ConceptsTreeGrid extends TreeGrid {

    private NewConceptWindow         newConceptWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    private Menu                     contextMenu;

    private MenuItem                 createConceptMenuItem;
    private MenuItem                 deleteConceptMenuItem;

    private String                   conceptSchemeUrn;
    private String                   selectedConceptUrn;

    private BaseConceptUiHandlers    uiHandlers;

    public ConceptsTreeGrid() {
        setHeight(200);
        setShowOpenIcons(false);
        setShowDropIcons(false);
        setShowSelectedStyle(true);
        setShowPartialSelection(true);
        setCascadeSelection(false);
        setCanSort(false);
        setShowConnectors(true);
        setShowHeader(true);
        setLoadDataOnDemand(false);
        setSelectionType(SelectionStyle.SINGLE);
        setShowCellContextMenus(true);
        setLeaveScrollbarGap(Boolean.FALSE);

        TreeGridField codeField = new TreeGridField(ConceptDS.CODE, getConstants().conceptCode());
        codeField.setWidth("30%");
        TreeGridField nameField = new TreeGridField(ConceptDS.NAME, getConstants().conceptName());

        setFields(codeField, nameField);

        // Menu

        createConceptMenuItem = new MenuItem(MetamacSrmWeb.getConstants().conceptCreate());
        createConceptMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newConceptWindow = new NewConceptWindow(MetamacSrmWeb.getConstants().conceptCreate());
                newConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptWindow.validateForm()) {
                            ConceptMetamacDto conceptMetamacDto = newConceptWindow.getNewConceptDto();
                            conceptMetamacDto.setItemSchemeVersionUrn(conceptSchemeUrn); // Set concept scheme URN
                            conceptMetamacDto.setItemParentUrn(selectedConceptUrn); // Set concept parent URN
                            ConceptsTreeGrid.this.uiHandlers.createConcept(conceptMetamacDto);
                            newConceptWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteConceptMenuItem = new MenuItem(MetamacSrmWeb.getConstants().conceptDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().conceptDeleteTitle(), MetamacSrmWeb.getConstants().conceptDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConceptsTreeGrid.this.uiHandlers.deleteConcept(selectedConceptUrn);
            }
        });
        deleteConceptMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        contextMenu = new Menu();
        contextMenu.addItem(createConceptMenuItem);
        contextMenu.addItem(deleteConceptMenuItem);

        addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(FolderContextClickEvent event) {
                selectedConceptUrn = event.getFolder().getAttribute(ConceptDS.URN);
                contextMenu.showContextMenu();
            }
        });

        addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                selectedConceptUrn = event.getLeaf().getAttribute(ConceptDS.URN);
                contextMenu.showContextMenu();
            }
        });
    }

    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setConceptSchemeUrn(String urn) {
        this.conceptSchemeUrn = urn;
    }

}
