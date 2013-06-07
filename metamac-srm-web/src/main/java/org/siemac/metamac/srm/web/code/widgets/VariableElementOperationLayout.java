package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementOperationDS;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.RecordUtils;
import org.siemac.metamac.srm.web.code.view.handlers.BaseVariableUiHandlers;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class VariableElementOperationLayout extends VLayout {

    private CustomToolStripButton    deleteOperationButton;
    private static final int         MAX_RESULTS = 10;
    private CustomListGrid           variableElementOperationsListGrid;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    private BaseVariableUiHandlers   uiHandlers;

    public VariableElementOperationLayout(String title) {

        // TOOL STRIP

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        deleteOperationButton = createDeleteOperationButton();
        toolStrip.addButton(deleteOperationButton);

        // LIST GRID

        variableElementOperationsListGrid = new CustomListGrid();
        variableElementOperationsListGrid.setAutoFitMaxRecords(MAX_RESULTS);
        variableElementOperationsListGrid.setAutoFitData(Autofit.VERTICAL);
        variableElementOperationsListGrid.setWrapCells(true);
        variableElementOperationsListGrid.setFixedRecordHeights(false);
        ListGridField operationCode = new ListGridField(VariableElementOperationDS.CODE, getConstants().identifiableArtefactCode());
        operationCode.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        // operationCode.setGroupTitleRenderer(new GroupTitleRenderer() {
        //
        // @Override
        // public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName, ListGrid grid) {
        // String groupTitle = StringUtils.EMPTY;
        // if (groupNode.getGroupMembers() != null) {
        // if (groupNode.getGroupMembers()[0] instanceof VariableElementOperationRecord) {
        // VariableElementOperationRecord record = (VariableElementOperationRecord) groupNode.getGroupMembers()[0];
        // String type = groupNode.getGroupMembers()[0].getAttribute(VariableElementOperationDS.OPERATION_TYPE);
        // String name = StringUtils.EMPTY;
        // if (VariableElementOperationTypeEnum.FUSION.equals(record.getTypeEnum())) {
        // name = record.getTarget();
        // } else if (VariableElementOperationTypeEnum.SEGREGATION.equals(record.getTypeEnum())) {
        // name = record.getSource();
        // }
        // groupTitle = getMessages().variableElementOperationTitle(type, name);
        // }
        // }
        // return groupTitle;
        // }
        // });
        ListGridField operationType = new ListGridField(VariableElementOperationDS.OPERATION_TYPE, getConstants().variableElementOperationType());
        // operationType.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        ListGridField source = new ListGridField(VariableElementOperationDS.OPERATION_SOURCES, getConstants().variableElementOperationSource());
        // source.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        ListGridField target = new ListGridField(VariableElementOperationDS.OPERATION_TARGETS, getConstants().variableElementOperationTarget());
        // target.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        // In this field the list of elements is stored (source or target elements, depending on the operation type)
        // ListGridField elementField = new ListGridField(VariableElementOperationDS.ELEMENTS, getConstants().variableElements());
        variableElementOperationsListGrid.setFields(operationCode, operationType, source, target);
        variableElementOperationsListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                ListGridRecord[] selectedRecords = variableElementOperationsListGrid.getSelectedRecords();
                // Select all the elements in the group (a group corresponds only with an operation)
                // for (ListGridRecord record : selectedRecords) {
                // if (record instanceof VariableElementOperationRecord) {
                // String code = ((VariableElementOperationRecord) record).getCode();
                // if (variableElementOperationsListGrid.getRecordList() != null) {
                // Record[] recordsInTheGroup = variableElementOperationsListGrid.getRecordList().findAll(VariableElementOperationDS.CODE, code);
                // for (Record recordInTheGroup : recordsInTheGroup) {
                // // Is record was already selected, does not select it again
                // // if (!isRecordSelected(recordInTheGroup, selectedRecords)) {
                // variableElementOperationsListGrid.selectRecord(recordInTheGroup, false);
                // // }
                // }
                // }
                // }
                // }
                updateListGridDeleteButtonVisibility(selectedRecords.length);
            }
        });
        // variableElementOperationsListGrid.setGroupByField(VariableElementOperationDS.CODE);
        // variableElementOperationsListGrid.setGroupStartOpen(GroupStartOpen.ALL);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().variableElementOperationDeleteConfirmationTitle(), getConstants().variableElementOperationDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteVariableElementOperations(org.siemac.metamac.srm.web.code.utils.CommonUtils.getCodesFromSelectedVariableElementOperations(variableElementOperationsListGrid
                        .getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        addMember(new TitleLabel(title));
        addMember(toolStrip);
        addMember(variableElementOperationsListGrid);
    }

    public void setUiHandlers(BaseVariableUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setVariableElementOperations(List<VariableElementOperationDto> variableElementOperationDtos) {
        // Set the operations in the list grid
        variableElementOperationsListGrid.setData(RecordUtils.getVariableElementOperationRecords(variableElementOperationDtos));

        // Do not show the operations is the list is empty
        if (variableElementOperationDtos.isEmpty()) {
            hide();
        } else {
            show();
        }
    }

    public CustomListGrid getListGrid() {
        return variableElementOperationsListGrid;
    }

    private CustomToolStripButton createDeleteOperationButton() {
        CustomToolStripButton deleteButton = new CustomToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisible(false);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });
        return deleteButton;
    }

    private void updateListGridDeleteButtonVisibility(int selectedRecords) {
        if (selectedRecords > 0) {
            showDeleteOperationButton();
        } else {
            deleteOperationButton.hide();
        }
    }

    private void showDeleteOperationButton() {
        if (CodesClientSecurityUtils.canDeleteVariableElement()) {
            deleteOperationButton.show();
        }
    }

    // private boolean isRecordSelected(Record record, ListGridRecord selectedRecords[]) {
    // String code = record.getAttribute(VariableElementOperationDS.CODE);
    // for (ListGridRecord selectedRecord : selectedRecords) {
    // return StringUtils.equals(code, selectedRecord.getAttribute(VariableElementOperationDS.CODE));
    // }
    // return false;
    // }
}
