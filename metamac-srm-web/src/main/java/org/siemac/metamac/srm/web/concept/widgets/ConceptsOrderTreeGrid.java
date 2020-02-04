package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SortChangedHandler;
import com.smartgwt.client.widgets.grid.events.SortEvent;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

public class ConceptsOrderTreeGrid extends ConceptsTreeGrid {

    protected TreeGridField       orderField;

    protected HandlerRegistration folderDropHandlerRegistration;

    public ConceptsOrderTreeGrid() {
        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        orderField = new TreeGridField(ConceptDS.ORDER, getConstants().conceptOrder());
        orderField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        orderField.setCanSort(true);
        orderField.setShowHover(false);

        List<ListGridField> fields = new ArrayList<ListGridField>(Arrays.asList(getAllFields()));

        // Disable the option to order by CODE and NAME fields (do not enable the button to order by other fields than order field)
        for (ListGridField field : fields) {
            field.setCanSort(Boolean.FALSE);
        }

        // The order field is added in the penultimate position
        fields.add(fields.size() - 1, orderField);

        setFields(fields.toArray(new ListGridField[fields.size()]));

        setCanSort(true);
        setSortField(ConceptDS.ORDER);

        // Do not let to order in descending direction!!
        addSortChangedHandler(new SortChangedHandler() {

            @Override
            public void onSortChanged(SortEvent event) {
                SortSpecifier[] sortSpecifiers = event.getSortSpecifiers();
                if (sortSpecifiers != null) {
                    for (SortSpecifier sortSpecifier : sortSpecifiers) {
                        if (SortDirection.DESCENDING.equals(sortSpecifier.getSortDirection())) {
                            // Instead of cancel the event (do not know if it is possible), the column is ordered again in ascending order
                            sort(ConceptDS.ORDER, SortDirection.ASCENDING);
                        }
                    }
                }
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

                    String oldItemParent = droppedNode.getAttribute(ConceptDS.ITEM_PARENT_URN);
                    String newItemParent = SCHEME_NODE_NAME.equals(dropFolder.getName()) ? SCHEME_NODE_NAME : dropFolder.getAttribute(ConceptDS.URN);

                    if (!StringUtils.equals(oldItemParent, newItemParent)) {
                        // UPDATE CONCEPT PARENT
                        if (ConceptsClientSecurityUtils.canUpdateConceptParent(conceptSchemeMetamacDto)) {
                            if (SCHEME_NODE_NAME.equals(newItemParent)) {
                                // The concept will be moved to the first level. The parent is null.
                                newItemParent = null;
                            }
                            getBaseConceptUiHandlers().updateConceptParent(droppedNode.getAttribute(ConceptDS.URN), newItemParent, relativePosition);
                        }
                    } else {
                        // UPDATE CONCEPT ORDER
                        if (ConceptsClientSecurityUtils.canUpdateConceptOrderInLevel(conceptSchemeMetamacDto)) {
                            getBaseConceptUiHandlers().updateConceptInOrder(droppedNode.getAttribute(ConceptDS.URN), conceptSchemeMetamacDto.getUrn(), relativePosition);
                        }
                    }
                }
                event.cancel();
            }
        });
    }

    @Override
    protected TreeNode createItemTreeNode(ItemVisualisationResult itemVisualisationResult) {
        TreeNode treeNode = super.createItemTreeNode(itemVisualisationResult);
        treeNode.setAttribute(ConceptDS.ORDER, ((ConceptMetamacVisualisationResult) itemVisualisationResult).getOrder());
        return treeNode;
    }

    protected boolean isDroppable(TreeNode dropFolder) {
        return !("/".equals(getDropFolder().getName()));
    }

}
