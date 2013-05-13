package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;

public class CodelistRecord extends ItemSchemeRecord {

    public CodelistRecord() {
    }

    public void setDescription(String desc) {
        setAttribute(CodelistDS.DESCRIPTION, desc);
    }

    public void setIsRecommended(String value) {
        setAttribute(CodelistDS.IS_RECOMMENDED, value);
    }

    public void setVariable(String value) {
        setAttribute(CodelistDS.VARIABLE, value);
    }

    public void setCodelistBasicDto(CodelistMetamacBasicDto codelistMetamacDto) {
        setAttribute(CodelistDS.DTO, codelistMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CodelistMetamacBasicDto) getAttributeAsObject(CodelistDS.DTO)).getLifeCycle().getProcStatus();
    }

    public String getDescription() {
        return getAttribute(CodelistDS.DESCRIPTION);
    }

    public CodelistMetamacBasicDto getCodelistMetamacBasicDto() {
        return (CodelistMetamacBasicDto) getAttributeAsObject(CodelistDS.DTO);
    }
}
