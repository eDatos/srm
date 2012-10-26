package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.web.client.model.record.RelatedResourceRecord;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class RecordUtils {

    public static RelatedResourceRecord getRelatedResourceRecord(RelatedResourceDto relatedResourceDto) {
        RelatedResourceRecord record = new RelatedResourceRecord(relatedResourceDto.getCode(), relatedResourceDto.getUrn(), relatedResourceDto.getTitle(), relatedResourceDto);
        return record;
    }

}
