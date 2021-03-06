package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.widgets.ItemSchemeVersionsSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.utils.CodesRecordUtils;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.SortDirection;

public class CodelistVersionsSectionStack extends ItemSchemeVersionsSectionStack {

    public CodelistVersionsSectionStack(String title) {
        super(title);
    }

    public void setCodelists(List<CodelistMetamacBasicDto> codelistDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (CodelistMetamacBasicDto codelistDto : codelistDtos) {
            listGrid.addData(CodesRecordUtils.getCodelistRecord(codelistDto));
        }
        listGrid.sort(CodelistDS.VERSION_LOGIC, SortDirection.DESCENDING);
    }

    public void selectCodelist(CodelistMetamacDto codelistMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(CodelistDS.URN, codelistMetamacDto.getUrn());
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }
}
