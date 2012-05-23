package org.siemac.metamac.srm.web.dsd.model.record;

import java.util.List;

import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GroupKeysRecord extends ListGridRecord {

    public static final String IDENTIFIER     = "identifier";
    public static final String ID_LOGIC       = "id_logic";
    public static final String DIMENSIONS     = "dimensions";
    public static final String DESCRIPTOR_DTO = "descriptor_dto";

    public GroupKeysRecord() {
    }

    public GroupKeysRecord(Long id, String idLogic, List<ComponentDto> dimensions, DescriptorDto descriptorDto) {
        setIdentifier(id);
        setIdLogic(idLogic);
        setDimensions(dimensions);
        setDescriptorDto(descriptorDto);
    }

    public void setIdentifier(Long value) {
        setAttribute(IDENTIFIER, value);
    }

    public void setIdLogic(String value) {
        setAttribute(ID_LOGIC, value);
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

    public String getIdLogic() {
        return getAttributeAsString(ID_LOGIC);
    }

    public DescriptorDto getDescriptorDto() {
        return (DescriptorDto) getAttributeAsObject(DESCRIPTOR_DTO);
    }

}
