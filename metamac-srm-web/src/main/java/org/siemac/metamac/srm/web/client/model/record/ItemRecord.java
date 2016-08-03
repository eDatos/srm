package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.web.common.client.widgets.NavigableListGridRecord;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class ItemRecord extends NavigableListGridRecord {

    public ItemRecord() {
    }

    public void setId(Long id) {
        setAttribute(ItemDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(ItemDS.NAME, name);
    }

    public void setCode(String code) {
        setAttribute(ItemDS.CODE, code);
    }

    public void setUrn(String value) {
        setAttribute(ItemDS.URN, value);
    }

    public void setItemSchemeUrn(String value) {
        setAttribute(ItemDS.ITEM_SCHEME_URN, value);
    }

    public void setItemScheme(RelatedResourceDto relatedResourceDto) {
        setRelatedResource(ItemDS.ITEM_SCHEME, relatedResourceDto);
    }

    public String getItemSchemeUrn() {
        return getAttributeAsString(ItemDS.ITEM_SCHEME_URN);
    }

    public Long getId() {
        return getAttributeAsLong(ItemDS.ID);
    }

    public String getCode() {
        return getAttribute(ItemDS.CODE);
    }

    public String getName() {
        return getAttribute(ItemDS.NAME);
    }

    public String getUrn() {
        return getAttributeAsString(ItemDS.URN);
    }

    public void setCreationDate(String value) {
        setAttribute(ItemDS.CREATION_DATE, value);
    }
}
