package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.code.utils.CodesTreeGridUtils;
import org.siemac.metamac.web.common.client.resources.StyleUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesCheckboxTreeGrid extends TreeGrid {

    protected ItemSchemeDto itemSchemeDto;

    protected Tree          tree;

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
        setCascadeSelection(false);
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

        for (int i = 0; i < itemMetamacResults.size(); i++) {
            treeNodes[i + 1] = createCodeTreeNode(itemMetamacResults.get(i));
        }

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        setShowPartialSelection(true);
        getData().openAll();
    }

    protected TreeNode createCodelistTreeNode(ItemSchemeDto itemSchemeDto) {
        return CodesTreeGridUtils.createCodelistTreeNode(itemSchemeDto.getUrn(), itemSchemeDto);
    }

    protected CodeTreeNode createCodeTreeNode(CodeMetamacVisualisationResult item) {
        return CodesTreeGridUtils.createCodeTreeNode(itemSchemeDto.getUrn(), item);
    }

    @Override
    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
        // Set the style of the codelist node like a disable node (the codelist node can be selected to select all the codes)
        if (StringUtils.equals(record.getAttributeAsString(ItemDS.URN), itemSchemeDto.getUrn())) {
            return "color:#aaaaaa;";
        }
        return super.getCellCSSText(record, rowNum, colNum);
    }
}
