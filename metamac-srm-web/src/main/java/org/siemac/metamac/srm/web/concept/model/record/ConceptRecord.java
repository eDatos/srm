package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.web.common.client.widgets.NavigableListGridRecord;

public class ConceptRecord extends NavigableListGridRecord {

    public ConceptRecord() {
    }

    public ConceptRecord(Long id, String code, String name, String urn, String conceptSchemeUrn, String description) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setConceptSchemeUrn(conceptSchemeUrn);
        setDescription(description);
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

    public void setConceptSchemeUrn(String value) {
        setAttribute(ConceptDS.ITEM_SCHEME_URN, value);
    }

    public void setDescription(String desc) {
        setAttribute(ConceptDS.DESCRIPTION, desc);
    }

    public void setCode(String code) {
        setAttribute(ConceptDS.CODE, code);
    }

    public void setConceptBasicDto(ConceptMetamacBasicDto conceptDto) {
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

    public String getConceptSchemeUrn() {
        return getAttribute(ConceptDS.ITEM_SCHEME_URN);
    }

    public String getDescription() {
        return getAttribute(ConceptDS.DESCRIPTION);
    }

    public ConceptMetamacBasicDto getConceptBasicDto() {
        return (ConceptMetamacBasicDto) getAttributeAsObject(ConceptDS.DTO);
    }
}
