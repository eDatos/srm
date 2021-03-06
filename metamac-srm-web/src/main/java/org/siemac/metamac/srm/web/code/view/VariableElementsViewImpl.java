package org.siemac.metamac.srm.web.code.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.model.record.VariableElementRecord;
import org.siemac.metamac.srm.web.code.presenter.VariableElementsPresenter;
import org.siemac.metamac.srm.web.code.view.handlers.VariableElementsUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.VariableElementSearchSectionStack;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class VariableElementsViewImpl extends ViewWithUiHandlers<VariableElementsUiHandlers> implements VariableElementsPresenter.VariableElementsView {

    private final VLayout                           panel;

    private final VariableElementSearchSectionStack searchSectionStack;

    private final PaginatedListGrid                 variableElementsListGrid;

    @Inject
    public VariableElementsViewImpl() {
        super();

        // Search
        searchSectionStack = new VariableElementSearchSectionStack();

        // Variable elements

        variableElementsListGrid = new PaginatedListGrid(VariableElementsPresenter.ELEMENT_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveVariableElements(firstResult, maxResults, searchSectionStack.getVariableElementWebCriteria());
            }
        });
        variableElementsListGrid.getListGrid().setDataSource(new VariableElementDS());
        variableElementsListGrid.getListGrid().setUseAllDataSourceFields(false);

        variableElementsListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String variableCode = ((VariableElementRecord) event.getRecord()).getVariableElementBasicDto().getVariable().getCode();
                String variableElementCode = ((VariableElementRecord) event.getRecord()).getCode();
                getUiHandlers().goToVariableElement(variableCode, variableElementCode);
            }
        });

        ListGridField fieldCode = new ListGridField(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldShortName = new ListGridField(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        CustomListGridField hasShape = new CustomListGridField(VariableElementDS.SHAPE_WKT, getConstants().variableElementHasPolygonShape());
        ListGridField urn = new ListGridField(VariableElementDS.URN, getConstants().identifiableArtefactUrn());
        variableElementsListGrid.getListGrid().setFields(fieldCode, fieldShortName, hasShape, urn);

        variableElementsListGrid.setHeight100();

        panel = new VLayout();
        panel.addMember(searchSectionStack);
        panel.addMember(variableElementsListGrid);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(VariableElementsUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
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

    private void setVariableElements(List<VariableElementBasicDto> variableElementDtos) {
        VariableElementRecord[] records = new VariableElementRecord[variableElementDtos.size()];
        int index = 0;
        for (VariableElementBasicDto variableElement : variableElementDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.CodesRecordUtils.getVariableElementRecord(variableElement);
        }
        variableElementsListGrid.getListGrid().setData(records);
    }

    @Override
    public void setCodesForVariableElementGeographicalGranularity(GetRelatedResourcesResult result) {
        searchSectionStack.setGeographicalGranularityCodes(result);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }
}
