package org.siemac.metamac.srm.web.dsd.model.record;

import org.siemac.metamac.srm.web.dsd.model.ds.DimensionDS;
import org.siemac.metamac.web.common.client.widgets.NavigableListGridRecord;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;

public class DimensionRecord extends NavigableListGridRecord {

    public DimensionRecord() {
    }

    public DimensionRecord(Long id, String idLogic, RelatedResourceDto concept, String type, DimensionComponentDto dimensionComponentDto) {
        setIdentifier(id);
        setCode(idLogic);
        setConcept(concept);
        setType(type);
        setDimensionComponentDto(dimensionComponentDto);
    }

    public void setIdentifier(Long value) {
        setAttribute(DimensionDS.ID, value);
    }

    public void setCode(String value) {
        setAttribute(DimensionDS.CODE, value);
    }

    public void setConcept(RelatedResourceDto relatedResourceDto) {
        setRelatedResource(DimensionDS.CONCEPT, relatedResourceDto);
    }

    public void setType(String attribute) {
        setAttribute(DimensionDS.TYPE, attribute);
    }

    public void setDimensionComponentDto(DimensionComponentDto value) {
        setAttribute(DimensionDS.DTO, value);
    }

    public Long getIdentifier() {
        return getAttributeAsLong(DimensionDS.ID);
    }

    public String getCode() {
        return getAttributeAsString(DimensionDS.CODE);
    }

    public String getConcept() {
        return getAttributeAsString(DimensionDS.CONCEPT);
    }

    public String getType() {
        return getAttributeAsString(DimensionDS.TYPE);
    }

    public DimensionComponentDto getDimensionComponentDto() {
        return (DimensionComponentDto) getAttributeAsObject(DimensionDS.DTO);
    }
}
