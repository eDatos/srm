package org.siemac.metamac.srm.web.category.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;

public class RecordUtils {

    // CATEGORY SCHEMES

    public static CategorySchemeRecord getCategorySchemeRecord(CategorySchemeMetamacBasicDto categorySchemeDto) {
        CategorySchemeRecord record = new CategorySchemeRecord();
        record.setCode(categorySchemeDto.getCode());
        record.setUrn(categorySchemeDto.getUrn());
        record.setVersionLogic(categorySchemeDto.getVersionLogic());
        record.setName(getLocalisedString(categorySchemeDto.getName()));
        record.setMaintainer(RelatedResourceUtils.getRelatedResourceName(categorySchemeDto.getMaintainer()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(categorySchemeDto.getLifeCycle().getProcStatus()));
        record.setCreationDate(DateUtils.getFormattedDate(categorySchemeDto.getCreatedDate()));
        record.setResourceCreationDate(DateUtils.getFormattedDate(categorySchemeDto.getResourceCreatedDate()));
        record.setInternalPublicationDate(DateUtils.getFormattedDate(categorySchemeDto.getLifeCycle().getInternalPublicationDate()));
        record.setInternalPublicationUser(categorySchemeDto.getLifeCycle().getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(categorySchemeDto.getLifeCycle().getExternalPublicationDate()));
        record.setExternalPublicationUser(categorySchemeDto.getLifeCycle().getExternalPublicationUser());
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
