package org.siemac.metamac.srm.web.client.utils;

import java.util.List;

import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.model.record.RelatedResourceRecord;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class RecordUtils {

    public static RelatedResourceRecord getRelatedResourceRecord(RelatedResourceDto relatedResourceDto) {
        RelatedResourceRecord record = new RelatedResourceRecord(relatedResourceDto.getCode(), relatedResourceDto.getUrn(), relatedResourceDto.getTitle(), relatedResourceDto);
        return record;
    }

    public static CategorisationRecord getCategorisationRecord(CategorisationDto categorisationDto) {
        CategorisationRecord record = new CategorisationRecord(categorisationDto.getId(), categorisationDto.getCode(), InternationalStringUtils.getLocalisedString(categorisationDto.getName()),
                RelatedResourceUtils.getRelatedResourceName(categorisationDto.getCategory()), categorisationDto.getUrn(), categorisationDto.getMaintainer());
        return record;
    }

    public static CategorisationRecord[] getCategorisationRecords(List<CategorisationDto> categorisationDtos) {
        CategorisationRecord[] records = new CategorisationRecord[categorisationDtos.size()];
        int index = 0;
        for (CategorisationDto categorisationDto : categorisationDtos) {
            records[index++] = getCategorisationRecord(categorisationDto);
        }
        return records;
    }
}
