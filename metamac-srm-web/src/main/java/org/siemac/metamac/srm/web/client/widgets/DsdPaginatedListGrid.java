package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DsdPaginatedListGrid extends PaginatedCheckListGrid {

    public DsdPaginatedListGrid(int maxResults, PaginatedAction action) {
        super(maxResults, action);

        getListGrid().setAutoFitMaxRecords(DsdListPresenter.DSD_LIST_MAX_RESULTS);
        getListGrid().setAutoFitData(Autofit.VERTICAL);
        getListGrid().setShowAllRecords(true);

        ListGridField codeDsdField = new ListGridField(DataStructureDefinitionDS.CODE, MetamacSrmWeb.getConstants().dsdCode());
        ListGridField nameDsdField = new ListGridField(DataStructureDefinitionDS.NAME, MetamacSrmWeb.getConstants().dsdName());
        ListGridField procStatusField = new ListGridField(DataStructureDefinitionDS.PROC_STATUS, MetamacSrmWeb.getConstants().dsdProcStatus());

        // ToolTip
        codeDsdField.setShowHover(true);
        codeDsdField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DsdRecord dsdRecord = (DsdRecord) record;
                return dsdRecord.getCode();
            }
        });
        nameDsdField.setShowHover(true);
        nameDsdField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DsdRecord dsdRecord = (DsdRecord) record;
                return dsdRecord.getName();
            }
        });
        getListGrid().setFields(codeDsdField, nameDsdField, procStatusField);
    }

    public void setDsds(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos, int firstResult, int totalResults) {
        DsdRecord[] dsdRecords = new DsdRecord[dataStructureDefinitionMetamacDtos.size()];
        for (int i = 0; i < dataStructureDefinitionMetamacDtos.size(); i++) {
            dsdRecords[i] = RecordUtils.getDsdRecord(dataStructureDefinitionMetamacDtos.get(i));
        }
        // Populate the List Grid
        getListGrid().setData(dsdRecords);
        refreshPaginationInfo(firstResult, dataStructureDefinitionMetamacDtos.size(), totalResults);
    }

}
