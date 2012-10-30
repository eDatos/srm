package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
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

public abstract class ItemsTreeGrid extends TreeGrid {

    protected static final String SCHEME_NODE_NAME = "scheme-node";

    protected Menu                contextMenu;

    protected HandlerRegistration folderContextHandlerRegistration;
    protected HandlerRegistration leafContextHandlerRegistration;
    protected HandlerRegistration folderClickHandlerRegistration;
    protected HandlerRegistration leafClickHandlerRegistration;

    protected ItemSchemeDto       itemSchemeDto;

    protected Tree                tree;

    public ItemsTreeGrid() {
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

        TreeGridField codeField = new TreeGridField(ItemDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setCanFilter(true);
        TreeGridField nameField = new TreeGridField(ItemDS.NAME, getConstants().nameableArtefactName());

        setFields(codeField, nameField);

        // Context menu

        contextMenu = new Menu();

        // Bind events

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                onNodeContextClick(event.getFolder().getName(), (ItemDto) event.getFolder().getAttributeAsObject(ItemDS.DTO));
            }
        });
        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (ItemDto) event.getLeaf().getAttributeAsObject(ItemDS.DTO));
            }
        });
        folderClickHandlerRegistration = addFolderClickHandler(new FolderClickHandler() {

            @Override
            public void onFolderClick(FolderClickEvent event) {
                onNodeClick(event.getFolder().getName(), event.getFolder().getAttribute(ItemDS.URN));
            }
        });
        leafClickHandlerRegistration = addLeafClickHandler(new LeafClickHandler() {

            @Override
            public void onLeafClick(LeafClickEvent event) {
                onNodeClick(event.getLeaf().getName(), event.getLeaf().getAttribute(ItemDS.URN));
            }
        });
    }
    public void removeHandlerRegistrations() {
        folderContextHandlerRegistration.removeHandler();
        leafContextHandlerRegistration.removeHandler();
        folderClickHandlerRegistration.removeHandler();
        leafClickHandlerRegistration.removeHandler();
    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemHierarchyDtos.size()];
        for (int i = 0; i < itemHierarchyDtos.size(); i++) {
            treeNodes[i] = createItemTreeNode(itemHierarchyDtos.get(i));
        }

        TreeNode organisationSchemeTreeNode = createItemSchemeTreeNode(itemSchemeDto);
        organisationSchemeTreeNode.setChildren(treeNodes);

        tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setData(new TreeNode[]{organisationSchemeTreeNode});
        setData(tree);
        getData().openAll();
    }

    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.itemSchemeDto = itemSchemeDto;
        // Update item scheme node
        TreeNode node = getTree().find(SCHEME_NODE_NAME);
        if (node != null) {
            node.setAttribute(ItemDS.CODE, itemSchemeDto.getCode());
            node.setAttribute(ItemDS.NAME, InternationalStringUtils.getLocalisedString(itemSchemeDto.getName()));
            markForRedraw();
        }
    }

    public void selectItem(ItemDto itemDto) {
        RecordList nodes = getDataAsRecordList();
        Record record = nodes.find(ItemDS.URN, itemDto.getUrn());
        selectRecord(record);
    }

    protected void showContextMenu() {
        contextMenu.markForRedraw();
        contextMenu.showContextMenu();
    }

    protected TreeNode createItemSchemeTreeNode(ItemSchemeDto itemSchemeDto) {
        TreeNode node = new TreeNode(SCHEME_NODE_NAME);
        node.setAttribute(ItemDS.CODE, itemSchemeDto.getCode());
        node.setAttribute(ItemDS.NAME, InternationalStringUtils.getLocalisedString(itemSchemeDto.getName()));
        return node;
    }

    protected TreeNode createItemTreeNode(ItemHierarchyDto itemHierarchyDto) {
        TreeNode node = new TreeNode(itemHierarchyDto.getItem().getId().toString());
        node.setAttribute(ItemDS.CODE, itemHierarchyDto.getItem().getCode());
        node.setAttribute(ItemDS.NAME, InternationalStringUtils.getLocalisedString(itemHierarchyDto.getItem().getName()));
        node.setAttribute(ItemDS.URN, itemHierarchyDto.getItem().getUrn());
        node.setAttribute(ItemDS.DTO, itemHierarchyDto.getItem());

        // Node children
        TreeNode[] children = new TreeNode[itemHierarchyDto.getChildren().size()];
        for (int i = 0; i < itemHierarchyDto.getChildren().size(); i++) {
            children[i] = createItemTreeNode(itemHierarchyDto.getChildren().get(i));
        }
        node.setChildren(children);

        return node;
    }

    protected void addItemsToContextMenu(MenuItem... menuItems) {
        for (MenuItem item : menuItems) {
            contextMenu.addItem(item);
        }
    }

    protected abstract void onNodeClick(String nodeName, String itemUrn);

    protected abstract void onNodeContextClick(String nodeName, ItemDto itemDto);

}
