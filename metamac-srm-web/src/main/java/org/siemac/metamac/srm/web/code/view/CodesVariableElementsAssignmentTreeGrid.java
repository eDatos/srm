package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.widgets.BaseCodesTreeGrid;

import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.TreeGridField;

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

        ListGridField[] itemFields = getAllFields();

        // Set all fields non editable (except the openness level field)
        for (ListGridField field : itemFields) {
            field.setCanEdit(false);
        }

        ListGridField[] codeFields = new ListGridField[itemFields.length + 1];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 1] = variableElementField;

        setFields(codeFields);

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult itemMetamacResult) {
        // Do nothing
    }
}
