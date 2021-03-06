package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.VersionableResourceRecord;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;

public class ConceptSchemeRecord extends VersionableResourceRecord {

    public ConceptSchemeRecord() {

    }

    public void setType(String value) {
        setAttribute(ConceptSchemeDS.TYPE, value);
    }

    public void setStatisticalOperation(ExternalItemDto value) {
        setExternalItem(ConceptSchemeDS.RELATED_OPERATION, value);
    }

    public ProcStatusEnum getProcStatus() {
        return ((ConceptSchemeMetamacBasicDto) getAttributeAsObject(ConceptSchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public void setConceptSchemeBasicDto(ConceptSchemeMetamacBasicDto conceptSchemeDto) {
        setAttribute(ConceptSchemeDS.DTO, conceptSchemeDto);
    }

    public ConceptSchemeMetamacBasicDto getConceptSchemeBasicDto() {
        return (ConceptSchemeMetamacBasicDto) getAttributeAsObject(ConceptSchemeDS.DTO);
    }
}
