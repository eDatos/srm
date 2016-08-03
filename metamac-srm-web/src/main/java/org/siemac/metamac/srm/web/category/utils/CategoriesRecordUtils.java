package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;

public class CategoriesRecordUtils extends org.siemac.metamac.srm.web.client.utils.RecordUtils {

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
        record = (CategoryRecord) getItemRecord(record, categoryDto);
        record.setCategoryBasicDto(categoryDto);
        return record;
    }
}
