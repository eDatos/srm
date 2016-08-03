package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VariableRecord extends ListGridRecord {

    public VariableRecord() {
    }

    public VariableRecord(Long id, String code, String name, String urn, VariableDto variableDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setVariableDto(variableDto);
    }

    public void setId(Long id) {
        setAttribute(VariableDS.ID, id);
    }

    public void setCode(String code) {
        setAttribute(VariableDS.CODE, code);
    }

    public void setName(String name) {
        setAttribute(VariableDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(VariableDS.URN, value);
    }

    public String getUrn() {
        return getAttributeAsString(VariableDS.URN);
    }

    public void setVariableDto(VariableDto variableDto) {
        setAttribute(VariableDS.DTO, variableDto);
    }

    public void setVariableBasicDto(VariableBasicDto variableDto) {
        setAttribute(VariableDS.DTO, variableDto);
    }

    public VariableDto getVariableDto() {
        return (VariableDto) getAttributeAsObject(VariableDS.DTO);
    }

    public VariableBasicDto getVariableBasicDto() {
        return (VariableBasicDto) getAttributeAsObject(VariableDS.DTO);
    }
}
