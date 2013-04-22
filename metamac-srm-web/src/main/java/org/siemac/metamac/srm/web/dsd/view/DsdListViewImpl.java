package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.utils.ImportationClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.DsdPaginatedListGrid;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.utils.DsdClientSecurityUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.dsd.widgets.DsdSearchSectionStack;
import org.siemac.metamac.srm.web.dsd.widgets.NewDsdWindow;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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
    private ToolStripButton          exportToolStripButton;
    private ToolStripButton          cancelValidityButton;

    private NewDsdWindow             newDsdWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    private DsdSearchSectionStack    searchSectionStack;

    @Inject
    public DsdListViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);

        dsdListGrid = new DsdPaginatedListGrid(DsdListPresenter.DSD_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveDsdList(firstResult, maxResults, searchSectionStack.getDataStructureDefinitionWebCriteria());
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
                newDsdWindow = new NewDsdWindow(getUiHandlers());
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

        exportToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionExport(), GlobalResources.RESOURCE.exportSDMXResource().getURL());
        exportToolStripButton.setVisibility(Visibility.HIDDEN);
        exportToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord record = dsdListGrid.getListGrid().getSelectedRecord();
                if (record instanceof DsdRecord) {
                    getUiHandlers().exportDsd(((DsdRecord) record).getUrn());
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
        dsdGridToolStrip.addButton(exportToolStripButton);

        // Search

        searchSectionStack = new DsdSearchSectionStack();

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
    public void setUiHandlers(DsdListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
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
     * @param selectedDsd
     */
    private void selectDsd(DataStructureDefinitionMetamacDto selectedDsd) {
        if (selectedDsd.getId() == null) {
            deleteToolStripButton.hide();
            dsdListGrid.getListGrid().deselectAllRecords();
        } else {
            showDeleteToolStripButton();
            showExportToolStripButton(selectedDsd);
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
        searchSectionStack.clearSearchSection();
    }

    @Override
    public void setOperations(GetStatisticalOperationsResult result) {
        if (newDsdWindow != null) {
            newDsdWindow.setOperations(result);
        }
    }

    @Override
    public void setOperationsForSearchSection(GetStatisticalOperationsResult result) {
        searchSectionStack.setOperations(result);
    }

    @Override
    public void setDimensionConceptsForSearchSection(GetConceptsResult result) {
        searchSectionStack.setDimensionConcepts(result);
    }

    @Override
    public void setAttributeConceptsForSearchSection(GetConceptsResult result) {
        searchSectionStack.setAttributeConcepts(result);
    }

    private void showDeleteToolStripButton() {
        List<DataStructureDefinitionMetamacDto> dsds = getSelectedDsds();
        boolean actionAllowed = true;
        for (DataStructureDefinitionMetamacDto dsd : dsds) {
            if (!DsdClientSecurityUtils.canDeleteDsd(dsd.getLifeCycle().getProcStatus(), CommonUtils.getStatisticalOperationCodeFromDsd(dsd))) {
                actionAllowed = false;
                break;
            }
        }
        if (actionAllowed) {
            deleteToolStripButton.show();
        }
    }

    private void showExportToolStripButton(DataStructureDefinitionMetamacDto selectedDsd) {
        if (ImportationClientSecurityUtils.canExportStructure(selectedDsd.getVersionLogic())) {
            exportToolStripButton.show();
        }
    }

    private List<DataStructureDefinitionMetamacDto> getSelectedDsds() {
        List<DataStructureDefinitionMetamacDto> dsds = new ArrayList<DataStructureDefinitionMetamacDto>();
        if (dsdListGrid.getListGrid().getSelectedRecords() != null) {
            ListGridRecord[] records = dsdListGrid.getListGrid().getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                DsdRecord record = (DsdRecord) records[i];
                dsds.add(record.getDsd());
            }
        }
        return dsds;
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
            DataStructureDefinitionMetamacDto dsd = ((DsdRecord) record).getDsd();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(dsd.getLifeCycle().getProcStatus()) || dsd.getValidTo() != null
                    || !DsdClientSecurityUtils.canCancelDsdValidity(CommonUtils.getStatisticalOperationCodeFromDsd(dsd))) {
                allSelectedDsdsExternallyPublished = false;
            }
        }
        if (allSelectedDsdsExternallyPublished) {
            cancelValidityButton.show();
        } else {
            cancelValidityButton.hide();
        }
    }
}
