package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;
import org.siemac.metamac.web.common.client.resources.StyleUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
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
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public abstract class BaseItemsTreeGrid extends TreeGrid {

    protected static final String SCHEME_NODE_NAME = "scheme-node";

    protected Menu                contextMenu;

    protected HandlerRegistration folderContextHandlerRegistration;
    protected HandlerRegistration leafContextHandlerRegistration;
    protected HandlerRegistration folderClickHandlerRegistration;
    protected HandlerRegistration leafClickHandlerRegistration;

    protected ItemSchemeDto       itemSchemeDto;

    protected Tree                tree;
    protected TreeGridField       codeField;
    protected TreeGridField       nameField;
    protected TreeGridField       infoField;

    protected HandlerRegistration filterEditionHandler;

    public BaseItemsTreeGrid() {
        setHeight(175);
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
        setCanHover(true);
        setShowHover(true);
        setShowHoverComponents(true);
        setShowHeaderContextMenu(false); // do not show context menu in trees (avoid to show columns that should not be shown)

        codeField = new TreeGridField(ItemDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setCanFilter(true);
        codeField.setShowHover(false); // only show hover in info field

        nameField = new TreeGridField(ItemDS.NAME, getConstants().nameableArtefactName());
        nameField.setShowHover(false); // only show hover in info field

        infoField = new TreeGridField(ItemDS.INFO, " ");
        infoField.setType(ListGridFieldType.IMAGE);
        infoField.setWidth(50);
        infoField.setAlign(Alignment.CENTER);
        infoField.setCanFilter(false);
        infoField.setShowHover(true);

        setFields(codeField, nameField, infoField);

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
                            if (codeCriteria != null && !StringUtils.containsIgnoreCase(code, codeCriteria)) {
                                matches = false;
                            }
                            if (nameCriteria != null && !StringUtils.containsIgnoreCase(name, nameCriteria)) {
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
        return ItemsTreeGridUtils.createItemSchemeTreeNode(SCHEME_NODE_NAME, itemSchemeDto);
    }

    protected void addItemsToContextMenu(MenuItem... menuItems) {
        for (MenuItem item : menuItems) {
            contextMenu.addItem(item);
        }
    }

    protected void removeFilterEditionHandler() {
        filterEditionHandler.removeHandler();
    }

    protected void disableItemSchemeNode() {
        RecordList recordList = getRecordList();
        if (recordList != null && itemSchemeDto != null) {
            Record record = recordList.find(ItemDS.URN, itemSchemeDto.getUrn());
            if (record != null) {
                int index = getRecordIndex(record);
                if (index != -1) {
                    getRecord(index).setEnabled(false);
                }
            }
        }
    }

    @Override
    protected Canvas getCellHoverComponent(Record record, Integer rowNum, Integer colNum) {
        if (rowNum != 0) { // do not show details if it is the root node (item scheme node)
            DetailViewer detailViewer = new DetailViewer();
            detailViewer.setFields(getDetailViewerFields());
            detailViewer.setData(new Record[]{record});
            return detailViewer;
        }
        return super.getCellHoverComponent(record, rowNum, colNum);
    }

    protected abstract void onNodeClick(String nodeName, String itemUrn);
    protected abstract DetailViewerField[] getDetailViewerFields();
}
