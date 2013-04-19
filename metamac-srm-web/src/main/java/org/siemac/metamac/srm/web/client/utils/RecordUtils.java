package org.siemac.metamac.srm.web.client.utils;

import java.util.List;

import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;

public class RecordUtils {

    public static CategorisationRecord getCategorisationRecord(CategorisationDto categorisationDto) {
        CategorisationRecord record = new CategorisationRecord(categorisationDto.getId(), categorisationDto.getCode(), InternationalStringUtils.getLocalisedString(categorisationDto.getName()),
                RelatedResourceUtils.getRelatedResourceName(categorisationDto.getCategory()), categorisationDto.getUrn(), categorisationDto.getMaintainer(), categorisationDto);
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

    /**
     * Remove the record from the RecordList (if exists) given propertyName and the value of the record
     * 
     * @param recordList
     * @param propertyName
     * @param value
     * @return
     */
    public static void removeRecord(RecordList recordList, String propertyName, String value) {
        Record record = recordList.find(propertyName, value);
        if (record != null) {
            recordList.remove(record);
        }
    }
}
