package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemeRecord extends ListGridRecord {

    public ConceptSchemeRecord(Long id, String idLogic, String name, String description, String status, ConceptSchemeDto conceptSchemeDto) {
        setId(id);
        setName(name);
        setDescription(description);
        setIdLogic(idLogic);
        setProcStatus(status);
        setConceptSchemeDto(conceptSchemeDto);
    }

    public void setId(Long id) {
        setAttribute(ConceptSchemeDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(ConceptSchemeDS.NAME, name);
    }

    public void setDescription(String desc) {
        setAttribute(ConceptSchemeDS.DESCRIPTION, desc);
    }

    public void setIdLogic(String idLogic) {
        setAttribute(ConceptSchemeDS.ID_LOGIC, idLogic);
    }

    public void setProcStatus(String value) {
        setAttribute(ConceptSchemeDS.PROC_STATUS, value);
    }

    public void setConceptSchemeDto(ConceptSchemeDto conceptSchemeDto) {
        setAttribute(ConceptSchemeDS.DTO, conceptSchemeDto);
    }

    public Long getId() {
        return getAttributeAsLong(ConceptSchemeDS.ID);
    }

    public String getIdLogic() {
        return getAttribute(ConceptSchemeDS.ID_LOGIC);
    }

    public String getName() {
        return getAttribute(ConceptSchemeDS.NAME);
    }

    public String getDescription() {
        return getAttribute(ConceptSchemeDS.DESCRIPTION);
    }

}
