package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.widgets.BaseCodesTreeGrid;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesVariableElementsAssignmentTreeGrid extends BaseCodesTreeGrid {

    public CodesVariableElementsAssignmentTreeGrid() {
        super(false, false);

        setShowFilterEditor(false);
        setCanReorderRecords(false);
        setCanAcceptDroppedRecords(false);
        setCanDragRecordsOut(false);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setCanSort(false);

        // Add a field with the variable element

        TreeGridField variableElementField = new TreeGridField(CodeDS.VARIABLE_ELEMENT, getConstants().variableElement());
        variableElementField.setCanFilter(false);
        variableElementField.setAlign(Alignment.RIGHT);
        // final String searchIconHTML = Canvas.imgHTML(GlobalResources.RESOURCE.search().getURL(), 16, 16);
        // variableElementField.setCellFormatter(new CellFormatter() {
        //
        // @Override
        // public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
        // StringBuilder builder = new StringBuilder();
        // if (value != null) {
        // builder.append(value.toString()).append(" ");
        // }
        // builder.append(searchIconHTML);
        // return builder.toString();
        // }
        // });

        TreeGridField editField = new TreeGridField(CodeDS.VARIABLE_ELEMENT_EDITION, " "); // Do not show title in this column (an space is needed)
        editField.setCanFilter(false);
        editField.setCanEdit(false);
        editField.setWidth(50);
        editField.setAlign(Alignment.LEFT);
        editField.setType(ListGridFieldType.IMAGE);

        ListGridField[] itemFields = getAllFields();

        // Set all fields non editable
        for (ListGridField field : itemFields) {
            field.setCanEdit(false);
        }

        ListGridField[] codeFields = new ListGridField[itemFields.length + 2];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 2] = variableElementField;
        codeFields[codeFields.length - 1] = editField;

        setFields(codeFields);

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult itemMetamacResult) {
        // Do nothing
    }

    public void setCodesVariableElementsNormalised(CodelistMetamacDto codelistMetamacDto, List<CodeVariableElementNormalisationResult> codeVariableElementNormalisationResults) {
        setCodesAndVariableElements(codelistMetamacDto, codeVariableElementNormalisationResults);
    }

    public Map<Long, Long> getCodesVariableElements() {
        Map<Long, Long> codesVariableElements = new HashMap<Long, Long>();
        TreeNode[] nodes = getTree().getAllNodes();
        for (TreeNode node : nodes) {
            if (!SCHEME_NODE_NAME.equals(node.getName())) {
                Long codeId = node.getAttributeAsLong(CodeDS.ID_DATABASE);
                Long variableElementId = node.getAttributeAsLong(CodeDS.VARIABLE_ELEMENT_ID_DATABASE);
                codesVariableElements.put(codeId, variableElementId);
            }
        }
        return codesVariableElements;
    }

    public void setViewMode() {
        hideField(CodeDS.VARIABLE_ELEMENT_EDITION);
    }

    public void setEditionMode() {
        showField(CodeDS.VARIABLE_ELEMENT_EDITION);
    }
}
