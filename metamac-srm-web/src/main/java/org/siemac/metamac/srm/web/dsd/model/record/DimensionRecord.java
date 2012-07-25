package org.siemac.metamac.srm.web.dsd.model.record;

import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DimensionRecord extends ListGridRecord {

    public static final String IDENTIFIER              = "identifier";
    public static final String CODE                    = "dim-code";
    public static final String CONCEPT                 = "concept";
    public static final String ROLE                    = "role";
    public static final String TYPE                    = "type";
    public static final String DIMENSION_COMPONENT_DTO = "dimension_component_dto";

    public DimensionRecord() {
    }

    public DimensionRecord(Long id, String idLogic, String concept, String type, DimensionComponentDto dimensionComponentDto) {
        setIdentifier(id);
        setCode(idLogic);
        setConcept(concept);
        setType(type);
        setDimensionComponentDto(dimensionComponentDto);
    }

    public void setIdentifier(Long value) {
        setAttribute(IDENTIFIER, value);
    }

    public void setCode(String value) {
        setAttribute(CODE, value);
    }

    public void setConcept(String attribute) {
        setAttribute(CONCEPT, attribute);
    }

    public void setType(String attribute) {
        setAttribute(TYPE, attribute);
    }

    public void setDimensionComponentDto(DimensionComponentDto value) {
        setAttribute(DIMENSION_COMPONENT_DTO, value);
    }

    public Long getIdentifier() {
        return getAttributeAsLong(IDENTIFIER);
    }

    public String getCode() {
        return getAttributeAsString(CODE);
    }

    public String getConcept() {
        return getAttributeAsString(CONCEPT);
    }

    public String getType() {
        return getAttributeAsString(TYPE);
    }

    public DimensionComponentDto getDimensionComponentDto() {
        return (DimensionComponentDto) getAttributeAsObject(DIMENSION_COMPONENT_DTO);
    }

}
