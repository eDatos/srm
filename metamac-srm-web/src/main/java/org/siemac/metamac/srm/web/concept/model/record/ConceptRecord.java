package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.domain.concept.dto.ConceptDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptRecord extends ListGridRecord {

    public ConceptRecord(Long id, String idLogic, String name, String description, ConceptDto conceptDto) {
        setId(id);
        setIdLogic(idLogic);
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

    public void setIdLogic(String idLogic) {
        setAttribute(ConceptDS.ID_LOGIC, idLogic);
    }

    public void setConceptDto(ConceptDto conceptDto) {
        setAttribute(ConceptDS.DTO, conceptDto);
    }

    public Long getId() {
        return getAttributeAsLong(ConceptDS.ID);
    }

    public String getIdLogic() {
        return getAttribute(ConceptDS.ID_LOGIC);
    }

    public String getName() {
        return getAttribute(ConceptDS.NAME);
    }

    public String getDescription() {
        return getAttribute(ConceptDS.DESCRIPTION);
    }

}
