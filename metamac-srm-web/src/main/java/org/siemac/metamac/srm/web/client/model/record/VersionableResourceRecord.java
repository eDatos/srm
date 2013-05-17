package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.srm.web.client.model.ds.ItemSchemeDS;
import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.web.common.client.widgets.NavigableListGridRecord;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class VersionableResourceRecord extends NavigableListGridRecord {

    public VersionableResourceRecord() {
    }

    public void setId(Long id) {
        setAttribute(ItemSchemeDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(ItemSchemeDS.NAME, name);
    }

    public void setCode(String code) {
        setAttribute(ItemSchemeDS.CODE, code);
    }

    public void setProcStatus(String value) {
        setAttribute(ItemSchemeDS.PROC_STATUS, value);
    }

    public void setVersionLogic(String value) {
        setAttribute(ItemSchemeDS.VERSION_LOGIC, value);
    }

    public void setUrn(String value) {
        setAttribute(ItemSchemeDS.URN, value);
    }

    public Long getId() {
        return getAttributeAsLong(ItemSchemeDS.ID);
    }

    public String getCode() {
        return getAttribute(ItemSchemeDS.CODE);
    }

    public String getName() {
        return getAttribute(ItemSchemeDS.NAME);
    }

    public String getUrn() {
        return getAttributeAsString(ItemSchemeDS.URN);
    }

    public void setMaintainer(RelatedResourceDto value) {
        setRelatedResource(ItemSchemeDS.MAINTAINER, value);
    }

    public void setInternalPublicationDate(String value) {
        setAttribute(ItemSchemeDS.INTERNAL_PUBLICATION_DATE, value);
    }

    public void setInternalPublicationUser(String value) {
        setAttribute(ItemSchemeDS.INTERNAL_PUBLICATION_USER, value);
    }

    public void setExternalPublicationDate(String value) {
        setAttribute(ItemSchemeDS.EXTERNAL_PUBLICATION_DATE, value);
    }

    public void setExternalPublicationUser(String value) {
        setAttribute(ItemSchemeDS.EXTERNAL_PUBLICATION_USER, value);
    }

    public void setCreationDate(String value) {
        setAttribute(ItemSchemeDS.VERSION_CREATION_DATE, value);
    }

    public void setResourceCreationDate(String value) {
        setAttribute(ItemSchemeDS.RESOURCE_CREATION_DATE, value);
    }

    public void setResourceId(Long value) {
        setAttribute(VersionableResourceDS.RESOURCE_ID, value);
    }
}
