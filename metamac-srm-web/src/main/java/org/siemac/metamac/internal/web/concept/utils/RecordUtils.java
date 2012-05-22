package org.siemac.metamac.internal.web.concept.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.concept.model.record.ConceptSchemeRecord;

public class RecordUtils {

    public static ConceptSchemeRecord getConceptSchemeRecord(ConceptSchemeDto conceptSchemeDto) {
        ConceptSchemeRecord record = new ConceptSchemeRecord(conceptSchemeDto.getUuid(), conceptSchemeDto.getIdLogic(), getLocalisedString(conceptSchemeDto.getName()), CommonUtils.getConceptSchemeProcStatus(conceptSchemeDto), conceptSchemeDto);
        return record;
    }
   
}
