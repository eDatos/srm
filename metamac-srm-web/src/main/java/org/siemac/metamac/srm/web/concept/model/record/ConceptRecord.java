package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptRecord extends ListGridRecord {

    public ConceptRecord(Long id, String code, String name, String urn, String description, ConceptMetamacDto conceptDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setDescription(description);
        setConceptDto(conceptDto);
    }

    public void setId(Long id) {
        setAttribute(ConceptDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(ConceptDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(ConceptDS.URN, value);
    }

    public void setDescription(String desc) {
        setAttribute(ConceptDS.DESCRIPTION, desc);
    }

    public void setCode(String code) {
        setAttribute(ConceptDS.CODE, code);
    }

    public void setConceptDto(ConceptMetamacDto conceptDto) {
        setAttribute(ConceptDS.DTO, conceptDto);
    }

    public Long getId() {
        return getAttributeAsLong(ConceptDS.ID);
    }

    public String getCode() {
        return getAttribute(ConceptDS.CODE);
    }

    public String getName() {
        return getAttribute(ConceptDS.NAME);
    }

    public String getUrn() {
        return getAttribute(ConceptDS.URN);
    }

    public String getDescription() {
        return getAttribute(ConceptDS.DESCRIPTION);
    }

    public ConceptMetamacDto getConceptDto() {
        return (ConceptMetamacDto) getAttributeAsObject(ConceptDS.DTO);
    }
}
