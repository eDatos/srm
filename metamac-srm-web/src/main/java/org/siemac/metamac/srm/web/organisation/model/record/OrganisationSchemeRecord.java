package org.siemac.metamac.srm.web.organisation.model.record;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.VersionableResourceRecord;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;

import static org.siemac.metamac.srm.web.shared.CommonSharedUtils.getPublicationStreamStatusIconUrl;

public class OrganisationSchemeRecord extends VersionableResourceRecord {

    public OrganisationSchemeRecord() {
    }

    public void setType(String value) {
        setAttribute(OrganisationSchemeDS.TYPE, value);
    }

    public void setOrganisationSchemeBasicDto(OrganisationSchemeMetamacBasicDto organisationSchemeMetamacDto) {
        setAttribute(OrganisationSchemeDS.DTO, organisationSchemeMetamacDto);
    }

    public void setStreamMessageStatus(StreamMessageStatusEnum value) {
        setAttribute(OrganisationSchemeDS.PUBLICATION_STREAM_STATUS, getPublicationStreamStatusIconUrl(value));
    }

    public ProcStatusEnum getProcStatus() {
        return ((OrganisationSchemeMetamacBasicDto) getAttributeAsObject(OrganisationSchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public OrganisationSchemeMetamacBasicDto getOrganisationSchemeBasicDto() {
        return (OrganisationSchemeMetamacBasicDto) getAttributeAsObject(OrganisationSchemeDS.DTO);
    }
}
