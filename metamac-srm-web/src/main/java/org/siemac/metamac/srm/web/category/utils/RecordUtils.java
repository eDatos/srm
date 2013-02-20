package org.siemac.metamac.srm.web.category.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;

public class RecordUtils {

    // CATEGORY SCHEMES

    public static CategorySchemeRecord getCategorySchemeRecord(CategorySchemeMetamacDto categorySchemeDto) {
        CategorySchemeRecord record = new CategorySchemeRecord(categorySchemeDto.getId(), categorySchemeDto.getCode(), getLocalisedString(categorySchemeDto.getName()),
                org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(categorySchemeDto.getLifeCycle().getProcStatus()), categorySchemeDto.getVersionLogic(),
                categorySchemeDto.getUrn(), categorySchemeDto);
        return record;
    }

    // CATEGORIES

    public static CategoryRecord getCategoryRecord(CategoryMetamacDto categoryDto) {
        CategoryRecord record = new CategoryRecord(categoryDto.getId(), categoryDto.getCode(), getLocalisedString(categoryDto.getName()), categoryDto.getUrn(), categoryDto.getItemSchemeVersionUrn(),
                getLocalisedString(categoryDto.getDescription()), categoryDto);
        return record;
    }
}
