package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodelistOrderDS;
import org.siemac.metamac.srm.web.client.code.model.record.CodelistOrderRecord;
import org.siemac.metamac.srm.web.client.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.EditCodelistOrderWindow;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistOrdersSectionStack extends CustomSectionStack {

    private ToolStripButton          newCodelistOrderButton;
    private ToolStripButton          editCodelistOrderButton;
    private ToolStripButton          deleteCodelistOrderButton;

    private DeleteConfirmationWindow deleteConfirmationWindow;

    private CodelistUiHandlers       uiHandlers;

    public CodelistOrdersSectionStack() {
        super(new CustomListGrid(), getConstants().codelistOrders());
        setWidth("40%");

        // Add fields to the listGrid
        ListGridField codeField = new ListGridField(CodelistOrderDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        ListGridField nameField = new ListGridField(CodelistOrderDS.NAME, getConstants().nameableArtefactName());
        ListGridField urnField = new ListGridField(CodelistOrderDS.URN, getConstants().identifiableArtefactUrn());
        listGrid.setFields(codeField, nameField, urnField);

        // ToolBar to manage orders
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        newCodelistOrderButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newCodelistOrderButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final EditCodelistOrderWindow newCodelistOrderWindow = new EditCodelistOrderWindow(getConstants().codelistOrderCreate());
                newCodelistOrderWindow.setCodelistOrder(new CodelistOrderVisualisationDto());
                newCodelistOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCodelistOrderWindow.validateForm()) {
                            uiHandlers.saveCodelistOrder(newCodelistOrderWindow.getCodelistOrderDto());
                            newCodelistOrderWindow.destroy();
                        }
                    }
                });
            }
        });
        // TODO Security
        // newCodelistOrderButton.setVisibility(CodesClientSecurityUtils.canCreateCodelist() ? Visibility.VISIBLE : Visibility.HIDDEN);

        editCodelistOrderButton = new ToolStripButton(getConstants().actionEdit(), RESOURCE.editListGrid().getURL());
        editCodelistOrderButton.setVisibility(Visibility.HIDDEN);
        editCodelistOrderButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CodelistOrderVisualisationDto selectedOrder = getSelectedCodelistOrder();
                final EditCodelistOrderWindow editCodelistOrderWindow = new EditCodelistOrderWindow(getConstants().codelistOrderEdit());
                editCodelistOrderWindow.setCodelistOrder(selectedOrder);
                editCodelistOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (editCodelistOrderWindow.validateForm()) {
                            uiHandlers.saveCodelistOrder(editCodelistOrderWindow.getCodelistOrderDto());
                            editCodelistOrderWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteCodelistOrderButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCodelistOrderButton.setVisibility(Visibility.HIDDEN);
        deleteCodelistOrderButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newCodelistOrderButton);
        toolStrip.addButton(editCodelistOrderButton);
        toolStrip.addButton(deleteCodelistOrderButton);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistOrderDeleteConfirmationTitle(), getConstants().codelistOrderDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteCodelistOrders(CommonUtils.getUrnsFromSelectedCodelistOrders(listGrid.getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getSelectedRecords().length > 0) {
                    // Show edit and delete button
                    showListGridDeleteButton();
                    if (listGrid.getSelectedRecords().length == 1) {
                        showListGridEditButton();
                    } else {
                        editCodelistOrderButton.hide();
                    }
                } else {
                    deleteCodelistOrderButton.hide();
                    editCodelistOrderButton.hide();
                }
            }
        });
        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String orderUrn = ((CodelistOrderRecord) event.getRecord()).getAttribute(CodelistOrderDS.URN);
                    uiHandlers.retrieveCodesByCodelist(orderUrn);
                }
            }
        });

        section.setItems(toolStrip, listGrid);
    }

    public void setCodelistOrders(List<CodelistOrderVisualisationDto> codelistOrderVisualisationDtos) {
        CodelistOrderRecord[] records = new CodelistOrderRecord[codelistOrderVisualisationDtos.size()];
        int index = 0;
        for (CodelistOrderVisualisationDto order : codelistOrderVisualisationDtos) {
            records[index++] = org.siemac.metamac.srm.web.client.code.utils.RecordUtils.getCodelistOrderRecord(order);
        }
        listGrid.setData(records);
    }

    public void selectCodelistOrder(String orderUrn) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(CodelistOrderDS.URN, orderUrn);
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private void showListGridDeleteButton() {
        // TODO Security
        deleteCodelistOrderButton.show();
    }

    private void showListGridEditButton() {
        // TODO Security
        editCodelistOrderButton.show();
    }

    private CodelistOrderVisualisationDto getSelectedCodelistOrder() {
        CodelistOrderVisualisationDto codelistOrderVisualisationDto = new CodelistOrderVisualisationDto();
        ListGridRecord record = listGrid.getSelectedRecord();
        if (record != null && record instanceof CodelistOrderRecord) {
            codelistOrderVisualisationDto = ((CodelistOrderRecord) record).getCodelistOrderVisualisationDto();
        }
        return codelistOrderVisualisationDto;
    }
}
