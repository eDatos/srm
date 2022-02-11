package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;

import static org.siemac.metamac.srm.web.shared.CommonSharedUtils.getPublicationStreamStatusIconUrl;

public class DsdRecord extends VersionableResourceRecord {

    public DsdRecord() {
    }

    public void setStatisticalOperation(ExternalItemDto value) {
        setExternalItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, value);
    }

    public void setDsdBasicDto(DataStructureDefinitionMetamacBasicDto value) {
        setAttribute(DataStructureDefinitionDS.DTO, value);
    }

    public void setStreamMessageStatus(StreamMessageStatusEnum value) {
        setAttribute(DataStructureDefinitionDS.PUBLICATION_STREAM_STATUS, getPublicationStreamStatusIconUrl(value));
    }

    public DataStructureDefinitionMetamacBasicDto getDsdBasicDto() {
        return (DataStructureDefinitionMetamacBasicDto) getAttributeAsObject(DataStructureDefinitionDS.DTO);
    }
}
