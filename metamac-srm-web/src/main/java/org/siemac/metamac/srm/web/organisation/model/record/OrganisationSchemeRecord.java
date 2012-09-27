package org.siemac.metamac.srm.web.organisation.model.record;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OrganisationSchemeRecord extends ListGridRecord {

    public OrganisationSchemeRecord(Long id, String code, String name, String status, String type, String versionLogic, String urn, OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        setId(id);
        setCode(code);
        setName(name);
        setProcStatus(status);
        setType(type);
        setVersionLogic(versionLogic);
        setUrn(urn);
        setOrganisationSchemeDto(organisationSchemeMetamacDto);
    }

    public void setId(Long id) {
        setAttribute(OrganisationSchemeDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(OrganisationSchemeDS.NAME, name);
    }

    public void setCode(String code) {
        setAttribute(OrganisationSchemeDS.CODE, code);
    }

    public void setProcStatus(String value) {
        setAttribute(OrganisationSchemeDS.PROC_STATUS, value);
    }

    public void setType(String value) {
        setAttribute(OrganisationSchemeDS.TYPE, value);
    }

    public void setVersionLogic(String value) {
        setAttribute(OrganisationSchemeDS.VERSION_LOGIC, value);
    }

    public void setUrn(String value) {
        setAttribute(OrganisationSchemeDS.URN, value);
    }

    public void setOrganisationSchemeDto(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        setAttribute(OrganisationSchemeDS.DTO, organisationSchemeMetamacDto);
    }

    public Long getId() {
        return getAttributeAsLong(OrganisationSchemeDS.ID);
    }

    public String getCode() {
        return getAttribute(OrganisationSchemeDS.CODE);
    }

    public String getName() {
        return getAttribute(OrganisationSchemeDS.NAME);
    }

    public ProcStatusEnum getProcStatus() {
        return ((OrganisationSchemeMetamacDto) getAttributeAsObject(OrganisationSchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public String getUrn() {
        return getAttributeAsString(OrganisationSchemeDS.URN);
    }

    public OrganisationSchemeMetamacDto getOrganisationSchemeDto() {
        return (OrganisationSchemeMetamacDto) getAttributeAsObject(OrganisationSchemeDS.DTO);
    }
}
