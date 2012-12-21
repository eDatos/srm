package org.siemac.metamac.srm.web.client.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.client.code.utils.RecordUtils;
import org.siemac.metamac.srm.web.client.widgets.VersionsSectionStack;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;

public class CodelistVersionsSectionStack extends VersionsSectionStack {

    public CodelistVersionsSectionStack(String title) {
        super(title);
    }

    public void setCodelists(List<CodelistMetamacDto> codelistDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (CodelistMetamacDto codelistDto : codelistDtos) {
            listGrid.addData(RecordUtils.getCodelistRecord(codelistDto));
        }
    }

    public void selectCodelist(CodelistMetamacDto codelistMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(CodelistDS.URN, codelistMetamacDto.getUrn());
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }
}
