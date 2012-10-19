package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;

public class ConceptSchemeRecord extends ItemSchemeRecord {

    public ConceptSchemeRecord(Long id, String code, String name, String description, String status, String versionLogic, String urn, ConceptSchemeMetamacDto conceptSchemeDto) {
        super(id, code, name, status, versionLogic, urn);
        setDescription(description);
        setConceptSchemeDto(conceptSchemeDto);
    }

    public void setDescription(String desc) {
        setAttribute(ConceptSchemeDS.DESCRIPTION, desc);
    }

    public void setConceptSchemeDto(ConceptSchemeMetamacDto conceptSchemeDto) {
        setAttribute(ConceptSchemeDS.DTO, conceptSchemeDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((ConceptSchemeMetamacDto) getAttributeAsObject(ConceptSchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public String getDescription() {
        return getAttribute(ConceptSchemeDS.DESCRIPTION);
    }

    public ConceptSchemeMetamacDto getConceptSchemeDto() {
        return (ConceptSchemeMetamacDto) getAttributeAsObject(ConceptSchemeDS.DTO);
    }

}
