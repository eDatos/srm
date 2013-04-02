package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.code.model.ds.CodelistVisualisationDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistVisualisationRecord;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public abstract class BaseCodelistVisualisationSectionStack extends CustomSectionStack {

    protected ToolStripButton          newCodelistVisualisationButton;
    protected ToolStripButton          editCodelistVisualisationButton;
    protected ToolStripButton          deleteCodelistVisualisationButton;

    protected DeleteConfirmationWindow deleteConfirmationWindow;

    protected CodelistUiHandlers       uiHandlers;

    protected ProcStatusEnum           codelistProcStatus;

    public BaseCodelistVisualisationSectionStack(final ListGrid listGrid, String title) {
        super(listGrid, title);

        // Add fields to the listGrid
        ListGridField codeField = new ListGridField(CodelistVisualisationDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CodelistVisualisationRecord) record).getCode();
            }
        });

        ListGridField nameField = new ListGridField(CodelistVisualisationDS.NAME, getConstants().nameableArtefactName());
        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CodelistVisualisationRecord) record).getName();
            }
        });

        ListGridField urnField = new ListGridField(CodelistVisualisationDS.URN, getConstants().identifiableArtefactUrn());
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CodelistVisualisationRecord) record).getUrn();
            }
        });

        listGrid.setFields(codeField, nameField, urnField);

        // ToolBar to manage visualisations
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        newCodelistVisualisationButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());

        editCodelistVisualisationButton = new ToolStripButton(getConstants().actionEdit(), RESOURCE.editListGrid().getURL());
        editCodelistVisualisationButton.setVisibility(Visibility.HIDDEN);

        deleteCodelistVisualisationButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCodelistVisualisationButton.setVisibility(Visibility.HIDDEN);

        toolStrip.addButton(newCodelistVisualisationButton);
        toolStrip.addButton(editCodelistVisualisationButton);
        toolStrip.addButton(deleteCodelistVisualisationButton);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistVisualisationDeleteConfirmationTitle(), getConstants().codelistVisualisationDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);

        listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getSelectedRecords().length > 0) {
                    // Show edit and delete button
                    showListGridDeleteButton();
                    if (listGrid.getSelectedRecords().length == 1) {
                        showListGridEditButton();
                    } else {
                        editCodelistVisualisationButton.hide();
                    }
                } else {
                    deleteCodelistVisualisationButton.hide();
                    editCodelistVisualisationButton.hide();
                }
            }
        });

        section.setItems(toolStrip, listGrid);

    }

    protected CodelistVisualisationDto getSelectedCodelistVisualisation() {
        CodelistVisualisationDto codelistVisualisationDto = new CodelistVisualisationDto();
        ListGridRecord record = listGrid.getSelectedRecord();
        if (record != null && record instanceof CodelistVisualisationRecord) {
            codelistVisualisationDto = ((CodelistVisualisationRecord) record).getCodelistVisualisationDto();
        }
        return codelistVisualisationDto;
    }

    public void selectCodelistVisualisation(String visualisationUrn) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(CodelistVisualisationDS.URN, visualisationUrn);
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }

    public void setCodelistProcStatus(ProcStatusEnum procStatusEnum) {
        this.codelistProcStatus = procStatusEnum;
        updateListGridNewButtonVisibility();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setCodelistVisualisations(List<CodelistVisualisationDto> codelistVisualisationDtos) {
        listGrid.setData(org.siemac.metamac.srm.web.code.utils.RecordUtils.getCodelistVisualisationRecords(codelistVisualisationDtos));
    }

    protected abstract void showListGridEditButton();
    protected abstract void showListGridDeleteButton();
    protected abstract void updateListGridNewButtonVisibility();
}