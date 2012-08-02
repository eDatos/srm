package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.concept.ConceptDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptRecord extends ListGridRecord {

    public ConceptRecord(Long id, String code, String name, String description, ConceptDto conceptDto) {
        setId(id);
        setCode(code);
        setName(name);
        setDescription(description);
        setConceptDto(conceptDto);
    }

    public void setId(Long id) {
        setAttribute(ConceptDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(ConceptDS.NAME, name);
    }

    public void setDescription(String desc) {
        setAttribute(ConceptDS.DESCRIPTION, desc);
    }

    public void setCode(String code) {
        setAttribute(ConceptDS.CODE, code);
    }

    public void setConceptDto(ConceptDto conceptDto) {
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

    public String getDescription() {
        return getAttribute(ConceptDS.DESCRIPTION);
    }

}
