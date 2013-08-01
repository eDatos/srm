package org.siemac.metamac.srm.web.dsd.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.utils.TasksClientSecurityUtils;
import org.siemac.metamac.srm.web.client.widgets.DsdPaginatedListGrid;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
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
        newToolStripButton.setVisible(DsdClientSecurityUtils.canCreateDsd());

        exportToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionExport(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportToolStripButton.setVisible(false);
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
        deleteConfirmationWindow.setVisible(false);

        deleteToolStripButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionDelete(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteToolStripButton.setVisible(false);
        deleteToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.disable().getURL());
        cancelValidityButton.setVisible(false);
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

        dsdListGrid = new DsdPaginatedListGrid(SrmWebConstants.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveDsdList(firstResult, maxResults, searchSectionStack.getDataStructureDefinitionWebCriteria());
            }
        });
        dsdListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (dsdListGrid.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showDeleteToolStripButton(dsdListGrid.getListGrid().getSelectedRecords());
                    // Show cancel validity button
                    showCancelValidityDeleteButton(dsdListGrid.getListGrid().getSelectedRecords());
                    // Show export button
                    showExportToolStripButton(dsdListGrid.getListGrid().getSelectedRecords());
                } else {
                    hideSelectionDependentButtons();
                }

            }
        });
        dsdListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // CheckBox is not clicked
                    String urn = ((DsdRecord) event.getRecord()).getAttribute(DataStructureDefinitionDS.URN);
                    getUiHandlers().goToDsd(urn);
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
        dsdListGrid.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setDsds(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionDtos, int firstResult, int totalResults) {
        dsdListGrid.setDsds(dataStructureDefinitionDtos, firstResult, totalResults);
        hideSelectionDependentButtons();
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

    private void showDeleteToolStripButton(ListGridRecord[] records) {
        boolean allSelectedDsdsCanBeDeleted = true;
        for (ListGridRecord record : records) {
            DataStructureDefinitionMetamacBasicDto dsd = ((DsdRecord) record).getDsdBasicDto();
            if (!DsdClientSecurityUtils.canDeleteDsd(dsd.getLifeCycle().getProcStatus(), CommonUtils.getStatisticalOperationCodeFromDsd(dsd))) {
                allSelectedDsdsCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedDsdsCanBeDeleted) {
            deleteToolStripButton.show();
        } else {
            deleteToolStripButton.hide();
        }
    }

    private void showExportToolStripButton(ListGridRecord[] records) {
        if (records.length == 1) {
            DataStructureDefinitionMetamacBasicDto dsd = ((DsdRecord) records[0]).getDsdBasicDto();
            if (TasksClientSecurityUtils.canExportStructure(dsd.getVersionLogic())) {
                exportToolStripButton.show();
            }
        } else {
            exportToolStripButton.hide();
        }
    }

    private List<String> getSelectedDsdsUrns() {
        List<String> urns = new ArrayList<String>();
        if (dsdListGrid.getListGrid().getSelectedRecords() != null) {
            ListGridRecord[] records = dsdListGrid.getListGrid().getSelectedRecords();
            for (int i = 0; i < records.length; i++) {
                DsdRecord record = (DsdRecord) records[i];
                urns.add(record.getUrn());
            }
        }
        return urns;
    }

    private void showCancelValidityDeleteButton(ListGridRecord[] records) {
        boolean allSelectedDsdsExternallyPublished = true;
        for (ListGridRecord record : records) {
            DataStructureDefinitionMetamacBasicDto dsd = ((DsdRecord) record).getDsdBasicDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!DsdClientSecurityUtils.canCancelDsdValidity(dsd)) {
                allSelectedDsdsExternallyPublished = false;
            }
        }
        if (allSelectedDsdsExternallyPublished) {
            cancelValidityButton.show();
        } else {
            cancelValidityButton.hide();
        }
    }

    private void hideSelectionDependentButtons() {
        deleteToolStripButton.hide();
        cancelValidityButton.hide();
        exportToolStripButton.hide();
    }
}
