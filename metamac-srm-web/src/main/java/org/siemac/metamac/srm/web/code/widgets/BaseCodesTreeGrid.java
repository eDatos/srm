package org.siemac.metamac.srm.web.code.widgets;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.widgets.BaseItemsTreeGrid;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public abstract class BaseCodesTreeGrid extends BaseItemsTreeGrid {

    public BaseCodesTreeGrid() {

        // Bind events

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                onNodeContextClick(event.getFolder().getName(), (CodeMetamacVisualisationResult) event.getFolder().getAttributeAsObject(ItemDS.DTO));
            }
        });
        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (CodeMetamacVisualisationResult) event.getLeaf().getAttributeAsObject(ItemDS.DTO));
            }
        });

    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<CodeMetamacVisualisationResult> itemMetamacResults) {
        // Clear filter editor
        setFilterEditorCriteria(null);

        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemMetamacResults.size() + 1];
        treeNodes[0] = createItemSchemeTreeNode(itemSchemeDto);

        CodeTreeNode[] codeTreeNodes = new CodeTreeNode[itemMetamacResults.size()];
        for (int i = 0; i < itemMetamacResults.size(); i++) {
            codeTreeNodes[i] = createItemTreeNode(itemMetamacResults.get(i));
        }

        // Sort the codes by its order
        Arrays.sort(codeTreeNodes, CodeTreeNode.OrderComparator);

        // Add the sorted codes to the final array (with the codelist root node)
        System.arraycopy(codeTreeNodes, 0, treeNodes, 1, codeTreeNodes.length);

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    protected CodeTreeNode createItemTreeNode(CodeMetamacVisualisationResult item) {
        CodeTreeNode node = new CodeTreeNode(item.getName());
        node.setID(item.getUrn());
        node.setIsFolder(true);
        node.setParentID(item.getParent() != null ? item.getParent().getUrn() : SCHEME_NODE_NAME);
        node.setAttribute(ItemDS.CODE, item.getCode());
        node.setAttribute(ItemDS.NAME, item.getName() != null ? item.getName() : StringUtils.EMPTY);
        node.setAttribute(ItemDS.URN, item.getUrn());
        node.setAttribute(ItemDS.ITEM_PARENT_URN, item.getParent() != null ? item.getParent().getUrn() : null);
        node.setAttribute(ItemDS.DTO, item);
        // Specify the order of the node in its level.
        // Avoid null pointer in CodeTreeNode comparator setting 0 when there is no order defined.
        node.setOrder(item.getOrder() == null ? 0 : item.getOrder());
        return node;
    }

    protected abstract void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult itemMetamacResult);
}
