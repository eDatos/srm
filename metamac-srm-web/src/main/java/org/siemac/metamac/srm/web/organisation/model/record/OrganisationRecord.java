package org.siemac.metamac.srm.web.organisation.model.record;

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

    public void setOrganisationSpecialHasBeenPublished(Boolean value) {
        setAttribute(OrganisationDS.SPECIAL_ORGANISATION_HAS_BEEN_PUBLISHED, value);
    }

    public Boolean getSpecialOrganisationHasBeenPublished() {
        return getAttributeAsBoolean(OrganisationDS.SPECIAL_ORGANISATION_HAS_BEEN_PUBLISHED);
    }
}
