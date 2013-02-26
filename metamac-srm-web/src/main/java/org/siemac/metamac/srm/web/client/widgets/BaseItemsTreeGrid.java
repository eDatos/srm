package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.web.common.client.resources.StyleUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;

public abstract class BaseItemsTreeGrid extends TreeGrid {

    protected static final String SCHEME_NODE_NAME = "scheme-node";

    protected Menu                contextMenu;

    protected HandlerRegistration folderContextHandlerRegistration;
    protected HandlerRegistration leafContextHandlerRegistration;
    protected HandlerRegistration folderClickHandlerRegistration;
    protected HandlerRegistration leafClickHandlerRegistration;

    protected ItemSchemeDto       itemSchemeDto;

    protected Tree                tree;

    protected HandlerRegistration filterEditionHandler;

    public BaseItemsTreeGrid() {
        setHeight(175);
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
        setRollUnderCanvasProperties(StyleUtils.getRollUnderCanvasProperties());

        TreeGridField codeField = new TreeGridField(ItemDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setCanFilter(true);
        TreeGridField nameField = new TreeGridField(ItemDS.NAME, getConstants().nameableArtefactName());

        setFields(codeField, nameField);

        setShowFilterEditor(true);

        filterEditionHandler = addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

            @Override
            public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
                event.cancel();
                TreeNode[] treeNodes = tree.getAllNodes();

                String codeCriteria = event.getCriteria().getAttribute(ItemDS.CODE);
                String nameCriteria = event.getCriteria().getAttribute(ItemDS.NAME);

                if (StringUtils.isBlank(codeCriteria) && StringUtils.isBlank(nameCriteria)) {
                    setData(tree);
                    return;
                } else {
                    List<TreeNode> matchingNodes = new ArrayList<TreeNode>();
                    for (TreeNode treeNode : treeNodes) {
                        if (!SCHEME_NODE_NAME.equals(treeNode.getName())) {
                            String code = treeNode.getAttributeAsString(ItemDS.CODE);
                            String name = treeNode.getAttributeAsString(ItemDS.NAME);

                            boolean matches = true;
                            if (codeCriteria != null && !StringUtils.startsWithIgnoreCase(code, codeCriteria)) {
                                matches = false;
                            }
                            if (nameCriteria != null && !StringUtils.startsWithIgnoreCase(name, nameCriteria)) {
                                matches = false;
                            }
                            if (matches) {
                                matchingNodes.add(treeNode);
                            }
                        }
                    }
                    Tree resultTree = new Tree();
                    resultTree.setData(matchingNodes.toArray(new TreeNode[0]));
                    setData(resultTree);
                }
            }
        });

        // Context menu

        contextMenu = new Menu();

        // Bind events

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

    public void selectItem(String urn) {
        RecordList nodes = getDataAsRecordList();
        Record record = nodes.find(ItemDS.URN, urn);
        selectRecord(record);
    }

    protected void showContextMenu() {
        contextMenu.markForRedraw();
        contextMenu.showContextMenu();
    }

    protected TreeNode createItemSchemeTreeNode(ItemSchemeDto itemSchemeDto) {
        TreeNode node = new TreeNode(SCHEME_NODE_NAME);
        node.setID(SCHEME_NODE_NAME);
        node.setAttribute(ItemDS.URN, itemSchemeDto.getUrn());
        node.setAttribute(ItemDS.CODE, itemSchemeDto.getCode());
        node.setAttribute(ItemDS.NAME, InternationalStringUtils.getLocalisedString(itemSchemeDto.getName()));
        return node;
    }

    protected void addItemsToContextMenu(MenuItem... menuItems) {
        for (MenuItem item : menuItems) {
            contextMenu.addItem(item);
        }
    }

    protected void removeFilterEditionHandler() {
        filterEditionHandler.removeHandler();
    }

    protected abstract void onNodeClick(String nodeName, String itemUrn);
}
