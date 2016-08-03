package org.siemac.metamac.srm.web.client.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClosedEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedHandler;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public abstract class ItemsTreeGrid extends BaseItemsTreeGrid {

    protected HandlerRegistration folderOpenedHandlerRegistration;
    protected HandlerRegistration folderClosedHandlerRegistration;

    // Internal representation that helps us to recover opened nodes after reloading tree
    private Map<String, String>   treeOpenStates = new HashMap<String, String>();

    public ItemsTreeGrid() {

        // Bind context menu events

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

        // Bind folders event

        folderOpenedHandlerRegistration = addFolderOpenedHandler(new FolderOpenedHandler() {

            @Override
            public void onFolderOpened(FolderOpenedEvent event) {
                saveTreeOpenState();
            }
        });

        folderClosedHandlerRegistration = addFolderClosedHandler(new FolderClosedHandler() {

            @Override
            public void onFolderClosed(FolderClosedEvent event) {
                saveTreeOpenState();
            }
        });
    }

    @SuppressWarnings("rawtypes")
    public void setItems(ItemSchemeDto itemSchemeDto, List itemVisualisationResults) {
        // Clear filter editor
        setFilterEditorCriteria(null);

        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemVisualisationResults.size() + 1];
        treeNodes[0] = createItemSchemeTreeNode(itemSchemeDto);

        for (int i = 0; i < itemVisualisationResults.size(); i++) {
            treeNodes[i + 1] = createItemTreeNode((ItemVisualisationResult) itemVisualisationResults.get(i));
        }

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
        recoverOpenState();
    }

    protected TreeNode createItemTreeNode(ItemVisualisationResult itemVisualisationResult) {
        return ItemsTreeGridUtils.createItemTreeNode(SCHEME_NODE_NAME, itemVisualisationResult);
    }

    protected void recoverOpenState() {
        if (treeOpenStates.containsKey(itemSchemeDto.getUrn()) && !StringUtils.isBlank(treeOpenStates.get(itemSchemeDto.getUrn()))) {
            setOpenState(treeOpenStates.get(itemSchemeDto.getUrn()));
        }
    }

    private void saveTreeOpenState() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                treeOpenStates.put(itemSchemeDto.getUrn(), getOpenState());
            }
        });
    }

    protected abstract void onNodeContextClick(String nodeName, ItemVisualisationResult itemVisualisationResult);
}
