package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemeRecord extends ListGridRecord {

    public ConceptSchemeRecord(Long id, String code, String name, String description, String status, String versionLogic, String urn, ConceptSchemeMetamacDto conceptSchemeDto) {
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

    public void setConceptSchemeDto(ConceptSchemeMetamacDto conceptSchemeDto) {
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

    public ProcStatusEnum getProcStatus() {
        return ((ConceptSchemeMetamacDto) getAttributeAsObject(ConceptSchemeDS.DTO)).getProcStatus();
    }

    public String getDescription() {
        return getAttribute(ConceptSchemeDS.DESCRIPTION);
    }

    public String getUrn() {
        return getAttributeAsString(ConceptSchemeDS.URN);
    }

    public ConceptSchemeMetamacDto getConceptSchemeDto() {
        return (ConceptSchemeMetamacDto) getAttributeAsObject(ConceptSchemeDS.DTO);
    }

}
