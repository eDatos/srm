package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.web.client.model.ds.RelatedResourceDS;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RelatedResourceRecord extends ListGridRecord {

    public RelatedResourceRecord(String code, String urn, InternationalStringDto title, RelatedResourceDto relatedResourceDto) {
        setCode(code);
        setUrn(urn);
        setTitle(InternationalStringUtils.getLocalisedString(title));
        setRelatedResourceDto(relatedResourceDto);
    }

    public void setCode(String value) {
        setAttribute(RelatedResourceDS.CODE, value);
    }

    public String getCode() {
        return getAttributeAsString(RelatedResourceDS.CODE);
    }

    public void setUrn(String value) {
        setAttribute(RelatedResourceDS.URN, value);
    }

    public String getUrn() {
        return getAttributeAsString(RelatedResourceDS.URN);
    }

    public void setTitle(String value) {
        setAttribute(RelatedResourceDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(RelatedResourceDS.TITLE);
    }

    public void setRelatedResourceDto(RelatedResourceDto relatedResourceDto) {
        setAttribute(RelatedResourceDS.DTO, relatedResourceDto);
    }

    public RelatedResourceDto getRelatedResourceDto() {
        return (RelatedResourceDto) getAttributeAsObject(RelatedResourceDS.DTO);
    }

}
