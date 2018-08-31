package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourcePaginatedCheckListGrid;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.presenter.CodelistListPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.CodelistSearchSectionStack;
import org.siemac.metamac.srm.web.code.widgets.NewCodelistWindow;
import org.siemac.metamac.srm.web.dsd.widgets.ExportSdmxResourceWindow;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
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

public class CodelistListViewImpl extends ViewWithUiHandlers<CodelistListUiHandlers> implements CodelistListPresenter.CodelistListView {

    private VLayout                                   panel;

    private ToolStripButton                           newButton;
    private ToolStripButton                           deleteButton;
    private ToolStripButton                           exportButton;
    private ToolStripButton                           cancelValidityButton;

    private CodelistSearchSectionStack                searchSectionStack;

    private VersionableResourcePaginatedCheckListGrid codelistsList;

    private NewCodelistWindow                         newCodelistWindow;
    private DeleteConfirmationWindow                  deleteConfirmationWindow;

    @Inject
    public CodelistListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newCodelistWindow = new NewCodelistWindow(getConstants().codelistCreate(), getUiHandlers());
                newCodelistWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCodelistWindow.validateForm()) {
                            getUiHandlers().createCodelist(newCodelistWindow.getNewCodelistDto());
                            newCodelistWindow.destroy();
                        }
                    }
                });
            }
        });
        newButton.setVisible(CodesClientSecurityUtils.canCreateCodelist());

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
                List<String> urns = CommonUtils.getUrnsFromSelectedCodelists(codelistsList.getListGrid().getSelectedRecords());
                if (!urns.isEmpty()) {
                    showExportationWindow(urns);
                }
            }

            protected void showExportationWindow(final List<String> urns) {
                new ExportSdmxResourceWindow() {

                    @Override
                    protected void startExportation(ExportDetailEnum infoAmount, ExportReferencesEnum references) {
                        getUiHandlers().exportCodelists(urns, infoAmount, references);
                    }
                };
            }
        });

        cancelValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelValidityButton.setVisible(false);
        cancelValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(CommonUtils.getUrnsFromSelectedCodelists(codelistsList.getListGrid().getSelectedRecords()));
            }
        });

        toolStrip.addButton(newButton);
        toolStrip.addButton(deleteButton);
        toolStrip.addButton(cancelValidityButton);
        toolStrip.addButton(exportButton);

        // Search

        searchSectionStack = new CodelistSearchSectionStack();

        // Codelist list

        codelistsList = new VersionableResourcePaginatedCheckListGrid(SrmWebConstants.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCodelists(firstResult, maxResults, searchSectionStack.getCodelistWebCriteria());
            }
        });
        codelistsList.getListGrid().setDataSource(new CodelistDS());
        codelistsList.getListGrid().setUseAllDataSourceFields(false);
        codelistsList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (codelistsList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(codelistsList.getListGrid().getSelectedRecords());
                    // Show cancel validity button
                    showListGridCancelValidityButton(codelistsList.getListGrid().getSelectedRecords());
                    // Show export button
                    showListGridExportButton(codelistsList.getListGrid().getSelectedRecords());
                } else {
                    hideSelectionDependentButtons();
                }
            }
        });

        codelistsList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((CodelistRecord) event.getRecord()).getAttribute(CodelistDS.URN);
                    getUiHandlers().goToCodelist(urn);
                }
            }
        });

        codelistsList.getListGrid().setFields(ResourceFieldUtils.getCodelistListGridFields());
        codelistsList.setHeight100();

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistDeleteConfirmationTitle(), getConstants().codelistDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCodelists(CommonUtils.getUrnsFromSelectedCodelists(codelistsList.getListGrid().getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(codelistsList);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(CodelistListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        codelistsList.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodelistListPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setCodelists(GetCodelistsResult codelistsPaginatedList) {
        setCodelistList(codelistsPaginatedList.getCodelists());
        codelistsList.refreshPaginationInfo(codelistsPaginatedList.getFirstResultOut(), codelistsPaginatedList.getCodelists().size(), codelistsPaginatedList.getTotalResults());
        hideSelectionDependentButtons();
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (newCodelistWindow != null) {
            newCodelistWindow.setVariables(result);
        }
    }

    @Override
    public void setVariablesForSearch(GetVariablesResult result) {
        searchSectionStack.setVariables(result);
    }

    @Override
    public void setVariableElementsForSearch(GetVariableElementsResult result) {
        searchSectionStack.setVariableElements(result);
    }

    @Override
    public void setVariableFamiliesForSearch(GetVariableFamiliesResult result) {
        searchSectionStack.setVariableFamilies(result);
    }

    private void setCodelistList(List<CodelistMetamacBasicDto> codelistDtos) {
        CodelistRecord[] records = new CodelistRecord[codelistDtos.size()];
        int index = 0;
        for (CodelistMetamacBasicDto scheme : codelistDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.CodesRecordUtils.getCodelistRecord(scheme);
        }
        codelistsList.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    public CodelistWebCriteria getCodelistWebCriteria() {
        return searchSectionStack.getCodelistWebCriteria();
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            CodelistMetamacBasicDto codelistMetamacDto = ((CodelistRecord) record).getCodelistMetamacBasicDto();
            if (!CodesClientSecurityUtils.canDeleteCodelist(codelistMetamacDto.getLifeCycle().getProcStatus(), codelistMetamacDto.getIsTaskInBackground())) {
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

    private void showListGridCancelValidityButton(ListGridRecord[] records) {
        boolean allSelectedCodelistValidityCanBeCanceled = true;
        for (ListGridRecord record : records) {
            CodelistMetamacBasicDto codelistMetamacDto = ((CodelistRecord) record).getCodelistMetamacBasicDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!CodesClientSecurityUtils.canCancelCodelistValidity(codelistMetamacDto)) {
                allSelectedCodelistValidityCanBeCanceled = false;
            }
        }
        if (allSelectedCodelistValidityCanBeCanceled) {
            cancelValidityButton.show();
        } else {
            cancelValidityButton.hide();
        }
    }

    private void showListGridExportButton(ListGridRecord[] records) {
        boolean allSelectedCodelistCanBeExported = true;
        for (ListGridRecord record : records) {
            CodelistMetamacBasicDto codelistMetamacDto = ((CodelistRecord) record).getCodelistMetamacBasicDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!CodesClientSecurityUtils.canExportCodelist(codelistMetamacDto.getVersionLogic())) {
                allSelectedCodelistCanBeExported = false;
            }
        }
        if (allSelectedCodelistCanBeExported) {
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
