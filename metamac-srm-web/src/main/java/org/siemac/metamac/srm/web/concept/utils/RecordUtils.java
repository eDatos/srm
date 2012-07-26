package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.domain.concept.dto.ConceptDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;

public class RecordUtils {

    public static ConceptSchemeRecord getConceptSchemeRecord(ConceptSchemeDto conceptSchemeDto) {
        ConceptSchemeRecord record = new ConceptSchemeRecord(conceptSchemeDto.getId(), conceptSchemeDto.getCode(), getLocalisedString(conceptSchemeDto.getName()),
                getLocalisedString(conceptSchemeDto.getDescription()), CommonUtils.getConceptSchemeProcStatus(conceptSchemeDto), conceptSchemeDto);
        return record;
    }

    public static ConceptRecord getConceptRecord(ConceptDto conceptDto) {
        ConceptRecord record = new ConceptRecord(conceptDto.getId(), conceptDto.getCode(), getLocalisedString(conceptDto.getName()), getLocalisedString(conceptDto.getDescription()), conceptDto);
        return record;
    }

}
