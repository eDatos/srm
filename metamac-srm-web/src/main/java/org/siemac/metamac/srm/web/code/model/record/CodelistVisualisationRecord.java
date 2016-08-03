package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.code.model.ds.CodelistVisualisationDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CodelistVisualisationRecord extends ListGridRecord {

    public CodelistVisualisationRecord(String code, String name, String urn, CodelistVisualisationDto codelistVisualisationDto) {
        setCode(code);
        setName(name);
        setUrn(urn);
        setCodelistVisualisationDto(codelistVisualisationDto);
    }

    public void setCode(String code) {
        setAttribute(CodelistVisualisationDS.CODE, code);
    }

    public String getCode() {
        return getAttributeAsString(CodelistVisualisationDS.CODE);
    }

    public void setName(String name) {
        setAttribute(CodelistVisualisationDS.NAME, name);
    }

    public String getName() {
        return getAttributeAsString(CodelistVisualisationDS.NAME);
    }

    public void setUrn(String urn) {
        setAttribute(CodelistVisualisationDS.URN, urn);
    }

    public String getUrn() {
        return getAttributeAsString(CodelistVisualisationDS.URN);
    }

    public void setCodelistVisualisationDto(CodelistVisualisationDto codelistVisualisationDto) {
        setAttribute(CodelistVisualisationDS.DTO, codelistVisualisationDto);
    }

    public CodelistVisualisationDto getCodelistVisualisationDto() {
        return (CodelistVisualisationDto) getAttributeAsObject(CodelistVisualisationDS.DTO);
    }
}
