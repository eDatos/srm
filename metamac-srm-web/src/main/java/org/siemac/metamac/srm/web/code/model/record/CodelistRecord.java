package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;

public class CodelistRecord extends ItemSchemeRecord {

    public CodelistRecord(Long id, String code, String name, String description, String status, String versionLogic, String urn, CodelistMetamacDto codelistMetamacDto) {
        super(id, code, name, status, versionLogic, urn);
        setDescription(description);
        setCodelistDto(codelistMetamacDto);
    }

    public void setDescription(String desc) {
        setAttribute(CodelistDS.DESCRIPTION, desc);
    }

    public void setCodelistDto(CodelistMetamacDto codelistMetamacDto) {
        setAttribute(CodelistDS.DTO, codelistMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CodelistMetamacDto) getAttributeAsObject(CodelistDS.DTO)).getLifeCycle().getProcStatus();
    }

    public String getDescription() {
        return getAttribute(CodelistDS.DESCRIPTION);
    }

    public CodelistMetamacDto getCodelistMetamacDto() {
        return (CodelistMetamacDto) getAttributeAsObject(CodelistDS.DTO);
    }
}
