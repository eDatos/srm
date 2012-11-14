package org.siemac.metamac.srm.web.organisation.model.record;

import org.siemac.metamac.srm.web.organisation.model.ds.ContactDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ContactRecord extends ListGridRecord {

    public ContactRecord(Long id, String name, String organisationUnit, ContactDto contactDto) {
        setId(id);
        setName(name);
        setOrganisationUnit(organisationUnit);
        setContactDto(contactDto);
    }

    public void setId(Long id) {
        setAttribute(ContactDS.ID, id);
    }

    public void setName(String value) {
        setAttribute(ContactDS.NAME, value);
    }

    public void setOrganisationUnit(String value) {
        setAttribute(ContactDS.ORGANISATION_UNIT, value);
    }

    public void setContactDto(ContactDto contactDto) {
        setAttribute(ContactDS.DTO, contactDto);
    }

    public Long getId() {
        return getAttributeAsLong(ContactDS.ID);
    }

    public ContactDto getContactDto() {
        return (ContactDto) getAttributeAsObject(ContactDS.DTO);
    }

}
