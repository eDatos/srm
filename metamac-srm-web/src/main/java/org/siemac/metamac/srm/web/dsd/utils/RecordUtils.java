package org.siemac.metamac.srm.web.dsd.utils;

import java.util.List;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.model.record.DsdRecord;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.model.record.AnnotationRecord;
import org.siemac.metamac.srm.web.dsd.model.record.AttributeRecord;
import org.siemac.metamac.srm.web.dsd.model.record.DimensionRecord;
import org.siemac.metamac.srm.web.dsd.model.record.GroupKeysRecord;
import org.siemac.metamac.srm.web.dsd.model.record.InternationalAnnotationRecord;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.google.gwt.resources.client.ImageResource;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RecordUtils {

    /**
     * Returns {@link DsdRecord} from {@link DataStructureDefinitionMetamacDto}
     * 
     * @param dsd
     * @return
     */
    public static DsdRecord getDsdRecord(DataStructureDefinitionMetamacDto dsd) {
        return new DsdRecord(dsd.getId(), dsd.getCode(), InternationalStringUtils.getLocalisedString(dsd.getName()), dsd.getUrn(), InternationalStringUtils.getLocalisedString(dsd.getDescription()),
                dsd.getFinalLogic(), CommonUtils.getProcStatusName(dsd.getLifeCycle().getProcStatus()), dsd.getVersionLogic());
    }

    /**
     * Returns {@link DsdRecord} from {@link DataStructureDefinitionMetamacBasicDto}
     * 
     * @param dsd
     * @return
     */
    public static DsdRecord getDsdRecord(DataStructureDefinitionMetamacBasicDto dsd) {
        DsdRecord record = new DsdRecord();
        record.setCode(dsd.getCode());
        record.setName(InternationalStringUtils.getLocalisedString(dsd.getName()));
        record.setProcStatus(CommonUtils.getProcStatusName(dsd.getProcStatus()));
        record.setVersion(dsd.getVersionLogic());
        record.setDsdBasicDto(dsd);
        return record;
    }

    /**
     * Returns {@link DimensionRecord} from {@link DimensionComponentDto}
     * 
     * @param dimensionComponentDto
     * @return
     */
    public static DimensionRecord getDimensionRecord(DimensionComponentDto dimensionComponentDto) {
        DimensionRecord dimensionRecord = new DimensionRecord(dimensionComponentDto.getId(), dimensionComponentDto.getCode(), dimensionComponentDto.getCptIdRef() == null
                ? null
                : dimensionComponentDto.getCptIdRef().getCode(), org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getDimensionTypeName(dimensionComponentDto), dimensionComponentDto);
        return dimensionRecord;
    }

    public static ListGridRecord[] getDimensionRecords(List<DimensionComponentDto> dimensionComponentDtos) {
        ListGridRecord[] records = new ListGridRecord[dimensionComponentDtos.size()];
        for (int i = 0; i < dimensionComponentDtos.size(); i++) {
            records[i] = RecordUtils.getDimensionRecord(dimensionComponentDtos.get(i));
        }
        return records;
    }

    /**
     * Returns {@link AttributeRecord} from {@link DataAttributeDto}
     * 
     * @param dataAttributeDto
     * @return
     */
    public static AttributeRecord getAttributeRecord(DataAttributeDto dataAttributeDto) {
        AttributeRecord record = new AttributeRecord(dataAttributeDto.getId(), dataAttributeDto.getCode(), MetamacSrmWeb.getCoreMessages().getString(
                MetamacSrmWeb.getCoreMessages().usageStatus() + dataAttributeDto.getUsageStatus().getName()), dataAttributeDto.getCptIdRef() == null ? null : dataAttributeDto.getCptIdRef().getCode(),
                dataAttributeDto);
        return record;
    }

    public static ListGridRecord[] getAttributeRecords(List<DataAttributeDto> dataAttributeDtos) {
        ListGridRecord[] records = new ListGridRecord[dataAttributeDtos.size()];
        for (int i = 0; i < dataAttributeDtos.size(); i++) {
            records[i] = RecordUtils.getAttributeRecord(dataAttributeDtos.get(i));
        }
        return records;
    }

    /**
     * Returns {@link GroupKeysRecord} from {@link DescriptorDto}
     * 
     * @param groupKeys
     * @return
     */
    public static GroupKeysRecord getGroupKeysRecord(DescriptorDto groupKeys) {
        GroupKeysRecord record = new GroupKeysRecord(groupKeys.getId(), groupKeys.getCode(), null, groupKeys);
        return record;
    }

    public static ListGridRecord[] getGroupKeysRecords(List<DescriptorDto> groupKeys) {
        ListGridRecord[] records = new ListGridRecord[groupKeys.size()];
        for (int i = 0; i < groupKeys.size(); i++) {
            records[i] = RecordUtils.getGroupKeysRecord(groupKeys.get(i));
        }
        return records;
    }

    /**
     * Returns {@link InternationalAnnotationRecord} from {@link AnnotationDto}
     * 
     * @param annotationDto
     * @return
     */
    public static InternationalAnnotationRecord getInternationalAnnotationRecord(AnnotationDto annotationDto) {
        InternationalAnnotationRecord record = new InternationalAnnotationRecord(annotationDto.getId().toString(), annotationDto.getId() + ApplicationEditionLanguages.getCurrentLocale(),
                annotationDto.getCode(), annotationDto.getTitle(), annotationDto.getType(), annotationDto.getUrl(), InternationalStringUtils.getLocalisedString(annotationDto.getText()),
                ApplicationEditionLanguages.getCurrentLocale(), ((ImageResource) GlobalResources.RESOURCE.getResource(ApplicationEditionLanguages.getCurrentLocale())).getURL(), annotationDto);
        return record;
    }

    /**
     * Returns translated {@link InternationalAnnotationRecord} from {@link AnnotationDto}
     * 
     * @param annotationDto
     * @param locale
     * @return
     */
    public static InternationalAnnotationRecord getInternationalAnnotationRecord(AnnotationDto annotationDto, String locale) {
        InternationalAnnotationRecord record = new InternationalAnnotationRecord(annotationDto.getId().toString(), annotationDto.getId() + locale, annotationDto.getCode(), annotationDto.getTitle(),
                annotationDto.getType(), annotationDto.getUrl(), InternationalStringUtils.getLocalisedString(annotationDto.getText(), locale), locale,
                ((ImageResource) GlobalResources.RESOURCE.getResource(locale)).getURL(), annotationDto);
        return record;
    }

    /**
     * Returns {@link AnnotationRecord} from {@link AnnotationDto}
     * 
     * @param annotationDto
     * @return
     */
    public static AnnotationRecord getAnnotationRecord(AnnotationDto annotationDto) {

        // Mark the whole record as unmodifiable when the text (localisedString) is unmodifiable. This is possible because in the annotations list only the text and the URL are shown. If the record is
        // mark as modifiable, maybe the URL can not be edited. This is checked in the AnnotationsPanel.
        boolean isRecordEditable = InternationalStringUtils.isCurrentLocalisedStringModifiable(annotationDto.getText());

        AnnotationRecord record = new AnnotationRecord(annotationDto.getId(), annotationDto.getCode(), annotationDto.getTitle(), annotationDto.getType(), annotationDto.getUrl(),
                InternationalStringUtils.getLocalisedString(annotationDto.getText()), isRecordEditable, annotationDto);
        return record;
    }

    /**
     * Returns {@link AnnotationRecord} from {@link AnnotationDto} given a locale
     * 
     * @param annotationDto
     * @return
     */
    public static AnnotationRecord getAnnotationRecord(AnnotationDto annotationDto, String locale) {

        // Mark the whole record as unmodifiable when the text (localisedString) is unmodifiable. This is possible because in the annotations list only the text and the URL are shown. If the record is
        // mark as modifiable, maybe the URL can not be edited. This is checked in the AnnotationsPanel.
        boolean isRecordEdiable = InternationalStringUtils.isLocalisedStringModifiable(annotationDto.getText(), locale);

        AnnotationRecord record = new AnnotationRecord(annotationDto.getId(), annotationDto.getCode(), annotationDto.getTitle(), annotationDto.getType(), annotationDto.getUrl(),
                InternationalStringUtils.getLocalisedString(annotationDto.getText(), locale), isRecordEdiable, annotationDto);
        return record;
    }
}
