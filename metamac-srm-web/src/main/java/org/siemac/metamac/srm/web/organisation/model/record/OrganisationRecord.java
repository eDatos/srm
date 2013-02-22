package org.siemac.metamac.srm.web.organisation.model.record;

import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OrganisationRecord extends ListGridRecord {

    public OrganisationRecord(Long id, String code, String name, String urn, String organisationSchemeUrn, OrganisationTypeEnum organisationTypeEnum) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setOrganisationSchemeUrn(organisationSchemeUrn);
        setOrganisationType(organisationTypeEnum);
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

    public void setOrganisationSchemeUrn(String value) {
        setAttribute(OrganisationDS.ITEM_SCHEME_URN, value);
    }

    public void setOrganisationType(OrganisationTypeEnum organisationTypeEnum) {
        setAttribute(OrganisationDS.TYPE, organisationTypeEnum);
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

    public String getOrganisationSchemeUrn() {
        return getAttributeAsString(OrganisationDS.ITEM_SCHEME_URN);
    }

    public OrganisationTypeEnum getOrganisationType() {
        return (OrganisationTypeEnum) getAttributeAsObject(OrganisationDS.TYPE);
    }
}
