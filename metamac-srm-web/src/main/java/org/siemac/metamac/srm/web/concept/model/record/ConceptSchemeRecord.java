package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemeRecord extends ListGridRecord {

    public ConceptSchemeRecord(Long id, String code, String name, String description, String status, String versionLogic, String urn, ConceptSchemeDto conceptSchemeDto) {
        setId(id);
        setCode(code);
        setName(name);
        setDescription(description);
        setProcStatus(status);
        setVersionLogic(versionLogic);
        setUrn(urn);
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

    public void setCode(String code) {
        setAttribute(ConceptSchemeDS.CODE, code);
    }

    public void setProcStatus(String value) {
        setAttribute(ConceptSchemeDS.PROC_STATUS, value);
    }

    public void setVersionLogic(String value) {
        setAttribute(ConceptSchemeDS.VERSION_LOGIC, value);
    }

    public void setUrn(String value) {
        setAttribute(ConceptSchemeDS.URN, value);
    }

    public void setConceptSchemeDto(ConceptSchemeDto conceptSchemeDto) {
        setAttribute(ConceptSchemeDS.DTO, conceptSchemeDto);
    }

    public Long getId() {
        return getAttributeAsLong(ConceptSchemeDS.ID);
    }

    public String getCode() {
        return getAttribute(ConceptSchemeDS.CODE);
    }

    public String getName() {
        return getAttribute(ConceptSchemeDS.NAME);
    }

    public String getDescription() {
        return getAttribute(ConceptSchemeDS.DESCRIPTION);
    }

    public String getUrn() {
        return getAttributeAsString(ConceptSchemeDS.URN);
    }

}
