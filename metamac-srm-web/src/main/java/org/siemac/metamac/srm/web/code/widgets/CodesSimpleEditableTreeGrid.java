package org.siemac.metamac.srm.web.code.widgets;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopyHierarchy;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesSimpleEditableTreeGrid extends BaseCodesSimpleTreeGrid {

    protected Tree tree;

    public CodesSimpleEditableTreeGrid() {
        super(true); // Always show editors, but only allow code edition

        getField(ItemDS.CODE).setCanEdit(true);
        getField(ItemDS.CODE).setValidators(SemanticIdentifiersUtils.getCodeIdentifierCustomValidator());
        getField(ItemDS.NAME).setCanEdit(false);
        getField(CodeDS.ORDER).setCanEdit(false);

        setValidateOnChange(true);
    }

    public void setCodes(List<TreeNode> nodes) {
        TreeNode[] treeNodes = nodes.toArray(new TreeNode[nodes.size()]);
        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        setShowPartialSelection(true);
        getData().openAll();
    }

    /**
     * Root node is excluded
     * 
     * @param rootNodeUrnToExlude
     * @return
     */
    public List<CodeToCopyHierarchy> getCodes() {
        saveAllEdits(); // ensure all changes made has been applied

        List<CodeToCopyHierarchy> codesToCopy = new ArrayList<CodeToCopyHierarchy>();

        TreeNode root = getTree().getRoot();
        if (root != null && getTree().getChildren(root) != null) {
            root = getTree().getChildren(root)[0]; // this node CANNOT be included in the list of node that will be copied
            if (root != null) {
                for (TreeNode child : getTree().getChildren(root)) {
                    CodeToCopyHierarchy code = new CodeToCopyHierarchy();
                    code.setNewCodeIdentifier(child.getAttributeAsString(ItemDS.CODE));
                    code.setSourceUrn(child.getAttributeAsString(ItemDS.URN));
                    code.getChildren().addAll(buildCodesHierarchy(child));
                    codesToCopy.add(code);
                }
            }
        }

        return codesToCopy;
    }

    private List<CodeToCopyHierarchy> buildCodesHierarchy(TreeNode root) {
        List<CodeToCopyHierarchy> codesToCopy = new ArrayList<CodeToCopyHierarchy>();
        TreeNode[] children = getTree().getChildren(root);
        if (children != null) {
            for (TreeNode child : children) {
                CodeToCopyHierarchy code = new CodeToCopyHierarchy();
                code.setNewCodeIdentifier(child.getAttributeAsString(ItemDS.CODE));
                code.setSourceUrn(child.getAttributeAsString(ItemDS.URN));
                code.getChildren().addAll(buildCodesHierarchy(child));
                codesToCopy.add(code);
            }
        }
        return codesToCopy;
    }

    @Override
    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
        if (rowNum == 0) {
            return "color:#aaaaaa;";
        }
        return super.getCellCSSText(record, rowNum, colNum);
    }
}
