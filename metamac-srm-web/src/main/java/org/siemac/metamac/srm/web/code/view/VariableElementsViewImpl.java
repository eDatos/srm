package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.code.presenter.VariableElementsPresenter;
import org.siemac.metamac.srm.web.code.view.handlers.VariableElementsUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
import org.siemac.metamac.web.common.client.widgets.SearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class VariableElementsViewImpl extends ViewWithUiHandlers<VariableElementsUiHandlers> implements VariableElementsPresenter.VariableElementsView {

    private VLayout            panel;

    private SearchSectionStack searchSectionStack;

    private PaginatedListGrid  variableElementsListGrid;

    @Inject
    public VariableElementsViewImpl() {
        super();

        // Search
        searchSectionStack = new SearchSectionStack();
        searchSectionStack.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveVariableElements(VariableElementsPresenter.ELEMENT_LIST_FIRST_RESULT, VariableElementsPresenter.ELEMENT_LIST_MAX_RESULTS,
                        searchSectionStack.getSearchCriteria());
            }
        });

        // Variable elements

        variableElementsListGrid = new PaginatedListGrid(VariableElementsPresenter.ELEMENT_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableElements(firstResult, maxResults, searchSectionStack.getSearchCriteria());
            }
        });
        variableElementsListGrid.getListGrid().setAutoFitMaxRecords(VariableElementsPresenter.ELEMENT_LIST_MAX_RESULTS);
        variableElementsListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        variableElementsListGrid.getListGrid().setDataSource(new VariableElementDS());
        variableElementsListGrid.getListGrid().setUseAllDataSourceFields(false);

        variableElementsListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String variableCode = ((VariableElementRecord) event.getRecord()).getVariableElementDto().getVariable().getCode();
                String variableElementCode = ((VariableElementRecord) event.getRecord()).getCode();
                getUiHandlers().goToVariableElement(variableCode, variableElementCode);
            }
        });

        ListGridField fieldCode = new ListGridField(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldName = new ListGridField(VariableElementDS.NAME, getConstants().nameableArtefactName());
        ListGridField urn = new ListGridField(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        variableElementsListGrid.getListGrid().setFields(fieldCode, fieldName, urn);

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(variableElementsListGrid);

        panel = new VLayout();
        panel.setHeight100();
        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == VariableElementsPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setVariableElements(GetVariableElementsResult result) {
        setVariableElements(result.getVariableElements());
        variableElementsListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
    }

    private void setVariableElements(List<VariableElementDto> variableElementDtos) {
        VariableElementRecord[] records = new VariableElementRecord[variableElementDtos.size()];
        int index = 0;
        for (VariableElementDto variableElement : variableElementDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.RecordUtils.getVariableElementRecord(variableElement);
        }
        variableElementsListGrid.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.reset();
    }
}