package org.siemac.metamac.srm.web.client.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.client.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.client.code.model.record.VariableRecord;
import org.siemac.metamac.srm.web.client.code.presenter.VariableListPresenter;
import org.siemac.metamac.srm.web.client.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.code.view.handlers.VariableListUiHandlers;
import org.siemac.metamac.srm.web.client.code.widgets.NewVariableWindow;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
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

public class VariableListViewImpl extends ViewWithUiHandlers<VariableListUiHandlers> implements VariableListPresenter.VariableListView {

    private VLayout                  panel;

    private ToolStripButton          newVariableButton;
    private ToolStripButton          deleteVariableButton;

    private SearchSectionStack       searchSectionStack;

    private PaginatedCheckListGrid   variablesList;

    private NewVariableWindow        newVariableWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    @Inject
    public VariableListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newVariableButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newVariableButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newVariableWindow = new NewVariableWindow(getConstants().variableCreate());
                newVariableWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newVariableWindow.validateForm()) {
                            getUiHandlers().createVariable(newVariableWindow.getNewVariableDto());
                            newVariableWindow.destroy();
                        }
                    }
                });
            }
        });
        // TODO newVariableButton.setVisibility(CodesClientSecurityUtils.canCreateVariable() ? Visibility.VISIBLE : Visibility.HIDDEN);

        deleteVariableButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteVariableButton.setVisibility(Visibility.HIDDEN);
        deleteVariableButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newVariableButton);
        toolStrip.addButton(deleteVariableButton);

        // Search

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveVariables(VariableListPresenter.VARIABLE_LIST_FIRST_RESULT, VariableListPresenter.VARIABLE_LIST_MAX_RESULTS, searchSectionStack.getSearchCriteria());
            }
        });

        // Variable list

        variablesList = new PaginatedCheckListGrid(VariableListPresenter.VARIABLE_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariables(firstResult, maxResults, null);
            }
        });
        variablesList.getListGrid().setAutoFitMaxRecords(VariableListPresenter.VARIABLE_LIST_MAX_RESULTS);
        variablesList.getListGrid().setAutoFitData(Autofit.VERTICAL);
        variablesList.getListGrid().setDataSource(new VariableDS());
        variablesList.getListGrid().setUseAllDataSourceFields(false);
        variablesList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (variablesList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(variablesList.getListGrid().getSelectedRecords());
                } else {
                    deleteVariableButton.hide();
                }
            }
        });

        variablesList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String urn = ((VariableRecord) event.getRecord()).getAttribute(VariableDS.URN);
                    getUiHandlers().goToVariable(urn);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(VariableDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(VariableDS.NAME, getConstants().nameableArtefactName());
        variablesList.getListGrid().setFields(fieldCode, fieldName);

        panel = new VLayout();
        panel.setHeight100();
        panel.setOverflow(Overflow.SCROLL);
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(variablesList);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().variableDeleteConfirmationTitle(), getConstants().variableDeleteConfirmation());
        deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteVariables(CommonUtils.getUrnsFromSelectedVariables(variablesList.getListGrid().getSelectedRecords()));
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
        if (slot == VariableListPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setVariablePaginatedList(GetVariablesResult variablesPaginatedList) {
        setVariableList(variablesPaginatedList.getVariables());
        variablesList.refreshPaginationInfo(variablesPaginatedList.getFirstResultOut(), variablesPaginatedList.getVariables().size(), variablesPaginatedList.getTotalResults());
    }

    private void setVariableList(List<VariableDto> variableDtos) {
        VariableRecord[] records = new VariableRecord[variableDtos.size()];
        int index = 0;
        for (VariableDto scheme : variableDtos) {
            records[index++] = org.siemac.metamac.srm.web.client.code.utils.RecordUtils.getVariableRecord(scheme);
        }
        variablesList.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedVariablesCanBeDeleted = true;
        // TODO Security
        if (allSelectedVariablesCanBeDeleted) {
            deleteVariableButton.show();
        } else {
            deleteVariableButton.hide();
        }
    }
}
