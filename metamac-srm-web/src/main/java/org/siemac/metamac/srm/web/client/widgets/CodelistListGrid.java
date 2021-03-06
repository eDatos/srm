package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.code.model.record.CodelistRecord;
import org.siemac.metamac.srm.web.code.utils.CodesRecordUtils;

public class CodelistListGrid extends NavigableListGrid {

    public CodelistListGrid() {
        setFields(ResourceFieldUtils.getCodelistListGridFields());
    }

    public void setCodelist(List<CodelistMetamacBasicDto> codelistDtos) {
        removeAllData();
        if (codelistDtos != null) {
            CodelistRecord[] codelistRecords = new CodelistRecord[codelistDtos.size()];
            for (int i = 0; i < codelistDtos.size(); i++) {
                codelistRecords[i] = CodesRecordUtils.getCodelistRecord(codelistDtos.get(i));
            }
            this.setData(codelistRecords);
        }
    }
}
