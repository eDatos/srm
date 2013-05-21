package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
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

public abstract class BaseCodelistVisualisationSectionStack extends CustomListGridSectionStack {

    protected ToolStrip                toolStrip;
    protected ToolStripButton          newCodelistVisualisationButton;
    protected ToolStripButton          editCodelistVisualisationButton;
    protected ToolStripButton          deleteCodelistVisualisationButton;
    protected ToolStripButton          importCodelistVisualisationButton; // Only to import orders! Openness levels cannot be imported.
    protected ToolStripButton          exportCodelistVisualisationButton; // Only to export orders! Openness levels cannot be imported.

    protected DeleteConfirmationWindow deleteConfirmationWindow;

    protected CodelistUiHandlers       uiHandlers;

    protected CodelistMetamacDto       codelistMetamacDto;

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
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCodelistVisualisationButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());

        editCodelistVisualisationButton = new ToolStripButton(getConstants().actionEdit(), RESOURCE.editListGrid().getURL());
        editCodelistVisualisationButton.setVisibility(Visibility.HIDDEN);

        deleteCodelistVisualisationButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCodelistVisualisationButton.setVisibility(Visibility.HIDDEN);

        importCodelistVisualisationButton = new ToolStripButton(getConstants().actionImportCodeOrders(), GlobalResources.RESOURCE.importResource().getURL());

        exportCodelistVisualisationButton = new ToolStripButton(getConstants().codesOrderExport(), GlobalResources.RESOURCE.exportResource().getURL());

        toolStrip.addButton(newCodelistVisualisationButton);
        toolStrip.addButton(editCodelistVisualisationButton);
        toolStrip.addButton(deleteCodelistVisualisationButton);
        toolStrip.addButton(importCodelistVisualisationButton);
        toolStrip.addButton(exportCodelistVisualisationButton);

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

        defaultSection.setItems(toolStrip, listGrid);

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

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        this.codelistMetamacDto = codelistMetamacDto;
        updateButtonsVisibility();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setCodelistVisualisations(List<CodelistVisualisationDto> codelistVisualisationDtos) {
        listGrid.setData(org.siemac.metamac.srm.web.code.utils.RecordUtils.getCodelistVisualisationRecords(codelistVisualisationDtos));
    }

    private void updateButtonsVisibility() {
        updateListGridNewButtonVisibility();
        updateListGridImportButtonVisibility();
        updateListGridExportButtonVisibility();
    }

    protected abstract void showListGridEditButton();
    protected abstract void showListGridDeleteButton();
    protected abstract void updateListGridNewButtonVisibility();
    protected abstract void updateListGridImportButtonVisibility();
    protected abstract void updateListGridExportButtonVisibility();
}
