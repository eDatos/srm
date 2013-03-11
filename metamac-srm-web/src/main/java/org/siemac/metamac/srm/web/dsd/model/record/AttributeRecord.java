package org.siemac.metamac.srm.web.dsd.model.record;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AttributeRecord extends ListGridRecord {

    public static final String IDENTIFIER         = "identifier";
    public static final String CODE               = "attr-code";
    public static final String USAGE_STATUS       = "usage-status";
    public static final String CONCEPT            = "concept";
    public static final String DATA_ATTRIBUTE_DTO = "data_attribute_dto";

    public AttributeRecord() {
    }

    public AttributeRecord(Long id, String code, String usageStatus, String conceptDto, DataAttributeDto dataAttributeDto) {
        setIdentifier(id);
        setCode(code);
        setUsageStatus(usageStatus);
        setConcept(conceptDto);
        setDataAttributeDto(dataAttributeDto);
    }

    public void setIdentifier(Long value) {
        setAttribute(IDENTIFIER, value);
    }

    public void setCode(String value) {
        setAttribute(CODE, value);
    }

    public void setUsageStatus(String value) {
        setAttribute(USAGE_STATUS, value);
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

    public String getCode() {
        return getAttributeAsString(CODE);
    }

    public String getUsageStatus() {
        return getAttributeAsString(USAGE_STATUS);
    }

    public String getConcept() {
        return getAttributeAsString(CONCEPT);
    }

    public DataAttributeDto getDataAttributeDto() {
        return (DataAttributeDto) getAttributeAsObject(DATA_ATTRIBUTE_DTO);
    }
}
