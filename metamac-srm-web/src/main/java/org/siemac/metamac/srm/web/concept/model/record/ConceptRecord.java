package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.ItemRecord;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;

public class ConceptRecord extends ItemRecord {

    public ConceptRecord() {
    }

    public void setAcronym(String value) {
        setAttribute(ConceptDS.ACRONYM, value);
    }

    public void setVariable(String value) {
        setAttribute(ConceptDS.VARIABLE, value);
    }

    public void setSdmxRelatedArtefact(String value) {
        setAttribute(ConceptDS.SDMX_RELATED_ARTEFACT, value);
    }

    public void setConceptBasicDto(ConceptMetamacBasicDto conceptDto) {
        setAttribute(ConceptDS.DTO, conceptDto);
    }

    public ConceptMetamacBasicDto getConceptBasicDto() {
        return (ConceptMetamacBasicDto) getAttributeAsObject(ConceptDS.DTO);
    }
}
