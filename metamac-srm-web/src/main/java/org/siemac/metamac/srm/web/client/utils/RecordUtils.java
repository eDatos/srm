package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.List;

import org.siemac.metamac.srm.core.base.dto.ItemMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.ItemSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.LifeCycleBasicDto;
import org.siemac.metamac.srm.core.base.dto.MaintainableArtefactMetamacBasicDto;
import org.siemac.metamac.srm.core.base.dto.StructureMetamacBasicDto;
import org.siemac.metamac.srm.web.client.model.record.CategorisationRecord;
import org.siemac.metamac.srm.web.client.model.record.ItemRecord;
import org.siemac.metamac.srm.web.client.model.record.VersionableResourceRecord;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;

public class RecordUtils {

    //
    // VERSIONABLE RESOURCE
    //

    public static VersionableResourceRecord getVersionableResourceRecord(VersionableResourceRecord record, MaintainableArtefactMetamacBasicDto maintainableArtefactDto, LifeCycleBasicDto lifeCycleDto) {
        record.setCode(maintainableArtefactDto.getCode());
        record.setUrn(maintainableArtefactDto.getUrn());
        record.setVersionLogic(maintainableArtefactDto.getVersionLogic());
        record.setName(getLocalisedString(maintainableArtefactDto.getName()));
        record.setProcStatus(org.siemac.metamac.srm.web.client.utils.CommonUtils.getProcStatusName(lifeCycleDto.getProcStatus()));
        record.setMaintainer(maintainableArtefactDto.getMaintainer());
        record.setInternalPublicationDate(DateUtils.getFormattedDate(lifeCycleDto.getInternalPublicationDate()));
        record.setInternalPublicationUser(lifeCycleDto.getInternalPublicationUser());
        record.setExternalPublicationDate(DateUtils.getFormattedDate(lifeCycleDto.getExternalPublicationDate()));
        record.setExternalPublicationUser(lifeCycleDto.getExternalPublicationUser());
        return record;
    }

    //
    // ITEM SCHEME
    //

    public static VersionableResourceRecord getItemSchemeRecord(VersionableResourceRecord record, ItemSchemeMetamacBasicDto itemSchemeMetamacBasicDto, LifeCycleBasicDto lifeCycleDto) {
        record = getVersionableResourceRecord(record, itemSchemeMetamacBasicDto, lifeCycleDto);
        record.setCreationDate(DateUtils.getFormattedDate(itemSchemeMetamacBasicDto.getCreatedDate()));
        record.setResourceCreationDate(DateUtils.getFormattedDate(itemSchemeMetamacBasicDto.getResourceCreatedDate()));
        record.setResourceId(itemSchemeMetamacBasicDto.getItemSchemeId());
        return record;
    }

    //
    // STRUCTURE
    //

    public static VersionableResourceRecord getStructureRecord(VersionableResourceRecord record, StructureMetamacBasicDto structureMetamacBasicDto, LifeCycleBasicDto lifeCycleDto) {
        record = getVersionableResourceRecord(record, structureMetamacBasicDto, lifeCycleDto);
        record.setCreationDate(DateUtils.getFormattedDate(structureMetamacBasicDto.getCreatedDate()));
        record.setResourceCreationDate(DateUtils.getFormattedDate(structureMetamacBasicDto.getResourceCreatedDate()));
        record.setResourceId(structureMetamacBasicDto.getStructureId());
        return record;
    }

    //
    // ITEM
    //

    public static ItemRecord getItemRecord(ItemRecord record, ItemMetamacBasicDto itemMetamacBasicDto) {
        record.setCode(itemMetamacBasicDto.getCode());
        record.setUrn(itemMetamacBasicDto.getUrn());
        record.setName(getLocalisedString(itemMetamacBasicDto.getName()));
        record.setCreationDate(DateUtils.getFormattedDate(itemMetamacBasicDto.getCreatedDate()));
        record.setItemSchemeUrn(itemMetamacBasicDto.getItemSchemeVersion().getUrn());
        record.setItemScheme(itemMetamacBasicDto.getItemSchemeVersion());
        return record;
    }

    //
    // CATEGORISATIONS
    //

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
