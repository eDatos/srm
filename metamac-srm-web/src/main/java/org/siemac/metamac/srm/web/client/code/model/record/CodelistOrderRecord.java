package org.siemac.metamac.srm.web.client.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodelistOrderVisualisationDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodelistOrderDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodelistOrderRecord extends ListGridRecord {

    public CodelistOrderRecord(String code, String name, String urn, CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        setCode(code);
        setName(name);
        setUrn(urn);
        setCodelistOrderVisualisationDto(codelistOrderVisualisationDto);
    }

    public void setCode(String code) {
        setAttribute(CodelistOrderDS.CODE, code);
    }

    public String getCode() {
        return getAttributeAsString(CodelistOrderDS.CODE);
    }

    public void setName(String name) {
        setAttribute(CodelistOrderDS.NAME, name);
    }

    public String getName() {
        return getAttributeAsString(CodelistOrderDS.NAME);
    }

    public void setUrn(String urn) {
        setAttribute(CodelistOrderDS.URN, urn);
    }

    public String getUrn() {
        return getAttributeAsString(CodelistOrderDS.URN);
    }

    public void setCodelistOrderVisualisationDto(CodelistOrderVisualisationDto codelistOrderVisualisationDto) {
        setAttribute(CodelistOrderDS.DTO, codelistOrderVisualisationDto);
    }

    public CodelistOrderVisualisationDto getCodelistOrderVisualisationDto() {
        return (CodelistOrderVisualisationDto) getAttributeAsObject(CodelistOrderDS.DTO);
    }
}
