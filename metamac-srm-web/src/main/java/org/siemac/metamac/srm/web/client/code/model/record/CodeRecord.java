package org.siemac.metamac.srm.web.client.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodeDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodeRecord extends ListGridRecord {

    public CodeRecord(Long id, String code, String name, String urn, String description, CodeMetamacDto codeDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setDescription(description);
        setCodeDto(codeDto);
    }

    public void setId(Long id) {
        setAttribute(CodeDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(CodeDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(CodeDS.URN, value);
    }

    public void setDescription(String desc) {
        setAttribute(CodeDS.DESCRIPTION, desc);
    }

    public void setCode(String code) {
        setAttribute(CodeDS.CODE, code);
    }

    public void setCodeDto(CodeMetamacDto codeDto) {
        setAttribute(CodeDS.DTO, codeDto);
    }

    public Long getId() {
        return getAttributeAsLong(CodeDS.ID);
    }

    public String getCode() {
        return getAttribute(CodeDS.CODE);
    }

    public String getName() {
        return getAttribute(CodeDS.NAME);
    }

    public String getUrn() {
        return getAttribute(CodeDS.URN);
    }

    public String getDescription() {
        return getAttribute(CodeDS.DESCRIPTION);
    }

    public CodeMetamacDto getCodeDto() {
        return (CodeMetamacDto) getAttributeAsObject(CodeDS.DTO);
    }
}
