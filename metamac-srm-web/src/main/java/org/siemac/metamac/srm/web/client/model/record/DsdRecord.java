package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;

public class DsdRecord extends VersionableResourceRecord {

    public DsdRecord() {
    }

    public void setStatisticalOperation(String value) {
        setAttribute(DataStructureDefinitionDS.STATISTICAL_OPERATION, value);
    }

    public void setDsdBasicDto(DataStructureDefinitionMetamacBasicDto value) {
        setAttribute(DataStructureDefinitionDS.DTO, value);
    }

    public DataStructureDefinitionMetamacBasicDto getDsdBasicDto() {
        return (DataStructureDefinitionMetamacBasicDto) getAttributeAsObject(DataStructureDefinitionDS.DTO);
    }
}
