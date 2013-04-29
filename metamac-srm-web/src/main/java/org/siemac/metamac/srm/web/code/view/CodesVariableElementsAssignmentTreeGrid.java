package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.widgets.BaseCodesTreeGrid;
import org.siemac.metamac.web.common.client.resources.GlobalResources;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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

        final String searchIconHTML = Canvas.imgHTML(GlobalResources.RESOURCE.search().getURL(), 16, 16);

        TreeGridField variableElementField = new TreeGridField(CodeDS.VARIABLE_ELEMENT, getConstants().variableElement());
        variableElementField.setCanFilter(false);
        variableElementField.setCellFormatter(new CellFormatter() {

            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                StringBuilder builder = new StringBuilder();
                if (value != null) {
                    builder.append(value.toString()).append(" ");
                }
                builder.append(searchIconHTML);
                return builder.toString();
            }
        });

        // TreeGridField editField = new TreeGridField(CodeDS.VARIABLE_ELEMENT_EDITION, MetamacWebCommon.getConstants().actionEdit());
        // editField.setCanFilter(false);
        // editField.setWidth(40);
        // editField.setAlign(Alignment.CENTER);
        // editField.setType(ListGridFieldType.IMAGE);

        ListGridField[] itemFields = getAllFields();

        // Set all fields non editable (except the openness level field)
        for (ListGridField field : itemFields) {
            field.setCanEdit(false);
        }

        ListGridField[] codeFields = new ListGridField[itemFields.length + 1];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 1] = variableElementField;

        // ListGridField[] codeFields = new ListGridField[itemFields.length + 2];
        // System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        // codeFields[codeFields.length - 2] = variableElementField;
        // codeFields[codeFields.length - 1] = editField;

        setFields(codeFields);

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult itemMetamacResult) {
        // Do nothing
    }
}
