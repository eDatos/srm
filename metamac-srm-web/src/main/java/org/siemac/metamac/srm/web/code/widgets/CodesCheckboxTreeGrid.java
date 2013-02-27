package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.code.utils.CodesTreeGridUtils;
import org.siemac.metamac.web.common.client.resources.StyleUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesCheckboxTreeGrid extends TreeGrid {

    protected static final String SCHEME_NODE_NAME = "scheme-node";

    protected ItemSchemeDto       itemSchemeDto;

    protected Tree                tree;

    public CodesCheckboxTreeGrid() {
        super();

        setHeight(175);
        setAutoFitMaxRecords(10);
        setAutoFitData(Autofit.VERTICAL);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setShowSelectedStyle(true);
        setShowPartialSelection(true);
        setShowConnectors(true);
        setLeaveScrollbarGap(false);
        setSelectionAppearance(SelectionAppearance.CHECKBOX);
        setCascadeSelection(true);
        setRollUnderCanvasProperties(StyleUtils.getRollUnderCanvasProperties());

        TreeGridField codeField = new TreeGridField(ItemDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("45%");
        TreeGridField nameField = new TreeGridField(ItemDS.NAME, getConstants().nameableArtefactName());
        setFields(codeField, nameField);
    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<CodeMetamacVisualisationResult> itemMetamacResults) {
        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemMetamacResults.size() + 1];
        treeNodes[0] = createCodelistTreeNode(itemSchemeDto);

        CodeTreeNode[] codeTreeNodes = new CodeTreeNode[itemMetamacResults.size()];
        for (int i = 0; i < itemMetamacResults.size(); i++) {
            codeTreeNodes[i] = createCodeTreeNode(itemMetamacResults.get(i));
        }

        // Sort the codes by its order
        Arrays.sort(codeTreeNodes, CodeTreeNode.OrderComparator);

        // Add the sorted codes to the final array (with the codelist root node)
        System.arraycopy(codeTreeNodes, 0, treeNodes, 1, codeTreeNodes.length);

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        setShowPartialSelection(true);
        getData().openAll();
    }

    protected TreeNode createCodelistTreeNode(ItemSchemeDto itemSchemeDto) {
        TreeNode node = CodesTreeGridUtils.createCodelistTreeNode(SCHEME_NODE_NAME, itemSchemeDto);
        node.setEnabled(false);
        return node;
    }

    protected CodeTreeNode createCodeTreeNode(CodeMetamacVisualisationResult item) {
        return CodesTreeGridUtils.createCodeTreeNode(SCHEME_NODE_NAME, item);
    }
}
