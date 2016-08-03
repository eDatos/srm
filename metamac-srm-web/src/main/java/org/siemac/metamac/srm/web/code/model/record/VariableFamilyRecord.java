package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableFamilyDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VariableFamilyRecord extends ListGridRecord {

    public VariableFamilyRecord() {
    }

    public VariableFamilyRecord(Long id, String code, String name, String urn, VariableFamilyDto variableFamilyDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setVariableFamilyDto(variableFamilyDto);
    }

    public void setId(Long id) {
        setAttribute(VariableFamilyDS.ID, id);
    }

    public void setCode(String code) {
        setAttribute(VariableFamilyDS.CODE, code);
    }

    public void setName(String name) {
        setAttribute(VariableFamilyDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(VariableFamilyDS.URN, value);
    }

    public String getUrn() {
        return getAttributeAsString(VariableFamilyDS.URN);
    }

    public void setVariableFamilyDto(VariableFamilyDto variableFamilyDto) {
        setAttribute(VariableFamilyDS.DTO, variableFamilyDto);
    }

    public VariableFamilyDto getVariableFamilyDto() {
        return (VariableFamilyDto) getAttributeAsObject(VariableFamilyDS.DTO);
    }

    public void setVariableFamilyBasicDto(VariableFamilyBasicDto variableFamilyDto) {
        setAttribute(VariableFamilyDS.DTO, variableFamilyDto);
    }

    public VariableFamilyBasicDto getVariableFamilyBasicDto() {
        return (VariableFamilyBasicDto) getAttributeAsObject(VariableFamilyDS.DTO);
    }
}
