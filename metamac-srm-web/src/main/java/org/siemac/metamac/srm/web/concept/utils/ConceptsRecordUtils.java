package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

public class ConceptsRecordUtils extends org.siemac.metamac.srm.web.client.utils.RecordUtils {

    // CONCEPT SCHEMES

    public static ConceptSchemeRecord getConceptSchemeRecord(ConceptSchemeMetamacBasicDto conceptSchemeDto) {
        ConceptSchemeRecord record = new ConceptSchemeRecord();
        record = (ConceptSchemeRecord) getItemSchemeRecord(record, conceptSchemeDto, conceptSchemeDto.getLifeCycle());
        record.setType(CommonUtils.getConceptSchemeTypeName(conceptSchemeDto.getType()));
        record.setStatisticalOperation(conceptSchemeDto.getRelatedOperation());
        record.setConceptSchemeBasicDto(conceptSchemeDto);
        return record;
    }

    // CONCEPTS

    public static ConceptRecord getConceptRecord(ConceptMetamacBasicDto conceptDto) {
        ConceptRecord record = new ConceptRecord();
        record = (ConceptRecord) getItemRecord(record, conceptDto);
        record.setAcronym(getLocalisedString(conceptDto.getAcronym()));
        record.setVariable(conceptDto.getVariable());
        record.setSdmxRelatedArtefact(CommonUtils.getConceptRoleName(conceptDto.getSdmxRelatedArtefact()));
        record.setConceptBasicDto(conceptDto);
        return record;
    }

    public static ConceptRecord getConceptRecord(ItemVisualisationResult concept) {
        ConceptRecord record = new ConceptRecord();
        record.setCode(concept.getCode());
        record.setName(concept.getName());
        record.setUrn(concept.getUrn());
        return record;
    }
}
