package org.siemac.metamac.srm.web.code.view;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.utils.ResourceListFieldUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.model.record.CodeRecord;
import org.siemac.metamac.srm.web.code.presenter.CodesPresenter;
import org.siemac.metamac.srm.web.code.view.handlers.CodesUiHandlers;
import org.siemac.metamac.srm.web.code.widgets.CodeSearchSectionStack;
import org.siemac.metamac.srm.web.shared.code.GetCodesResult;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CodesViewImpl extends ViewWithUiHandlers<CodesUiHandlers> implements CodesPresenter.CodesView {

    private VLayout                panel;

    private CodeSearchSectionStack searchSectionStack;

    private PaginatedListGrid      codesListGrid;

    @Inject
    public CodesViewImpl() {
        super();

        // Search
        searchSectionStack = new CodeSearchSectionStack();

        // Codes

        codesListGrid = new PaginatedListGrid(CodesPresenter.ITEM_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCodes(firstResult, maxResults, searchSectionStack.getCodeWebCriteria());
            }
        });
        codesListGrid.getListGrid().setAutoFitMaxRecords(CodesPresenter.ITEM_LIST_MAX_RESULTS);
        codesListGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        codesListGrid.getListGrid().setDataSource(new CodeDS());
        codesListGrid.getListGrid().setUseAllDataSourceFields(false);

        codesListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                String codelistUrn = ((CodeRecord) event.getRecord()).getItemSchemeUrn();
                String codeUrn = ((CodeRecord) event.getRecord()).getUrn();
                getUiHandlers().goToCode(codelistUrn, codeUrn);
            }
        });

        codesListGrid.getListGrid().setFields(ResourceListFieldUtils.getCodeFields());

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(searchSectionStack);
        subPanel.addMember(codesListGrid);

        panel = new VLayout();
        panel.setHeight100();
        panel.addMember(subPanel);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(CodesUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CodesPresenter.TYPE_SetContextAreaContentCodesToolBar) {
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
    public void setCodes(GetCodesResult result) {
        setCodes(result.getCodes());
        codesListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getCodes().size(), result.getTotalResults());
    }

    private void setCodes(List<CodeMetamacBasicDto> codeDtos) {
        CodeRecord[] records = new CodeRecord[codeDtos.size()];
        int index = 0;
        for (CodeMetamacBasicDto scheme : codeDtos) {
            records[index++] = org.siemac.metamac.srm.web.code.utils.RecordUtils.getCodeRecord(scheme);
        }
        codesListGrid.getListGrid().setData(records);
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }
}
