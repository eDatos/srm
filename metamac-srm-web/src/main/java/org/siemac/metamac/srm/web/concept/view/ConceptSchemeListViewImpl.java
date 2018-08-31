package org.siemac.metamac.srm.web.concept.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourcePaginatedCheckListGrid;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsRecordUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptSchemeSearchSectionStack;
import org.siemac.metamac.srm.web.concept.widgets.NewConceptSchemeWindow;
import org.siemac.metamac.srm.web.dsd.widgets.ExportSdmxResourceWindow;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ConceptSchemeListViewImpl extends ViewWithUiHandlers<ConceptSchemeListUiHandlers> implements ConceptSchemeListPresenter.ConceptSchemeListView {

    private VLayout                                   panel;

    private ToolStripButton                           newButton;
    private ToolStripButton                           deleteButton;
    private ToolStripButton                           exportButton;
    private ToolStripButton                           cancelValidityButton;

    private ConceptSchemeSearchSectionStack           searchSectionStack;

    private VersionableResourcePaginatedCheckListGrid conceptSchemesList;

    private NewConceptSchemeWindow                    newConceptSchemeWindow;
    private DeleteConfirmationWindow                  deleteConfirmationWindow;

    @Inject
    public ConceptSchemeListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newConceptSchemeWindow = new NewConceptSchemeWindow(getConstants().conceptSchemeCreate());
                newConceptSchemeWindow.setUiHandlers(getUiHandlers());
                newConceptSchemeWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptSchemeWindow.validateForm()) {
                            getUiHandlers().createConceptScheme(newConceptSchemeWindow.getNewConceptSchemeDto());
                            newConceptSchemeWindow.destroy();
                        }
                    }
                });
            }
        });
        newButton.setVisible(ConceptsClientSecurityUtils.canCreateConceptScheme());

        deleteButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisible(false);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        exportButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionExportSdmxMl(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportButton.setVisible(false);
        exportButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<String> urns = getUrnsFromSelectedConceptSchemes();
                if (!urns.isEmpty()) {
                    showExportationWindow(urns);
                }
            }

            protected void showExportationWindow(final List<String> urns) {
                new ExportSdmxResourceWindow() {

                    @Override
                    protected void startExportation(ExportDetailEnum infoAmount, ExportReferencesEnum references) {
                        getUiHandlers().exportConceptSchemes(urns, infoAmount, references);
                    }
                };
            }
        });

        cancelValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelValidityButton.setVisible(false);
        cancelValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(getUrnsFromSelectedConceptSchemes());
            }
        });

        toolStrip.addButton(newButton);
        toolStrip.addButton(deleteButton);
        toolStrip.addButton(cancelValidityButton);
        toolStrip.addButton(exportButton);

        // Search

        searchSectionStack = new ConceptSchemeSearchSectionStack();

        // Concepts scheme list

        conceptSchemesList = new VersionableResourcePaginatedCheckListGrid(SrmWebConstants.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveConceptSchemes(firstResult, maxResults, searchSectionStack.getConceptSchemeWebCriteria());
            }
        });
        conceptSchemesList.getListGrid().setDataSource(new ConceptSchemeDS());
        conceptSchemesList.getListGrid().setUseAllDataSourceFields(false);
        conceptSchemesList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (conceptSchemesList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(conceptSchemesList.getListGrid().getSelectedRecords());
                    // Show cancel validity button
                    showListGridCancelValidityDeleteButton(conceptSchemesList.getListGrid().getSelectedRecords());
                    // Show export button
                    showListGridExportButton(conceptSchemesList.getListGrid().getSelectedRecords());
                } else {
                    hideSelectionDependentButtons();
                }
            }
        });

        conceptSchemesList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((ConceptSchemeRecord) event.getRecord()).getAttribute(ConceptSchemeDS.URN);
                    getUiHandlers().goToConceptScheme(urn);
                }
            }
        });

        conceptSchemesList.getListGrid().setFields(ResourceFieldUtils.getConceptSchemeListGridFields());
        conceptSchemesList.setHeight100();

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().conceptSchemeDeleteConfirmationTitle(), getConstants().conceptSchemeDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteConceptSchemes(getUrnsFromSelectedConceptSchemes());
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(conceptSchemesList);
    }

    @Override
    public void setConceptSchemePaginatedList(GetConceptSchemesResult conceptSchemesPaginatedList) {
        setConceptSchemeList(conceptSchemesPaginatedList.getConceptSchemeList());
        conceptSchemesList.refreshPaginationInfo(conceptSchemesPaginatedList.getFirstResultOut(), conceptSchemesPaginatedList.getConceptSchemeList().size(),
                conceptSchemesPaginatedList.getTotalResults());
        hideSelectionDependentButtons();
    }

    @Override
    public void goToConceptSchemeListLastPageAfterCreate() {
        conceptSchemesList.goToLastPageAfterCreate();
    }

    private void setConceptSchemeList(List<ConceptSchemeMetamacBasicDto> conceptSchemesDtos) {
        ConceptSchemeRecord[] records = new ConceptSchemeRecord[conceptSchemesDtos.size()];
        int index = 0;
        for (ConceptSchemeMetamacBasicDto scheme : conceptSchemesDtos) {
            records[index++] = ConceptsRecordUtils.getConceptSchemeRecord(scheme);
        }
        conceptSchemesList.getListGrid().setData(records);
    }

    public List<String> getUrnsFromSelectedConceptSchemes() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : conceptSchemesList.getListGrid().getSelectedRecords()) {
            ConceptSchemeRecord schemeRecord = (ConceptSchemeRecord) record;
            urns.add(schemeRecord.getUrn());
        }
        return urns;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == ConceptSchemeListPresenter.TYPE_SetContextAreaContentConceptsToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        conceptSchemesList.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    public void setOperations(GetStatisticalOperationsResult result) {
        if (newConceptSchemeWindow != null) {
            newConceptSchemeWindow.setOperations(result);
        }
    }

    @Override
    public void setOperationsForSearchSection(GetStatisticalOperationsResult result) {
        searchSectionStack.setOperations(result);
    }

    @Override
    public ConceptSchemeWebCriteria getConceptSchemeWebCriteria() {
        return searchSectionStack.getConceptSchemeWebCriteria();
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = ((ConceptSchemeRecord) record).getConceptSchemeBasicDto();
            if (!ConceptsClientSecurityUtils.canDeleteConceptScheme(conceptSchemeMetamacDto.getLifeCycle().getProcStatus(), conceptSchemeMetamacDto.getType(),
                    CommonUtils.getRelatedOperationCode(conceptSchemeMetamacDto))) {
                allSelectedSchemesCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedSchemesCanBeDeleted) {
            deleteButton.show();
        } else {
            deleteButton.hide();
        }
    }

    private void showListGridCancelValidityDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesValidityCanBeCanceled = true;
        for (ListGridRecord record : records) {
            ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = ((ConceptSchemeRecord) record).getConceptSchemeBasicDto();
            if (!ConceptsClientSecurityUtils.canCancelConceptSchemeValidity(conceptSchemeMetamacDto)) {
                allSelectedSchemesValidityCanBeCanceled = false;
            }
        }
        if (allSelectedSchemesValidityCanBeCanceled) {
            cancelValidityButton.show();
        } else {
            cancelValidityButton.hide();
        }
    }

    private void showListGridExportButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeExported = true;
        for (ListGridRecord record : records) {
            ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto = ((ConceptSchemeRecord) record).getConceptSchemeBasicDto();
            if (!ConceptsClientSecurityUtils.canExportConceptScheme(conceptSchemeMetamacDto.getVersionLogic())) {
                allSelectedSchemesCanBeExported = false;
            }
        }
        if (allSelectedSchemesCanBeExported) {
            exportButton.show();
        } else {
            exportButton.hide();
        }
    }

    private void hideSelectionDependentButtons() {
        deleteButton.hide();
        cancelValidityButton.hide();
        exportButton.hide();
    }
}
