package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.code.model.record.VariableRecord;
import org.siemac.metamac.srm.web.code.presenter.VariableListPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.VariableListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.NewVariableWindow;
import org.siemac.metamac.srm.web.code.widgets.VariableSearchSectionStack;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
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

    private VLayout                    panel;

    private ToolStripButton            newVariableButton;
    private ToolStripButton            deleteVariableButton;

    private VariableSearchSectionStack searchSectionStack;

    private PaginatedCheckListGrid     variablesList;

    private NewVariableWindow          newVariableWindow;
    private DeleteConfirmationWindow   deleteConfirmationWindow;

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
                newVariableWindow = new NewVariableWindow(getConstants().variableCreate(), getUiHandlers());
                // Load families
                getUiHandlers().retrieveVariableFamilies(VariableListPresenter.FAMILY_LIST_FIRST_RESULT, VariableListPresenter.FAMILY_LIST_MAX_RESULTS, null);
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
        newVariableButton.setVisible(CodesClientSecurityUtils.canCreateVariable());

        deleteVariableButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteVariableButton.setVisible(false);
        deleteVariableButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newVariableButton);
        toolStrip.addButton(deleteVariableButton);

        // Search
        searchSectionStack = new VariableSearchSectionStack();

        // Variable list

        variablesList = new PaginatedCheckListGrid(VariableListPresenter.VARIABLE_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariables(firstResult, maxResults, searchSectionStack.getVariableWebCriteria());
            }
        });
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
                    String code = ((VariableRecord) event.getRecord()).getAttribute(VariableDS.CODE);
                    getUiHandlers().goToVariable(code);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(VariableDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(VariableDS.NAME, getConstants().nameableArtefactName());
        variablesList.getListGrid().setFields(fieldCode, fieldName);

        variablesList.setHeight100();

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().variableDeleteConfirmationTitle(), getConstants().variableDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteVariables(CommonUtils.getUrnsFromSelectedVariables(variablesList.getListGrid().getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(variablesList);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(VariableListUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
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

    private void setVariableList(List<VariableBasicDto> variableDtos) {
        VariableRecord[] records = new VariableRecord[variableDtos.size()];
        int index = 0;
        for (VariableBasicDto scheme : variableDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.CodesRecordUtils.getVariableRecord(scheme);
        }
        variablesList.getListGrid().setData(records);
    }

    @Override
    public void setVariableFamilies(List<RelatedResourceDto> families, int firstResult, int totalResults) {
        if (newVariableWindow != null) {
            newVariableWindow.setVariableFamilies(families, firstResult, totalResults);
        }
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    @Override
    public VariableWebCriteria getVariableCriteria() {
        return searchSectionStack.getVariableWebCriteria();
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedVariablesCanBeDeleted = true;
        for (ListGridRecord record : records) {
            VariableBasicDto variable = ((VariableRecord) record).getVariableBasicDto();
            if (!CodesClientSecurityUtils.canDeleteVariable(variable)) {
                allSelectedVariablesCanBeDeleted = false;
                break;
            }
        }
        if (allSelectedVariablesCanBeDeleted) {
            deleteVariableButton.show();
        } else {
            deleteVariableButton.hide();
        }
    }
}
