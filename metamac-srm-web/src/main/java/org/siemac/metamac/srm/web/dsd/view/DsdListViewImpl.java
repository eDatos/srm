package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.widgets.DsdPaginatedListGrid;
import org.siemac.metamac.srm.web.dsd.listener.UploadListener;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.ImportDsdWindow;
import org.siemac.metamac.srm.web.dsd.widgets.NewDsdWindow;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.HasRecordClickHandlers;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class DsdListViewImpl extends ViewWithUiHandlers<DsdListUiHandlers> implements DsdListPresenter.DsdListView {

    private VLayout                  panel;

    private DsdPaginatedListGrid     dsdListGrid;

    private ToolStripButton          newToolStripButton;
    private ToolStripButton          deleteToolStripButton;
    private ToolStripButton          importToolStripButton;
    private ToolStripButton          exportToolStripButton;
    private ToolStripButton          cancelValidityButton;

    private NewDsdWindow             newDsdWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;
    private ImportDsdWindow          importDsdWindow;

    private SearchSectionStack       searchSectionStack;

    @Inject
    public DsdListViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        dsdListGrid = new DsdPaginatedListGrid(DsdListPresenter.DSD_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveDsdList(firstResult, maxResults, searchSectionStack.getSearchCriteria());
            }
        });

        // ············
        // List of DSDs
        // ············

        // ToolStrip

        newToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionNew(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());
        newToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newDsdWindow = new NewDsdWindow();
                newDsdWindow.setUiHandlers(getUiHandlers());
                newDsdWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newDsdWindow.validateForm()) {
                            getUiHandlers().saveDsd(newDsdWindow.getNewDsd());
                            newDsdWindow.destroy();
                        }
                    }
                });
            }
        });
        newToolStripButton.setVisibility(DsdClientSecurityUtils.canCreateDsd() ? Visibility.VISIBLE : Visibility.HIDDEN);

        importToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionImport(), GlobalResources.RESOURCE.importDsd().getURL());
        importToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importDsdWindow.show();
            }
        });
        importToolStripButton.setVisibility(DsdClientSecurityUtils.canImportDsd() ? Visibility.VISIBLE : Visibility.HIDDEN);

        importDsdWindow = new ImportDsdWindow();
        importDsdWindow.setVisibility(Visibility.HIDDEN);
        importDsdWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadComplete(String fileName) {
                getUiHandlers().dsdSuccessfullyImported(fileName);
            }
            @Override
            public void uploadFailed(String fileName) {
                getUiHandlers().dsdImportFailed(fileName);
            }
        });

        exportToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionExport(), GlobalResources.RESOURCE.exportDsd().getURL());
        exportToolStripButton.setVisibility(Visibility.HIDDEN);
        exportToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord record = dsdListGrid.getListGrid().getSelectedRecord();
                if (record instanceof DsdRecord) {
                    getUiHandlers().exportDsd(((DsdRecord) record).getDsd());
                }
            }
        });

        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().dsdDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().dsdDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);

        deleteToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisibility(Visibility.HIDDEN);
        deleteToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.disable().getURL());
        cancelValidityButton.setVisibility(Visibility.HIDDEN);
        cancelValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(getSelectedDsdsUrns());
            }
        });

        ToolStrip dsdGridToolStrip = new ToolStrip();
        dsdGridToolStrip.setWidth100();
        dsdGridToolStrip.addButton(newToolStripButton);
        dsdGridToolStrip.addButton(deleteToolStripButton);
        dsdGridToolStrip.addButton(cancelValidityButton);
        dsdGridToolStrip.addSeparator();
        dsdGridToolStrip.addButton(importToolStripButton);
        dsdGridToolStrip.addButton(exportToolStripButton);

        // Search

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveDsdList(DsdListPresenter.DSD_LIST_FIRST_RESULT, DsdListPresenter.DSD_LIST_MAX_RESULTS, searchSectionStack.getSearchCriteria());
            }
        });

        // DSD ListGrid

        dsdListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (dsdListGrid.getListGrid().getSelectedRecords() != null && dsdListGrid.getListGrid().getSelectedRecords().length == 1) {
                    DsdRecord record = (DsdRecord) dsdListGrid.getListGrid().getSelectedRecord();
                    DataStructureDefinitionMetamacDto dsd = record.getDsd();
                    selectDsd(dsd);
                    showCancelValidityDeleteButton(new ListGridRecord[]{record});
                } else {
                    // No record selected
                    deselectDsd();
                    if (dsdListGrid.getListGrid().getSelectedRecords().length > 1) {
                        // Delete more than one DSD with one click
                        showDeleteToolStripButton();
                        showCancelValidityDeleteButton(dsdListGrid.getListGrid().getSelectedRecords());
                        exportToolStripButton.hide();
                    }
                }
            }
        });
        dsdListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // CheckBox is not clicked
                    dsdListGrid.getListGrid().deselectAllRecords();
                    dsdListGrid.getListGrid().selectRecord(event.getRecord());
                }
            }
        });

        panel.addMember(dsdGridToolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(dsdListGrid);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setDsds(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionDtos, int firstResult, int totalResults) {
        dsdListGrid.setDsds(dataStructureDefinitionDtos, firstResult, totalResults);
    }

    @Override
    public HasRecordClickHandlers getSelectedDsd() {
        return dsdListGrid.getListGrid();
    }

    @Override
    public com.smartgwt.client.widgets.events.HasClickHandlers getDelete() {
        return deleteConfirmationWindow.getYesButton();
    }

    @Override
    public List<String> getSelectedDsdUrns() {
        if (dsdListGrid.getListGrid().getSelectedRecords() != null) {
            ListGridRecord[] records = dsdListGrid.getListGrid().getSelectedRecords();
            List<String> selectedDsdUrns = new ArrayList<String>();
            for (int i = 0; i < records.length; i++) {
                DsdRecord record = (DsdRecord) records[i];
                selectedDsdUrns.add(record.getUrn());
            }
            return selectedDsdUrns;
        }
        return null;
    }

    /**
     * Select DSD in ListGrid
     * 
     * @param dsdSelected
     */
    private void selectDsd(DataStructureDefinitionMetamacDto dsdSelected) {
        if (dsdSelected.getId() == null) {
            // New dimension
            deleteToolStripButton.hide();
            dsdListGrid.getListGrid().deselectAllRecords();
        } else {
            showDeleteToolStripButton();
            showExportToolStripButton();
        }
    }

    /**
     * DeSelect DSD in ListGrid
     */
    private void deselectDsd() {
        deleteToolStripButton.hide();
        exportToolStripButton.hide();
        cancelValidityButton.hide();
    }

    @Override
    public void onNewDsdCreated() {
        dsdListGrid.goToLastPageAfterCreate();
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
    }

    private void showDeleteToolStripButton() {
        List<ProcStatusEnum> statusList = getSelectedDsdsProcStatus();
        boolean actionAllowed = true;
        for (ProcStatusEnum procStatus : statusList) {
            if (!DsdClientSecurityUtils.canDeleteDsd(procStatus)) {
                actionAllowed = false;
                break;
            }
        }
        if (actionAllowed) {
            deleteToolStripButton.show();
        }
    }

    private void showExportToolStripButton() {
        exportToolStripButton.show();
    }

    private List<ProcStatusEnum> getSelectedDsdsProcStatus() {
        List<ProcStatusEnum> status = new ArrayList<ProcStatusEnum>();
        if (dsdListGrid.getListGrid().getSelectedRecords() != null) {
            ListGridRecord[] records = dsdListGrid.getListGrid().getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                DsdRecord record = (DsdRecord) records[i];
                status.add(record.getDsd().getLifeCycle().getProcStatus());
            }
        }
        return status;
    }

    private List<String> getSelectedDsdsUrns() {
        List<String> urns = new ArrayList<String>();
        if (dsdListGrid.getListGrid().getSelectedRecords() != null) {
            ListGridRecord[] records = dsdListGrid.getListGrid().getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                DsdRecord record = (DsdRecord) records[i];
                urns.add(record.getDsd().getUrn());
            }
        }
        return urns;
    }

    private void showCancelValidityDeleteButton(ListGridRecord[] records) {
        boolean allSelectedDsdsExternallyPublished = true;
        for (ListGridRecord record : records) {
            DsdRecord dsdRecord = (DsdRecord) record;
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(dsdRecord.getDsd().getLifeCycle().getProcStatus()) || dsdRecord.getDsd().getValidTo() != null) {
                allSelectedDsdsExternallyPublished = false;
            }
        }
        if (allSelectedDsdsExternallyPublished && DsdClientSecurityUtils.canCancelDsdValidity()) {
            cancelValidityButton.show();
        } else {
            cancelValidityButton.hide();
        }
    }
}
