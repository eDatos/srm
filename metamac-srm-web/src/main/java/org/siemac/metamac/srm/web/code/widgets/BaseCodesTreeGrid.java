package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.widgets.BaseItemsTreeGrid;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CodesTreeGridUtils;
import org.siemac.metamac.srm.web.code.view.handlers.BaseCodeUiHandlers;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public abstract class BaseCodesTreeGrid extends BaseItemsTreeGrid {

    protected CodelistMetamacDto            codelistMetamacDto;

    protected HandlerRegistration           folderDropHandlerRegistration;

    protected BaseCodeUiHandlers            uiHandlers;

    protected CodelistOrderVisualisationDto codelistOrderVisualisationDto; // This field is only filled by the CodesOrderTreeGrid class

    public BaseCodesTreeGrid(boolean canStructureBeModified, boolean canOrderBeModified) {

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

        folderDropHandlerRegistration = addFolderDropHandler(new FolderDropHandler() {

            @Override
            public void onFolderDrop(FolderDropEvent event) {
                TreeNode dropFolder = event.getFolder();
                TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
                int position = event.getIndex(); // Absolute position
                if (isDroppable(dropFolder)) {
                    TreeNode[] siblings = getData().getChildren(dropFolder);

                    // We find out the position of the node under the dropFolder
                    int relativePosition = position; // Used to update position
                    int pos = -1;
                    for (int i = 0; i < siblings.length; i++) {
                        if (siblings[i] == droppedNode) {
                            pos = i;
                        }
                    }
                    if (pos >= 0 && pos < position) { // If moved node is before final position, the position must be updated
                        relativePosition--;
                    }

                    String oldItemParent = droppedNode.getAttribute(CodeDS.ITEM_PARENT_URN);
                    String newItemParent = SCHEME_NODE_NAME.equals(dropFolder.getName()) ? SCHEME_NODE_NAME : dropFolder.getAttribute(CodeDS.URN);

                    if (!StringUtils.equals(oldItemParent, newItemParent)) {

                        // UPDATE CODE PARENT

                        if (CodesClientSecurityUtils.canUpdateCodeParent(codelistMetamacDto)) {

                            if (SCHEME_NODE_NAME.equals(newItemParent)) {
                                // The code will be moved to the first level. The parent is null.
                                newItemParent = null;
                            }
                            uiHandlers.updateCodeParent(droppedNode.getAttribute(CodeDS.URN), newItemParent);
                        }
                    } else {

                        // UPDATE ORDER

                        if (CodesClientSecurityUtils.canUpdateCodelistOrderVisualisation(codelistMetamacDto.getLifeCycle().getProcStatus())) {

                            // Only update order if there is an order selected and it is not the alphabetical one
                            if (codelistOrderVisualisationDto != null && !SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(codelistOrderVisualisationDto.getCode())) {
                                uiHandlers.updateCodeInOrder(droppedNode.getAttribute(CodeDS.URN), codelistOrderVisualisationDto.getUrn(), relativePosition);
                            }
                        }
                    }
                }
                event.cancel();
            }
        });

    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<CodeMetamacVisualisationResult> itemMetamacResults) {
        this.codelistMetamacDto = (CodelistMetamacDto) itemSchemeDto;

        // Clear filter editor
        setFilterEditorCriteria(null);

        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemMetamacResults.size() + 1];
        treeNodes[0] = createItemSchemeTreeNode(itemSchemeDto);

        for (int i = 0; i < itemMetamacResults.size(); i++) {
            treeNodes[i + 1] = createItemTreeNode(itemMetamacResults.get(i));
        }

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.codelistMetamacDto = (CodelistMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseCodeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    protected CodeTreeNode createItemTreeNode(CodeMetamacVisualisationResult item) {
        return CodesTreeGridUtils.createCodeTreeNode(SCHEME_NODE_NAME, item);
    }

    protected boolean isDroppable(TreeNode dropFolder) {
        return !("/".equals(getDropFolder().getName()));
    }

    protected abstract void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult itemMetamacResult);
}
