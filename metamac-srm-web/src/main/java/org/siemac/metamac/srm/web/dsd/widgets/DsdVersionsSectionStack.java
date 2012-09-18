package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.VersionsSectionStack;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.grid.ListGridField;

public class DsdVersionsSectionStack extends VersionsSectionStack {

    public DsdVersionsSectionStack(String title) {
        super(title);

        ListGridField codeField = new ListGridField(DataStructureDefinitionDS.CODE, getConstants().dsdCode());
        codeField.setWidth("30%");

        ListGridField nameField = new ListGridField(DataStructureDefinitionDS.NAME, getConstants().dsdName());

        ListGridField versionField = new ListGridField(DataStructureDefinitionDS.VERSION_LOGIC, getConstants().dsdVersion());
        versionField.setWidth("15%");

        listGrid.setFields(codeField, nameField, versionField);
    }

    public void setDataStructureDefinitions(List<DataStructureDefinitionMetamacDto> dataStructureDefinitionMetamacDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (DataStructureDefinitionMetamacDto dsd : dataStructureDefinitionMetamacDtos) {
            listGrid.addData(RecordUtils.getDsdRecord(dsd));
        }
    }

    public void selectDataStructureDefinition(DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(DataStructureDefinitionDS.URN, dataStructureDefinitionMetamacDto.getUrn());
        listGrid.selectRecord(record);
    }

}
