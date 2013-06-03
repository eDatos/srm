package org.siemac.metamac.srm.web.organisation.model.record;

import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.ItemRecord;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationRecord extends ItemRecord {

    public OrganisationRecord() {
    }

    public void setOrganisationTypeName(String value) {
        setAttribute(OrganisationDS.TYPE_NAME, value);
    }

    public void setOrganisationType(OrganisationTypeEnum organisationTypeEnum) {
        setAttribute(OrganisationDS.TYPE, organisationTypeEnum);
    }

    public OrganisationTypeEnum getOrganisationType() {
        return (OrganisationTypeEnum) getAttributeAsObject(OrganisationDS.TYPE);
    }

    public void setOrganisationBasicDto(OrganisationMetamacBasicDto organisationDto) {
        setAttribute(OrganisationDS.DTO, organisationDto);
    }

    public OrganisationMetamacBasicDto getOrganisationBasicDto() {
        return (OrganisationMetamacBasicDto) getAttributeAsObject(OrganisationDS.DTO);
    }
}
