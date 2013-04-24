package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DsdListGrid extends BaseCustomListGrid {

    public DsdListGrid() {
        super();

        this.setShowAllRecords(true);

        ListGridField codeDsdField = new ListGridField(DataStructureDefinitionDS.CODE, MetamacSrmWeb.getConstants().identifiableArtefactCode());
        ListGridField nameDsdField = new ListGridField(DataStructureDefinitionDS.NAME, MetamacSrmWeb.getConstants().nameableArtefactName());
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
        setFields(codeDsdField, nameDsdField, procStatusField);
    }

    public void setDsds(List<DataStructureDefinitionMetamacBasicDto> dataStructureDefinitionMetamacDtos) {
        removeAllData();
        if (dataStructureDefinitionMetamacDtos != null) {
            DsdRecord[] dsdRecords = new DsdRecord[dataStructureDefinitionMetamacDtos.size()];
            for (int i = 0; i < dataStructureDefinitionMetamacDtos.size(); i++) {
                dsdRecords[i] = RecordUtils.getDsdRecord(dataStructureDefinitionMetamacDtos.get(i));
            }
            setData(dsdRecords);
        }
    }
}
