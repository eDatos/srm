package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.code.model.ds.CodelistOrderDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistOrderRecord;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.EditCodelistOrderWindow;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
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

    private ProcStatusEnum           codelistProcStatus;

    public CodelistOrdersSectionStack() {
        super(new CustomListGrid(), getConstants().codelistOrders());
        setWidth("40%");

        // Add fields to the listGrid
        ListGridField codeField = new ListGridField(CodelistOrderDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CodelistOrderRecord) record).getCode();
            }
        });

        ListGridField nameField = new ListGridField(CodelistOrderDS.NAME, getConstants().nameableArtefactName());
        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CodelistOrderRecord) record).getName();
            }
        });

        ListGridField urnField = new ListGridField(CodelistOrderDS.URN, getConstants().identifiableArtefactUrn());
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CodelistOrderRecord) record).getUrn();
            }
        });

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
                if (isAlphabeticalOrderSelected()) {
                    // Alphabetical order can not be deleted
                    InformationWindow informationWindow = new InformationWindow(getMessages().codelistOrderAlphabeticalInfoDeleteTitle(), getMessages().codelistOrderAlphabeticalInfoDeleteMessage());
                    informationWindow.show();
                } else {
                    deleteConfirmationWindow.show();
                }
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
                    String orderCode = ((CodelistOrderRecord) event.getRecord()).getAttribute(CodelistOrderDS.CODE);
                    if (SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(orderCode) && !org.siemac.metamac.srm.web.client.utils.CommonUtils.isItemSchemePublished(codelistProcStatus)) {
                        // The alphabetical order can not be visualized until the codelist is published
                        InformationWindow informationWindow = new InformationWindow(getMessages().codelistOrderAlphabeticalInfoViewTitle(), getMessages().codelistOrderAlphabeticalInfoViewMessage());
                        informationWindow.show();
                    } else {
                        String orderUrn = ((CodelistOrderRecord) event.getRecord()).getAttribute(CodelistOrderDS.URN);
                        uiHandlers.retrieveCodesByCodelist(orderUrn);
                    }
                }
            }
        });

        section.setItems(toolStrip, listGrid);
    }

    public void setCodelistOrders(List<CodelistOrderVisualisationDto> codelistOrderVisualisationDtos) {
        CodelistOrderRecord[] records = new CodelistOrderRecord[codelistOrderVisualisationDtos.size()];
        int index = 0;
        for (CodelistOrderVisualisationDto order : codelistOrderVisualisationDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.RecordUtils.getCodelistOrderRecord(order);
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

    public void setCodelistProcStatus(ProcStatusEnum procStatusEnum) {
        this.codelistProcStatus = procStatusEnum;
        updateListGridNewButtonVisibility();
    }

    private void updateListGridNewButtonVisibility() {
        if (CodesClientSecurityUtils.canCrudCodelistOrderVisualisation(codelistProcStatus)) {
            newCodelistOrderButton.show();
        } else {
            newCodelistOrderButton.hide();
        }
    }

    private void showListGridEditButton() {
        if (CodesClientSecurityUtils.canCrudCodelistOrderVisualisation(codelistProcStatus)) {
            editCodelistOrderButton.show();
        }
    }

    private void showListGridDeleteButton() {
        if (CodesClientSecurityUtils.canCrudCodelistOrderVisualisation(codelistProcStatus)) {
            deleteCodelistOrderButton.show();
        }
    }

    private CodelistOrderVisualisationDto getSelectedCodelistOrder() {
        CodelistOrderVisualisationDto codelistOrderVisualisationDto = new CodelistOrderVisualisationDto();
        ListGridRecord record = listGrid.getSelectedRecord();
        if (record != null && record instanceof CodelistOrderRecord) {
            codelistOrderVisualisationDto = ((CodelistOrderRecord) record).getCodelistOrderVisualisationDto();
        }
        return codelistOrderVisualisationDto;
    }

    private boolean isAlphabeticalOrderSelected() {
        List<String> orderCodes = CommonUtils.getOrderCodesFromSelectedCodelistOrders(listGrid.getSelectedRecords());
        for (String orderCode : orderCodes) {
            if (SrmConstants.CODELIST_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(orderCode)) {
                return true;
            }
        }
        return false;
    }
}
