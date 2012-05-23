package org.siemac.metamac.internal.web.dsd.model.record;

import org.siemac.metamac.domain.srm.dto.DataAttributeDto;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AttributeRecord extends ListGridRecord {

    public static final String IDENTIFIER         = "identifier";
    public static final String ID_LOGIC           = "id_logic";
    public static final String ASSIGNMENT         = "assignment";
    public static final String CONCEPT            = "concept";
    public static final String DATA_ATTRIBUTE_DTO = "data_attribute_dto";

    public AttributeRecord() {
    }

    public AttributeRecord(Long id, String idLogic, String assigment, String conceptDto, DataAttributeDto dataAttributeDto) {
        setIdentifier(id);
        setIdLogic(idLogic);
        setAssignment(assigment);
        setConcept(conceptDto);
        setDataAttributeDto(dataAttributeDto);
    }

    public void setIdentifier(Long value) {
        setAttribute(IDENTIFIER, value);
    }

    public void setIdLogic(String value) {
        setAttribute(ID_LOGIC, value);
    }

    public void setAssignment(String value) {
        setAttribute(ASSIGNMENT, value);
    }

    public void setConcept(String value) {
        setAttribute(CONCEPT, value);
    }

    public void setDataAttributeDto(DataAttributeDto value) {
        setAttribute(DATA_ATTRIBUTE_DTO, value);
    }

    public Long getIdentifier() {
        return getAttributeAsLong(IDENTIFIER);
    }

    public String getIdLogic() {
        return getAttributeAsString(ID_LOGIC);
    }

    public String getAssigment() {
        return getAttributeAsString(ASSIGNMENT);
    }

    public String getConcept() {
        return getAttributeAsString(CONCEPT);
    }

    public DataAttributeDto getDataAttributeDto() {
        return (DataAttributeDto) getAttributeAsObject(DATA_ATTRIBUTE_DTO);
    }

}
