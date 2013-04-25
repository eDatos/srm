package org.siemac.metamac.srm.web.category.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;

public class RecordUtils {

    // CATEGORY SCHEMES

    public static CategorySchemeRecord getCategorySchemeRecord(CategorySchemeMetamacDto categorySchemeDto) {
        CategorySchemeRecord record = new CategorySchemeRecord();
        record.setId(categorySchemeDto.getId());
        record.setCode(categorySchemeDto.getCode());
        record.setName(getLocalisedString(categorySchemeDto.getName()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(categorySchemeDto.getLifeCycle().getProcStatus()));
        record.setVersionLogic(categorySchemeDto.getVersionLogic());
        record.setUrn(categorySchemeDto.getUrn());
        record.setMaintainer(RelatedResourceUtils.getRelatedResourceName(categorySchemeDto.getMaintainer()));
        record.setInternalPublicationDate(DateUtils.getFormattedDate(categorySchemeDto.getLifeCycle().getInternalPublicationDate()));
        record.setInternalPublicationUser(categorySchemeDto.getLifeCycle().getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(categorySchemeDto.getLifeCycle().getExternalPublicationDate()));
        record.setExternalPublicationUser(categorySchemeDto.getLifeCycle().getExternalPublicationUser());
        return record;
    }

    public static CategorySchemeRecord getCategorySchemeRecord(CategorySchemeMetamacBasicDto categorySchemeDto) {
        CategorySchemeRecord record = new CategorySchemeRecord();
        record.setCode(categorySchemeDto.getCode());
        record.setName(getLocalisedString(categorySchemeDto.getName()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(categorySchemeDto.getProcStatus()));
        record.setVersionLogic(categorySchemeDto.getVersionLogic());
        record.setUrn(categorySchemeDto.getUrn());
        record.setMaintainer(RelatedResourceUtils.getRelatedResourceName(categorySchemeDto.getMaintainer()));
        record.setInternalPublicationDate(DateUtils.getFormattedDate(categorySchemeDto.getInternalPublicationDate()));
        record.setInternalPublicationUser(categorySchemeDto.getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(categorySchemeDto.getExternalPublicationDate()));
        record.setExternalPublicationUser(categorySchemeDto.getExternalPublicationUser());
        record.setCategorySchemeBasicDto(categorySchemeDto);
        return record;
    }

    // CATEGORIES

    public static CategoryRecord getCategoryRecord(CategoryMetamacDto categoryDto) {
        CategoryRecord record = new CategoryRecord(categoryDto.getId(), categoryDto.getCode(), getLocalisedString(categoryDto.getName()), categoryDto.getUrn(), categoryDto.getItemSchemeVersionUrn(),
                getLocalisedString(categoryDto.getDescription()));
        return record;
    }

    public static CategoryRecord getCategoryRecord(CategoryMetamacBasicDto categoryDto) {
        CategoryRecord record = new CategoryRecord();
        record.setCode(categoryDto.getCode());
        record.setName(getLocalisedString(categoryDto.getName()));
        record.setUrn(categoryDto.getUrn());
        record.setCategorySchemeUrn(categoryDto.getItemSchemeVersionUrn());
        record.setCategoryBasicDto(categoryDto);
        return record;
    }
}
