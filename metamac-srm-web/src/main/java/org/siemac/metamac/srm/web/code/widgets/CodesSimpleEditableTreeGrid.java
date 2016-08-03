package org.siemac.metamac.srm.web.code.widgets;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeToCopy;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxSrmUrnParserUtils;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
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
    public List<CodeToCopy> getCodes() {
        saveAllEdits(); // ensure all changes made has been applied

        List<CodeToCopy> codesToCopy = new ArrayList<CodeToCopy>();

        TreeNode root = getTree().getRoot();
        if (root != null && getTree().getChildren(root) != null) {
            root = getTree().getChildren(root)[0]; // this node CANNOT be included in the list of node that will be copied
            String rootUrn = root != null ? root.getAttribute(ItemDS.URN) : null;
            if (!StringUtils.isBlank(rootUrn)) {
                for (TreeNode node : getTree().getAllNodes(root)) {

                    if (!StringUtils.equals(rootUrn, node.getAttribute(ItemDS.URN))) { // do not include the root node!

                        TreeNode parentNode = getTree().getParent(node);
                        String parentNodeUrn = parentNode != null ? parentNode.getAttribute(ItemDS.URN) : null;

                        String code = node.getAttributeAsString(ItemDS.CODE);
                        String urn = node.getAttributeAsString(ItemDS.URN);
                        // If the new codes are going to be inserted in the first level in the target codelist, the parent should be null (not the codelist urn!!!)
                        String parentCode = !SdmxSrmUrnParserUtils.isCodelistUrn(parentNodeUrn) ? getNodeCode(parentNodeUrn) : null;

                        CodeToCopy codeToCopy = new CodeToCopy();
                        codeToCopy.setNewCodeIdentifier(code);
                        codeToCopy.setSourceUrn(urn);
                        codeToCopy.setParentNewCodeIdentifier(parentCode);
                        codesToCopy.add(codeToCopy);

                    }
                }
            }
        }
        return codesToCopy;
    }

    /**
     * Given the URN of a node, returns the code specified for this node
     * 
     * @param nodeUrn
     * @return
     */
    private String getNodeCode(String nodeUrn) {
        if (!StringUtils.isBlank(nodeUrn)) {
            RecordList recordList = getRecordList();
            if (recordList != null) {
                Record record = recordList.find(ItemDS.URN, nodeUrn);
                if (record != null) {
                    return record.getAttribute(ItemDS.CODE);
                }
            }
        }
        return null;
    }

    @Override
    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
        if (rowNum == 0) {
            return "color:#aaaaaa;";
        }
        return super.getCellCSSText(record, rowNum, colNum);
    }
}
