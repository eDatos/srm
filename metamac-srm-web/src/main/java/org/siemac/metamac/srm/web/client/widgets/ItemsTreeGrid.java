package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public abstract class ItemsTreeGrid extends BaseItemsTreeGrid {

    public ItemsTreeGrid() {

        // Bind events

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                onNodeContextClick(event.getFolder().getName(), (ItemVisualisationResult) event.getFolder().getAttributeAsObject(ItemDS.DTO));
            }
        });
        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (ItemVisualisationResult) event.getLeaf().getAttributeAsObject(ItemDS.DTO));
            }
        });
    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<ItemVisualisationResult> itemVisualisationResults) {
        // Clear filter editor
        setFilterEditorCriteria(null);

        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemVisualisationResults.size() + 1];
        treeNodes[0] = createItemSchemeTreeNode(itemSchemeDto);

        for (int i = 0; i < itemVisualisationResults.size(); i++) {
            treeNodes[i + 1] = createItemTreeNode(itemVisualisationResults.get(i));
        }

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    protected TreeNode createItemTreeNode(ItemVisualisationResult itemVisualisationResult) {
        String parentUrn = itemVisualisationResult.getParent() != null ? itemVisualisationResult.getParent().getUrn() : SCHEME_NODE_NAME;

        TreeNode node = new TreeNode(itemVisualisationResult.getItemIdDatabase().toString());
        node.setID(itemVisualisationResult.getUrn());
        node.setParentID(parentUrn);
        node.setAttribute(ItemDS.CODE, itemVisualisationResult.getCode());
        node.setAttribute(ItemDS.NAME, itemVisualisationResult.getName());
        node.setAttribute(ItemDS.URN, itemVisualisationResult.getUrn());
        node.setAttribute(ItemDS.ITEM_PARENT_URN, parentUrn);
        node.setAttribute(ItemDS.DTO, itemVisualisationResult);
        return node;
    }

    protected abstract void onNodeContextClick(String nodeName, ItemVisualisationResult itemVisualisationResult);
}
