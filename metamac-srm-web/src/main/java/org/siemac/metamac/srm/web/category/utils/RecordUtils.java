package org.siemac.metamac.srm.web.category.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.web.common.client.utils.DateUtils;

public class RecordUtils extends org.siemac.metamac.srm.web.client.utils.RecordUtils {

    // CATEGORY SCHEMES

    public static CategorySchemeRecord getCategorySchemeRecord(CategorySchemeMetamacBasicDto categorySchemeDto) {
        CategorySchemeRecord record = new CategorySchemeRecord();
        record = (CategorySchemeRecord) getItemSchemeRecord(record, categorySchemeDto, categorySchemeDto.getLifeCycle());
        record.setCategorySchemeBasicDto(categorySchemeDto);
        return record;
    }

    // CATEGORIES

    public static CategoryRecord getCategoryRecord(CategoryMetamacBasicDto categoryDto) {
        CategoryRecord record = new CategoryRecord();
        record.setCode(categoryDto.getCode());
        record.setName(getLocalisedString(categoryDto.getName()));
        record.setUrn(categoryDto.getUrn());
        record.setItemSchemeUrn(categoryDto.getItemSchemeVersionUrn());
        record.setCreationDate(DateUtils.getFormattedDate(categoryDto.getCreatedDate()));
        record.setCategoryBasicDto(categoryDto);
        return record;
    }
}
