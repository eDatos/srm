package org.siemac.metamac.srm.web.organisation.model.record;

import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.OrganisationDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OrganisationRecord extends ListGridRecord {

    public OrganisationRecord(Long id, String code, String name, String urn, OrganisationDto organisationDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setOrganisationDto(organisationDto);
    }

    public void setId(Long id) {
        setAttribute(OrganisationDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(OrganisationDS.NAME, name);
    }

    public void setCode(String code) {
        setAttribute(OrganisationDS.CODE, code);
    }

    public void setUrn(String value) {
        setAttribute(OrganisationDS.URN, value);
    }

    public void setOrganisationDto(OrganisationDto organisationDto) {
        setAttribute(OrganisationDS.DTO, organisationDto);
    }

    public Long getId() {
        return getAttributeAsLong(OrganisationDS.ID);
    }

    public String getCode() {
        return getAttribute(OrganisationDS.CODE);
    }

    public String getName() {
        return getAttribute(OrganisationDS.NAME);
    }

    public String getUrn() {
        return getAttributeAsString(OrganisationDS.URN);
    }

    public OrganisationDto getOrganisationDto() {
        return (OrganisationDto) getAttributeAsObject(OrganisationDS.DTO);
    }

}
