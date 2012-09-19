package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public class ConceptsTreeGrid extends TreeGrid {

    private static final String      SCHEME_NODE_NAME = "scheme-node";

    private NewConceptWindow         newConceptWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    private Menu                     contextMenu;

    private MenuItem                 createConceptMenuItem;
    private MenuItem                 deleteConceptMenuItem;

    private ConceptSchemeMetamacDto  conceptSchemeMetamacDto;
    private String                   selectedConceptUrn;

    private BaseConceptUiHandlers    uiHandlers;

    private HandlerRegistration      folderContextHandlerRegistration;
    private HandlerRegistration      leafContextHandlerRegistration;
    private HandlerRegistration      folderClickHandlerRegistration;
    private HandlerRegistration      leafClickHandlerRegistration;

    public ConceptsTreeGrid() {

        setAutoFitMaxRecords(10);
        setAutoFitData(Autofit.VERTICAL);

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
        TreeGridField type = new TreeGridField(ConceptDS.TYPE, getConstants().conceptType());
        TreeGridField sdmxRelatedArtefact = new TreeGridField(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());

        setFields(codeField, nameField, type, sdmxRelatedArtefact);

        // Menu

        createConceptMenuItem = new MenuItem(MetamacSrmWeb.getConstants().conceptCreate());
        createConceptMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newConceptWindow = new NewConceptWindow(MetamacSrmWeb.getConstants().conceptCreate(), conceptSchemeMetamacDto.getType());
                newConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptWindow.validateForm()) {
                            ConceptMetamacDto conceptMetamacDto = newConceptWindow.getNewConceptDto();
                            conceptMetamacDto.setItemSchemeVersionUrn(conceptSchemeMetamacDto.getUrn()); // Set concept scheme URN
                            conceptMetamacDto.setItemParentUrn(selectedConceptUrn); // Set concept parent URN
                            ConceptsTreeGrid.this.uiHandlers.saveConcept(conceptMetamacDto);
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

        // Bind events

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                selectedConceptUrn = event.getFolder().getAttribute(ConceptDS.URN);
                deleteConceptMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(event.getFolder().getName()));
                showContextMenu();
            }
        });

        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                selectedConceptUrn = event.getLeaf().getAttribute(ConceptDS.URN);
                deleteConceptMenuItem.setEnabled(!SCHEME_NODE_NAME.equals(event.getLeaf().getName()));
                showContextMenu();
            }
        });

        folderClickHandlerRegistration = addFolderClickHandler(new FolderClickHandler() {

            @Override
            public void onFolderClick(FolderClickEvent event) {
                if (!SCHEME_NODE_NAME.equals(event.getFolder().getName())) {
                    uiHandlers.goToConcept(event.getFolder().getAttribute(ConceptDS.URN));
                }
            }
        });

        leafClickHandlerRegistration = addLeafClickHandler(new LeafClickHandler() {

            @Override
            public void onLeafClick(LeafClickEvent event) {
                if (!SCHEME_NODE_NAME.equals(event.getLeaf().getName())) {
                    uiHandlers.goToConcept(event.getLeaf().getAttribute(ConceptDS.URN));
                }
            }
        });
    }

    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.conceptSchemeMetamacDto = conceptSchemeMetamacDto;

        TreeNode[] treeNodes = new TreeNode[itemHierarchyDtos.size()];
        for (int i = 0; i < itemHierarchyDtos.size(); i++) {
            treeNodes[i] = createConceptTreeNode(itemHierarchyDtos.get(i));
        }

        TreeNode conceptSchemeTreeNode = createConceptSchemeTreeNode(conceptSchemeMetamacDto);
        conceptSchemeTreeNode.setChildren(treeNodes);

        Tree tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setData(new TreeNode[]{conceptSchemeTreeNode});
        setData(tree);
        getData().openAll();
    }

    private TreeNode createConceptSchemeTreeNode(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        TreeNode node = new TreeNode(SCHEME_NODE_NAME);
        node.setAttribute(ConceptDS.CODE, conceptSchemeMetamacDto.getCode());
        node.setAttribute(ConceptDS.NAME, InternationalStringUtils.getLocalisedString(conceptSchemeMetamacDto.getName()));
        return node;
    }

    private TreeNode createConceptTreeNode(ItemHierarchyDto itemHierarchyDto) {
        TreeNode node = new TreeNode(itemHierarchyDto.getItem().getId().toString());
        node.setAttribute(ConceptDS.CODE, itemHierarchyDto.getItem().getCode());
        node.setAttribute(ConceptDS.NAME, InternationalStringUtils.getLocalisedString(itemHierarchyDto.getItem().getName()));
        node.setAttribute(ConceptDS.URN, itemHierarchyDto.getItem().getUrn());
        node.setAttribute(ConceptDS.DTO, itemHierarchyDto.getItem());
        node.setAttribute(
                ConceptDS.TYPE,
                ((ConceptMetamacDto) itemHierarchyDto.getItem()).getType() != null ? InternationalStringUtils.getLocalisedString(((ConceptMetamacDto) itemHierarchyDto.getItem()).getType()
                        .getDescription()) : StringUtils.EMPTY);
        node.setAttribute(ConceptDS.SDMX_RELATED_ARTEFACT, CommonUtils.getConceptRoleName(((ConceptMetamacDto) itemHierarchyDto.getItem()).getSdmxRelatedArtefact()));

        // Node children
        TreeNode[] children = new TreeNode[itemHierarchyDto.getChildren().size()];
        for (int i = 0; i < itemHierarchyDto.getChildren().size(); i++) {
            children[i] = createConceptTreeNode(itemHierarchyDto.getChildren().get(i));
        }
        node.setChildren(children);

        return node;
    }

    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void selectConcept(ConceptMetamacDto conceptMetamacDto) {
        RecordList nodes = getDataAsRecordList();
        Record record = nodes.find(ConceptDS.URN, conceptMetamacDto.getUrn());
        selectRecord(record);
    }

    public void removeHandlerRegistrations() {
        folderContextHandlerRegistration.removeHandler();
        leafContextHandlerRegistration.removeHandler();
        folderClickHandlerRegistration.removeHandler();
        leafClickHandlerRegistration.removeHandler();
    }

    private void showContextMenu() {
        contextMenu.markForRedraw();
        contextMenu.showContextMenu();
    }

}
