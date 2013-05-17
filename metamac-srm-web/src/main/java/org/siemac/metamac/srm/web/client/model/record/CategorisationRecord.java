package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.srm.web.client.model.ds.CategorisationDS;
import org.siemac.metamac.web.common.client.widgets.NavigableListGridRecord;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CategorisationRecord extends NavigableListGridRecord {

    public CategorisationRecord(Long id, String code, String name, RelatedResourceDto category, String urn, RelatedResourceDto maintainer, CategorisationDto categorisationDto) {
        setId(id);
        setCode(code);
        setName(name);
        setCategory(category);
        setUrn(urn);
        setMaintainer(maintainer);
        setCategorisationDto(categorisationDto);
    }

    public void setId(Long id) {
        setAttribute(CategorisationDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(CategorisationDS.NAME, name);
    }

    public void setCode(String code) {
        setAttribute(CategorisationDS.CODE, code);
    }

    public void setUrn(String value) {
        setAttribute(CategorisationDS.URN, value);
    }

    public void setCategory(RelatedResourceDto value) {
        setRelatedResource(CategorisationDS.CATEGORY, value);
    }

    public Long getId() {
        return getAttributeAsLong(CategorisationDS.ID);
    }

    public String getCode() {
        return getAttribute(CategorisationDS.CODE);
    }

    public String getName() {
        return getAttribute(CategorisationDS.NAME);
    }

    public String getUrn() {
        return getAttributeAsString(CategorisationDS.URN);
    }

    public void setMaintainer(RelatedResourceDto maintainer) {
        setAttribute(CategorisationDS.MAINTAINER, maintainer);
    }

    public RelatedResourceDto getMaintainer() {
        return (RelatedResourceDto) getAttributeAsObject(CategorisationDS.MAINTAINER);
    }

    public void setCategorisationDto(CategorisationDto categorisationDto) {
        setAttribute(CategorisationDS.DTO, categorisationDto);
    }

    public CategorisationDto getCategorisationDto() {
        return (CategorisationDto) getAttributeAsObject(CategorisationDS.DTO);
    }
}
