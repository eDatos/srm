package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.presenter.CodelistListPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.NewCodelistWindow;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistListViewImpl extends ViewWithUiHandlers<CodelistListUiHandlers> implements CodelistListPresenter.CodelistListView {

    private VLayout                  panel;

    private ToolStripButton          newCodelistButton;
    private ToolStripButton          deleteCodelistButton;
    private ToolStripButton          cancelCodelistValidityButton;

    private SearchSectionStack       searchSectionStack;

    private PaginatedCheckListGrid   codelistsList;

    private NewCodelistWindow        newCodelistWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    @Inject
    public CodelistListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCodelistButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newCodelistButton.addClickHandler(new ClickHandler() {

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
        newCodelistButton.setVisibility(CodesClientSecurityUtils.canCreateCodelist() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteCodelistButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteCodelistButton.setVisibility(Visibility.HIDDEN);
        deleteCodelistButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        cancelCodelistValidityButton = new ToolStripButton(getConstants().lifeCycleCancelValidity(), GlobalResources.RESOURCE.disable().getURL());
        cancelCodelistValidityButton.setVisibility(Visibility.HIDDEN);
        cancelCodelistValidityButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().cancelValidity(CommonUtils.getUrnsFromSelectedCodelists(codelistsList.getListGrid().getSelectedRecords()));
            }
        });

        toolStrip.addButton(newCodelistButton);
        toolStrip.addButton(deleteCodelistButton);
        toolStrip.addButton(cancelCodelistValidityButton);

        // Search

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveCodelists(CodelistListPresenter.SCHEME_LIST_FIRST_RESULT, CodelistListPresenter.SCHEME_LIST_MAX_RESULTS, searchSectionStack.getSearchCriteria());
            }
        });

        // Codelist list

        codelistsList = new PaginatedCheckListGrid(CodelistListPresenter.SCHEME_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCodelists(firstResult, maxResults, searchSectionStack.getSearchCriteria());
            }
        });
        codelistsList.getListGrid().setAutoFitMaxRecords(CodelistListPresenter.SCHEME_LIST_MAX_RESULTS);
        codelistsList.getListGrid().setAutoFitData(Autofit.VERTICAL);
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
                } else {
                    deleteCodelistButton.hide();
                    cancelCodelistValidityButton.hide();
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

        ListGridField fieldCode = new ListGridField(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(CodelistDS.NAME, getConstants().nameableArtefactName());
        ListGridField status = new ListGridField(CodelistDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        codelistsList.getListGrid().setFields(fieldCode, fieldName, status);

        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(codelistsList);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().codelistDeleteConfirmationTitle(), getConstants().codelistDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCodelists(CommonUtils.getUrnsFromSelectedCodelists(codelistsList.getListGrid().getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
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
    public void setCodelistPaginatedList(GetCodelistsResult codelistsPaginatedList) {
        setCodelistList(codelistsPaginatedList.getCodelists());
        codelistsList.refreshPaginationInfo(codelistsPaginatedList.getFirstResultOut(), codelistsPaginatedList.getCodelists().size(), codelistsPaginatedList.getTotalResults());
    }

    @Override
    public void setVariables(GetVariablesResult result) {
        if (newCodelistWindow != null) {
            newCodelistWindow.setVariables(result);
        }
    }

    private void setCodelistList(List<CodelistMetamacDto> codelistDtos) {
        CodelistRecord[] records = new CodelistRecord[codelistDtos.size()];
        int index = 0;
        for (CodelistMetamacDto scheme : codelistDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.RecordUtils.getCodelistRecord(scheme);
        }
        codelistsList.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedSchemesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            CodelistMetamacDto codelistMetamacDto = ((CodelistRecord) record).getCodelistMetamacDto();
            if (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(codelistMetamacDto.getLifeCycle().getProcStatus())
                    || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(codelistMetamacDto.getLifeCycle().getProcStatus()) || !CodesClientSecurityUtils.canDeleteCodelist()) {
                allSelectedSchemesCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedSchemesCanBeDeleted) {
            deleteCodelistButton.show();
        } else {
            deleteCodelistButton.hide();
        }
    }

    private void showListGridCancelValidityButton(ListGridRecord[] records) {
        boolean allSelectedCodelistValidityCanBeCanceled = true;
        for (ListGridRecord record : records) {
            CodelistMetamacDto codelistMetamacDto = ((CodelistRecord) record).getCodelistMetamacDto();
            // Do not show cancel validity button if scheme is not published externally or if scheme validity has been canceled previously
            if (!ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(codelistMetamacDto.getLifeCycle().getProcStatus()) || codelistMetamacDto.getValidTo() != null
                    || !CodesClientSecurityUtils.canCancelCodelistValidity()) {
                allSelectedCodelistValidityCanBeCanceled = false;
            }
        }
        if (allSelectedCodelistValidityCanBeCanceled) {
            cancelCodelistValidityButton.show();
        } else {
            cancelCodelistValidityButton.hide();
        }
    }
}