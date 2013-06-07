package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableFamilyDS;
import org.siemac.metamac.srm.web.code.model.record.VariableFamilyRecord;
import org.siemac.metamac.srm.web.code.presenter.VariableFamilyListPresenter;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.RecordUtils;
import org.siemac.metamac.srm.web.code.view.handlers.VariableFamilyListUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.NewVariableFamilyWindow;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
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

public class VariableFamilyListViewImpl extends ViewWithUiHandlers<VariableFamilyListUiHandlers> implements VariableFamilyListPresenter.VariableFamilyListView {

    private VLayout                  panel;

    private ToolStripButton          newVariableFamilyButton;
    private ToolStripButton          deleteVariableFamilyButton;

    private SearchSectionStack       searchSectionStack;

    private PaginatedCheckListGrid   variableFamilyList;

    private NewVariableFamilyWindow  newVariableFamilyWindow;
    private DeleteConfirmationWindow deleteConfirmationWindow;

    @Inject
    public VariableFamilyListViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newVariableFamilyButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        newVariableFamilyButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                newVariableFamilyWindow = new NewVariableFamilyWindow(getConstants().variableFamilyCreate());
                newVariableFamilyWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newVariableFamilyWindow.validateForm()) {
                            getUiHandlers().createVariableFamily(newVariableFamilyWindow.getNewVariableFamilyDto());
                            newVariableFamilyWindow.destroy();
                        }
                    }
                });
            }
        });
        newVariableFamilyButton.setVisible(CodesClientSecurityUtils.canCreateVariableFamily());

        deleteVariableFamilyButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteVariableFamilyButton.setVisible(false);
        deleteVariableFamilyButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        toolStrip.addButton(newVariableFamilyButton);
        toolStrip.addButton(deleteVariableFamilyButton);

        // Search

        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveVariableFamilies(VariableFamilyListPresenter.FAMILY_LIST_FIRST_RESULT, VariableFamilyListPresenter.FAMILY_LIST_MAX_RESULTS,
                        searchSectionStack.getSearchCriteria());
            }
        });

        // Family list

        variableFamilyList = new PaginatedCheckListGrid(VariableFamilyListPresenter.FAMILY_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableFamilies(firstResult, maxResults, searchSectionStack.getSearchCriteria());
            }
        });
        variableFamilyList.getListGrid().setAutoFitMaxRecords(VariableFamilyListPresenter.FAMILY_LIST_MAX_RESULTS);
        variableFamilyList.getListGrid().setAutoFitData(Autofit.VERTICAL);
        variableFamilyList.getListGrid().setDataSource(new VariableFamilyDS());
        variableFamilyList.getListGrid().setUseAllDataSourceFields(false);
        variableFamilyList.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (variableFamilyList.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(variableFamilyList.getListGrid().getSelectedRecords());
                } else {
                    deleteVariableFamilyButton.hide();
                }
            }
        });

        variableFamilyList.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String code = ((VariableFamilyRecord) event.getRecord()).getAttribute(VariableFamilyDS.CODE);
                    getUiHandlers().goToVariableFamily(code);
                }
            }
        });

        ListGridField fieldCode = new ListGridField(VariableFamilyDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(VariableFamilyDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(VariableFamilyDS.URN, getConstants().identifiableArtefactUrn());
        variableFamilyList.getListGrid().setFields(fieldCode, fieldName, urn);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().variableFamilyDeleteConfirmationTitle(), getConstants().variableFamilyDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteVariableFamilies(getUrnsFromSelectedFamilies());
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.setHeight100();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(toolStrip);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(variableFamilyList);

        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == VariableFamilyListPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setVariableFamilyPaginatedList(GetVariableFamiliesResult variableFamiliesPaginatedList) {
        setVariableFamilies(variableFamiliesPaginatedList.getFamilies());
        variableFamilyList
                .refreshPaginationInfo(variableFamiliesPaginatedList.getFirstResultOut(), variableFamiliesPaginatedList.getFamilies().size(), variableFamiliesPaginatedList.getTotalResults());
    }

    private void setVariableFamilies(List<VariableFamilyBasicDto> variableFamilyDtos) {
        VariableFamilyRecord[] records = RecordUtils.getVariableFamilyRecords(variableFamilyDtos);
        variableFamilyList.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
    }

    private List<String> getUrnsFromSelectedFamilies() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : variableFamilyList.getListGrid().getSelectedRecords()) {
            VariableFamilyRecord familyRecord = (VariableFamilyRecord) record;
            urns.add(familyRecord.getUrn());
        }
        return urns;
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        if (CodesClientSecurityUtils.canDeleteVariableFamily()) {
            deleteVariableFamilyButton.show();
        }
    }
}
