package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VariableElementRecord extends ListGridRecord {

    public VariableElementRecord() {
    }

    public VariableElementRecord(Long id, String code, String shortName, String urn, String hasShape, VariableElementDto variableDto) {
        setId(id);
        setCode(code);
        setShortName(shortName);
        setUrn(urn);
        setHasShape(hasShape);
        setVariableElementDto(variableDto);
    }

    public void setId(Long id) {
        setAttribute(VariableElementDS.ID, id);
    }

    public void setCode(String code) {
        setAttribute(VariableElementDS.CODE, code);
    }

    public void setShortName(String value) {
        setAttribute(VariableElementDS.SHORT_NAME, value);
    }

    public void setUrn(String value) {
        setAttribute(VariableElementDS.URN, value);
    }

    public void setHasShape(String value) {
        setAttribute(VariableElementDS.SHAPE_WKT, value);
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

    public void setVariableElementBasicDto(VariableElementBasicDto variableElementDto) {
        setAttribute(VariableElementDS.DTO, variableElementDto);
    }

    public VariableElementDto getVariableElementDto() {
        return (VariableElementDto) getAttributeAsObject(VariableElementDS.DTO);
    }

    public VariableElementBasicDto getVariableElementBasicDto() {
        return (VariableElementBasicDto) getAttributeAsObject(VariableElementDS.DTO);
    }
}
