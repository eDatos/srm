package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.widgets.BaseItemsTreeGrid;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.utils.CodesTreeGridUtils;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SortChangedHandler;
import com.smartgwt.client.widgets.grid.events.SortEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public abstract class BaseCodesTreeGrid extends BaseItemsTreeGrid {

    public BaseCodesTreeGrid() {

        // Add order field to treeGrid and disable the option to order by CODE and NAME fields

        // Do not enable the button to order by other fields than order field
        ListGridField[] itemFields = getFields();
        for (ListGridField itemField : itemFields) {
            itemField.setCanSort(false);
        }

        // Add the orderField to the previous fields
        TreeGridField orderField = new TreeGridField(CodeDS.ORDER, getConstants().codeOrder());
        orderField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        orderField.setCanSort(true);

        ListGridField[] codeFields = new ListGridField[itemFields.length + 1];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 1] = orderField;

        setFields(codeFields);

        // Do not let to order in descending direction!!
        addSortChangedHandler(new SortChangedHandler() {

            @Override
            public void onSortChanged(SortEvent event) {
                SortSpecifier[] sortSpecifiers = event.getSortSpecifiers();
                if (sortSpecifiers != null) {
                    for (SortSpecifier sortSpecifier : sortSpecifiers) {
                        if (SortDirection.DESCENDING.equals(sortSpecifier.getSortDirection())) {
                            // Instead of cancel the event (do not know if it is possible), the column is ordered again in ascending order
                            sort(CodeDS.ORDER, SortDirection.ASCENDING);
                        }
                    }
                }
            }
        });

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

        for (int i = 0; i < itemMetamacResults.size(); i++) {
            treeNodes[i + 1] = createItemTreeNode(itemMetamacResults.get(i));
        }

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    protected CodeTreeNode createItemTreeNode(CodeMetamacVisualisationResult item) {
        return CodesTreeGridUtils.createCodeTreeNode(SCHEME_NODE_NAME, item);
    }

    protected abstract void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult itemMetamacResult);
}
