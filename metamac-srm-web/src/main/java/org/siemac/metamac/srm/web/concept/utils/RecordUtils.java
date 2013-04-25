package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.web.concept.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeRecord;
import org.siemac.metamac.srm.web.concept.model.record.ConceptTreeNode;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemDto;

public class RecordUtils {

    // CONCEPT SCHEMES

    public static ConceptSchemeRecord getConceptSchemeRecord(ConceptSchemeMetamacDto conceptSchemeDto) {
        ConceptSchemeRecord record = new ConceptSchemeRecord();
        record.setId(conceptSchemeDto.getId());
        record.setCode(conceptSchemeDto.getCode());
        record.setName(getLocalisedString(conceptSchemeDto.getName()));
        record.setDescription(getLocalisedString(conceptSchemeDto.getDescription()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(conceptSchemeDto.getLifeCycle().getProcStatus()));
        record.setVersionLogic(conceptSchemeDto.getVersionLogic());
        record.setUrn(conceptSchemeDto.getUrn());
        record.setMaintainer(RelatedResourceUtils.getRelatedResourceName(conceptSchemeDto.getMaintainer()));
        record.setInternalPublicationDate(DateUtils.getFormattedDate(conceptSchemeDto.getLifeCycle().getInternalPublicationDate()));
        record.setInternalPublicationUser(conceptSchemeDto.getLifeCycle().getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(conceptSchemeDto.getLifeCycle().getExternalPublicationDate()));
        record.setExternalPublicationUser(conceptSchemeDto.getLifeCycle().getExternalPublicationUser());
        return record;
    }

    public static ConceptSchemeRecord getConceptSchemeRecord(ConceptSchemeMetamacBasicDto conceptSchemeDto) {
        ConceptSchemeRecord record = new ConceptSchemeRecord();
        record.setCode(conceptSchemeDto.getCode());
        record.setName(getLocalisedString(conceptSchemeDto.getName()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(conceptSchemeDto.getProcStatus()));
        record.setVersionLogic(conceptSchemeDto.getVersionLogic());
        record.setUrn(conceptSchemeDto.getUrn());
        record.setMaintainer(RelatedResourceUtils.getRelatedResourceName(conceptSchemeDto.getMaintainer()));
        record.setInternalPublicationDate(DateUtils.getFormattedDate(conceptSchemeDto.getInternalPublicationDate()));
        record.setInternalPublicationUser(conceptSchemeDto.getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(conceptSchemeDto.getExternalPublicationDate()));
        record.setExternalPublicationUser(conceptSchemeDto.getExternalPublicationUser());
        record.setConceptSchemeBasicDto(conceptSchemeDto);
        return record;
    }

    // CONCEPTS

    public static ConceptRecord getConceptRecord(ConceptMetamacDto conceptDto) {
        ConceptRecord record = new ConceptRecord(conceptDto.getId(), conceptDto.getCode(), getLocalisedString(conceptDto.getName()), conceptDto.getUrn(), conceptDto.getItemSchemeVersionUrn(),
                getLocalisedString(conceptDto.getDescription()));
        return record;
    }

    public static ConceptRecord getConceptRecord(ConceptMetamacBasicDto conceptDto) {
        ConceptRecord record = new ConceptRecord();
        record.setCode(conceptDto.getCode());
        record.setName(getLocalisedString(conceptDto.getName()));
        record.setUrn(conceptDto.getUrn());
        record.setConceptSchemeUrn(conceptDto.getItemSchemeVersionUrn());
        record.setConceptBasicDto(conceptDto);
        return record;
    }

    public static ConceptRecord getConceptRecord(ItemDto itemDto) {
        ConceptRecord record = new ConceptRecord(itemDto.getId(), itemDto.getCode(), getLocalisedString(itemDto.getName()), itemDto.getUrn(), itemDto.getItemSchemeVersionUrn(), null);
        return record;
    }

    public static ConceptTreeNode getConceptTreeNode(ConceptMetamacDto conceptDto) {
        ConceptTreeNode treeNode = new ConceptTreeNode(conceptDto.getId(), conceptDto.getCode(), getLocalisedString(conceptDto.getName()), conceptDto.getUrn(), conceptDto);
        return treeNode;
    }
}
