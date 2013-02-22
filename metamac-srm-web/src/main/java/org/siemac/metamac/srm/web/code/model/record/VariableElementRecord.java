package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VariableElementRecord extends ListGridRecord {

    public VariableElementRecord(Long id, String code, String name, String urn, VariableElementDto variableDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setVariableElementDto(variableDto);
    }

    public void setId(Long id) {
        setAttribute(VariableElementDS.ID, id);
    }

    public void setCode(String code) {
        setAttribute(VariableElementDS.CODE, code);
    }

    public void setName(String name) {
        setAttribute(VariableElementDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(VariableElementDS.URN, value);
    }

    public String getCode() {
        return getAttributeAsString(VariableElementDS.CODE);
    }

    public String getUrn() {
        return getAttributeAsString(VariableElementDS.URN);
    }

    public void setVariableElementDto(VariableElementDto variableElementDto) {
        setAttribute(VariableElementDS.DTO, variableElementDto);
    }

    public VariableElementDto getVariableElementDto() {
        return (VariableElementDto) getAttributeAsObject(VariableElementDS.DTO);
    }
}
