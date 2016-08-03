package org.siemac.metamac.srm.web.dsd.model.record;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GroupKeysRecord extends ListGridRecord {

    public static final String IDENTIFIER     = "identifier";
    public static final String CODE           = "gr-code";
    public static final String DIMENSIONS     = "dimensions";
    public static final String DESCRIPTOR_DTO = "descriptor_dto";

    public GroupKeysRecord() {
    }

    public GroupKeysRecord(Long id, String idLogic, List<ComponentDto> dimensions, DescriptorDto descriptorDto) {
        setIdentifier(id);
        setCode(idLogic);
        setDimensions(dimensions);
        setDescriptorDto(descriptorDto);
    }

    public void setIdentifier(Long value) {
        setAttribute(IDENTIFIER, value);
    }

    public void setCode(String value) {
        setAttribute(CODE, value);
    }

    public void setDimensions(List<ComponentDto> value) {
        setAttribute(DIMENSIONS, value);
    }

    public void setDescriptorDto(DescriptorDto value) {
        setAttribute(DESCRIPTOR_DTO, value);
    }

    public Long getIdentifier() {
        return getAttributeAsLong(IDENTIFIER);
    }

    public String getCode() {
        return getAttributeAsString(CODE);
    }

    public DescriptorDto getDescriptorDto() {
        return (DescriptorDto) getAttributeAsObject(DESCRIPTOR_DTO);
    }

}
