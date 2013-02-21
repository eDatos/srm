package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
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
                onNodeContextClick(event.getFolder().getName(), (ItemDto) event.getFolder().getAttributeAsObject(ItemDS.DTO));
            }
        });
        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (ItemDto) event.getLeaf().getAttributeAsObject(ItemDS.DTO));
            }
        });
    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<ItemHierarchyDto> itemHierarchyDtos) {
        // Clear filter editor
        setFilterEditorCriteria(null);

        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemHierarchyDtos.size()];
        for (int i = 0; i < itemHierarchyDtos.size(); i++) {
            treeNodes[i] = createItemTreeNode(itemHierarchyDtos.get(i), null);
        }

        TreeNode organisationSchemeTreeNode = createItemSchemeTreeNode(itemSchemeDto);
        organisationSchemeTreeNode.setChildren(treeNodes);

        tree = new Tree();
        tree.setModelType(TreeModelType.CHILDREN);
        tree.setData(new TreeNode[]{organisationSchemeTreeNode});
        setData(tree);
        getData().openAll();
    }

    protected TreeNode createItemTreeNode(ItemHierarchyDto itemHierarchyDto, String itemParentUrn) {
        TreeNode node = new TreeNode(itemHierarchyDto.getItem().getId().toString());
        node.setAttribute(ItemDS.CODE, itemHierarchyDto.getItem().getCode());
        node.setAttribute(ItemDS.NAME, InternationalStringUtils.getLocalisedString(itemHierarchyDto.getItem().getName()));
        node.setAttribute(ItemDS.URN, itemHierarchyDto.getItem().getUrn());
        node.setAttribute(ItemDS.ITEM_PARENT_URN, itemParentUrn);
        node.setAttribute(ItemDS.DTO, itemHierarchyDto.getItem());

        // Node children
        TreeNode[] children = new TreeNode[itemHierarchyDto.getChildren().size()];
        for (int i = 0; i < itemHierarchyDto.getChildren().size(); i++) {
            children[i] = createItemTreeNode(itemHierarchyDto.getChildren().get(i), itemHierarchyDto.getItem().getUrn());
        }
        node.setChildren(children);

        return node;
    }

    protected abstract void onNodeContextClick(String nodeName, ItemDto itemDto);
}
