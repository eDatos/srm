package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DsdPaginatedListGrid extends PaginatedCheckListGrid {

    public DsdPaginatedListGrid(int maxResults, PaginatedAction action) {
        super(maxResults, action);

        getListGrid().setAutoFitMaxRecords(DsdListPresenter.DSD_LIST_MAX_RESULTS);
        getListGrid().setAutoFitData(Autofit.VERTICAL);
        getListGrid().setShowAllRecords(true);

        ListGridField codeDsdField = new ListGridField(DsdRecord.CODE, MetamacSrmWeb.getConstants().dsdCode());
        ListGridField nameDsdField = new ListGridField(DsdRecord.NAME, MetamacSrmWeb.getConstants().dsdName());
        ListGridField descriptionDsdField = new ListGridField(DsdRecord.DESCRIPTION, MetamacSrmWeb.getConstants().dsdDescription());
        ListGridField finalStructureDsdField = new ListGridField(DsdRecord.FINAL_STRUCTURE, MetamacSrmWeb.getConstants().dsdFinalStructure());
        finalStructureDsdField.setType(ListGridFieldType.BOOLEAN);

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
        descriptionDsdField.setShowHover(true);
        descriptionDsdField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                DsdRecord dsdRecord = (DsdRecord) record;
                return dsdRecord.getDescription();
            }
        });
        getListGrid().setFields(codeDsdField, nameDsdField, descriptionDsdField, finalStructureDsdField);
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
