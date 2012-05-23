package org.siemac.metamac.internal.web.concept.model.record;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.concept.model.ds.ConceptSchemeDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemeRecord extends ListGridRecord {

    public ConceptSchemeRecord(String uuid, String idLogic, String name, String status, ConceptSchemeDto conceptSchemeDto) {
        setUuid(uuid);
        setName(name);
        setIdLogic(idLogic);
        setProcStatus(status);
        setConceptSchemeDto(conceptSchemeDto);
    }

    public void setUuid(String uuid) {
        setAttribute(ConceptSchemeDS.UUID, uuid);
    }

    public void setName(String name) {
        setAttribute(ConceptSchemeDS.NAME, name);
    }

    public void setIdLogic(String idLogic) {
        setAttribute(ConceptSchemeDS.ID_LOGIC, idLogic);
    }

    public String getUuid() {
        return getAttribute(ConceptSchemeDS.UUID);
    }

    public void setProcStatus(String value) {
        setAttribute(ConceptSchemeDS.PROC_STATUS, value);
    }

    public void setConceptSchemeDto(ConceptSchemeDto conceptSchemeDto) {
        setAttribute(ConceptSchemeDS.DTO, conceptSchemeDto);
    }
}
