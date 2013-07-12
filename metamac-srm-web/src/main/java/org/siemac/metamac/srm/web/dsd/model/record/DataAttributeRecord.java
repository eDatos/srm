package org.siemac.metamac.srm.web.dsd.model.record;

import org.siemac.metamac.srm.web.dsd.model.ds.DataAttributeDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DataAttributeRecord extends ListGridRecord {

    public DataAttributeRecord() {
    }

    public void setId(Long value) {
        setAttribute(DataAttributeDS.ID, value);
    }

    public void setCode(String value) {
        setAttribute(DataAttributeDS.CODE, value);
    }

    public void setUsageStatus(String value) {
        setAttribute(DataAttributeDS.USAGE_STATUS, value);
    }

    public void setConcept(String value) {
        setAttribute(DataAttributeDS.CONCEPT, value);
    }

    public void setDataAttributeDto(DataAttributeDto value) {
        setAttribute(DataAttributeDS.DTO, value);
    }

    public Long getIdentifier() {
        return getAttributeAsLong(DataAttributeDS.ID);
    }

    public String getCode() {
        return getAttributeAsString(DataAttributeDS.CODE);
    }

    public String getUsageStatus() {
        return getAttributeAsString(DataAttributeDS.USAGE_STATUS);
    }

    public String getConcept() {
        return getAttributeAsString(DataAttributeDS.CONCEPT);
    }

    public DataAttributeDto getDataAttributeDto() {
        return (DataAttributeDto) getAttributeAsObject(DataAttributeDS.DTO);
    }

    public void setSpecialAttributeType(String value) {
        setAttribute(DataAttributeDS.SPECIAL_ATTRIBUTE_TYPE, value);
    }
}
