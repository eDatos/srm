package org.siemac.metamac.srm.web.organisation.model.record;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;

public class OrganisationSchemeRecord extends ItemSchemeRecord {

    public OrganisationSchemeRecord(Long id, String code, String name, String status, String type, String versionLogic, String urn, OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        super(id, code, name, status, versionLogic, urn);
        setType(type);
        setOrganisationSchemeDto(organisationSchemeMetamacDto);
    }

    public void setType(String value) {
        setAttribute(OrganisationSchemeDS.TYPE, value);
    }

    public void setOrganisationSchemeDto(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        setAttribute(OrganisationSchemeDS.DTO, organisationSchemeMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((OrganisationSchemeMetamacDto) getAttributeAsObject(OrganisationSchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public OrganisationSchemeMetamacDto getOrganisationSchemeDto() {
        return (OrganisationSchemeMetamacDto) getAttributeAsObject(OrganisationSchemeDS.DTO);
    }

}
