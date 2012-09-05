package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptTreeNode;

public class RecordUtils {

    // CONCEPT SCHEMES

    public static ConceptSchemeRecord getConceptSchemeRecord(ConceptSchemeMetamacDto conceptSchemeDto) {
        ConceptSchemeRecord record = new ConceptSchemeRecord(conceptSchemeDto.getId(), conceptSchemeDto.getCode(), getLocalisedString(conceptSchemeDto.getName()),
                getLocalisedString(conceptSchemeDto.getDescription()), CommonUtils.getConceptSchemeProcStatus(conceptSchemeDto), conceptSchemeDto.getVersionLogic(), conceptSchemeDto.getUrn(),
                conceptSchemeDto);
        return record;
    }

    // CONCEPTS

    public static ConceptRecord getConceptRecord(ConceptMetamacDto conceptDto) {
        ConceptRecord record = new ConceptRecord(conceptDto.getId(), conceptDto.getCode(), getLocalisedString(conceptDto.getName()), conceptDto.getUrn(),
                getLocalisedString(conceptDto.getDescription()), conceptDto);
        return record;
    }

    public static ConceptTreeNode getConceptTreeNode(ConceptMetamacDto conceptDto) {
        ConceptTreeNode treeNode = new ConceptTreeNode(conceptDto.getId(), conceptDto.getCode(), getLocalisedString(conceptDto.getName()), conceptDto.getUrn(), conceptDto);
        return treeNode;
    }
}
