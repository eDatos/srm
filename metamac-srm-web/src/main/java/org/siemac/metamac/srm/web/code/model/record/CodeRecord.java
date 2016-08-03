package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.ItemRecord;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

public class CodeRecord extends ItemRecord {

    public CodeRecord() {
    }

    public void setCodeBasicDto(CodeMetamacBasicDto codeDto) {
        setAttribute(CodeDS.DTO, codeDto);
    }

    public CodeMetamacBasicDto getCodeBasicDto() {
        return (CodeMetamacBasicDto) getAttributeAsObject(CodeDS.DTO);
    }
}
