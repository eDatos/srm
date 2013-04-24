package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;

public class ConceptSchemeRecord extends ItemSchemeRecord {

    public ConceptSchemeRecord() {

    }

    public ConceptSchemeRecord(Long id, String code, String name, String description, String status, String versionLogic, String urn, String maintainer, String internalPublicationDate,
            String internalPublicationUser, String externalPublicationDate, String externalPublicationUser, ConceptSchemeMetamacDto conceptSchemeDto) {
        super();
        setId(id);
        setCode(code);
        setName(name);
        setProcStatus(status);
        setVersionLogic(versionLogic);
        setUrn(urn);
        setMaintainer(maintainer);
        setInternalPublicationDate(internalPublicationDate);
        setInternalPublicationUser(internalPublicationUser);
        setExternalPublicationDate(externalPublicationDate);
        setExternalPublicationUser(externalPublicationUser);
        setDescription(description);
        setConceptSchemeDto(conceptSchemeDto);
    }

    public void setDescription(String desc) {
        setAttribute(ConceptSchemeDS.DESCRIPTION, desc);
    }

    public ProcStatusEnum getProcStatus() {
        return ((ConceptSchemeMetamacDto) getAttributeAsObject(ConceptSchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public String getDescription() {
        return getAttribute(ConceptSchemeDS.DESCRIPTION);
    }

    public void setConceptSchemeDto(ConceptSchemeMetamacDto conceptSchemeDto) {
        setAttribute(ConceptSchemeDS.DTO, conceptSchemeDto);
    }

    public ConceptSchemeMetamacDto getConceptSchemeDto() {
        return (ConceptSchemeMetamacDto) getAttributeAsObject(ConceptSchemeDS.DTO);
    }

    public void setConceptSchemeBasicDto(ConceptSchemeMetamacBasicDto conceptSchemeDto) {
        setAttribute(ConceptSchemeDS.DTO, conceptSchemeDto);
    }

    public ConceptSchemeMetamacBasicDto getConceptSchemeBasicDto() {
        return (ConceptSchemeMetamacBasicDto) getAttributeAsObject(ConceptSchemeDS.DTO);
    }
}
