package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementOperationDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VariableElementOperationRecord extends ListGridRecord {

    public VariableElementOperationRecord(Long id, String code, String type, String source, String target, VariableElementOperationDto variableElementOperationDto) {
        setId(id);
        setCode(code);
        setType(type);
        setTypeEnum(variableElementOperationDto.getOperationType());
        setSource(source);
        setTarget(target);
        // setElement(VariableElementOperationTypeEnum.FUSION.equals(variableElementOperationDto.getOperationType()) ? source : target);
        setVariableElementOperationDto(variableElementOperationDto);
    }

    public void setId(Long id) {
        setAttribute(VariableElementOperationDS.ID, id);
    }

    public void setCode(String code) {
        setAttribute(VariableElementOperationDS.CODE, code);
    }

    public void setType(String type) {
        setAttribute(VariableElementOperationDS.OPERATION_TYPE, type);
    }

    public void setTypeEnum(VariableElementOperationTypeEnum typeEnum) {
        setAttribute(VariableElementOperationDS.OPERATION_TYPE_ENUM, typeEnum);
    }

    public VariableElementOperationTypeEnum getTypeEnum() {
        return (VariableElementOperationTypeEnum) getAttributeAsObject(VariableElementOperationDS.OPERATION_TYPE_ENUM);
    }

    public void setSource(String value) {
        setAttribute(VariableElementOperationDS.OPERATION_SOURCES, value);
    }

    public String getSource() {
        return getAttributeAsString(VariableElementOperationDS.OPERATION_SOURCES);
    }

    public void setTarget(String value) {
        setAttribute(VariableElementOperationDS.OPERATION_TARGETS, value);
    }

    public String getTarget() {
        return getAttributeAsString(VariableElementOperationDS.OPERATION_TARGETS);
    }

    // public void setElement(String value) {
    // setAttribute(VariableElementOperationDS.ELEMENTS, value);
    // }

    public String getCode() {
        return getAttributeAsString(VariableElementOperationDS.CODE);
    }

    public void setVariableElementOperationDto(VariableElementOperationDto variableDto) {
        setAttribute(VariableElementOperationDS.DTO, variableDto);
    }

    public VariableElementOperationDto getVariableElementOperationDto() {
        return (VariableElementOperationDto) getAttributeAsObject(VariableElementOperationDS.DTO);
    }
}
