package org.siemac.metamac.srm.web.client.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodelistFamilyDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodelistFamilyRecord extends ListGridRecord {

    public CodelistFamilyRecord(Long id, String code, String name, String urn, CodelistFamilyDto codelistFamilyDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setCodelistFamilyDto(codelistFamilyDto);
    }

    public void setId(Long id) {
        setAttribute(CodelistFamilyDS.ID, id);
    }

    public void setCode(String code) {
        setAttribute(CodelistFamilyDS.CODE, code);
    }

    public void setName(String name) {
        setAttribute(CodelistFamilyDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(CodelistFamilyDS.URN, value);
    }

    public String getUrn() {
        return getAttributeAsString(CodelistFamilyDS.URN);
    }

    public void setCodelistFamilyDto(CodelistFamilyDto codelistFamilyDto) {
        setAttribute(CodelistFamilyDS.DTO, codelistFamilyDto);
    }

    public CodelistFamilyDto getCodelistFamilyDto() {
        return (CodelistFamilyDto) getAttributeAsObject(CodelistFamilyDS.DTO);
    }
}
