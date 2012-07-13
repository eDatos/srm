package org.siemac.metamac.srm.web.dsd.utils;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.domain.srm.dto.AnnotationDto;
import org.siemac.metamac.domain.srm.dto.DataAttributeDto;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.dsd.model.record.AnnotationRecord;
import org.siemac.metamac.srm.web.dsd.model.record.AttributeRecord;
import org.siemac.metamac.srm.web.dsd.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.dsd.model.record.DimensionRecord;
import org.siemac.metamac.srm.web.dsd.model.record.GroupKeysRecord;
import org.siemac.metamac.srm.web.dsd.model.record.InternationalAnnotationRecord;
import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.google.gwt.resources.client.ImageResource;

public class RecordUtils {

    /**
     * Returns {@link DimensionRecord} from {@link DimensionComponentDto}
     * 
     * @param dimensionComponentDto
     * @return
     */
    public static DimensionRecord getDimensionRecord(DimensionComponentDto dimensionComponentDto) {
        DimensionRecord dimensionRecord = new DimensionRecord(dimensionComponentDto.getId(), dimensionComponentDto.getIdLogic(), dimensionComponentDto.getCptIdRef() == null
                ? null
                : dimensionComponentDto.getCptIdRef().getCode(), MetamacSrmWeb.getCoreMessages().getString(
                MetamacSrmWeb.getCoreMessages().typeDimensionComponent() + dimensionComponentDto.getTypeDimensionComponent().getName()), dimensionComponentDto);
        return dimensionRecord;
    }

    /**
     * Returns {@link AttributeRecord} from {@link DataAttributeDto}
     * 
     * @param dataAttributeDto
     * @return
     */
    public static AttributeRecord getAttributeRecord(DataAttributeDto dataAttributeDto) {
        AttributeRecord record = new AttributeRecord(dataAttributeDto.getId(), dataAttributeDto.getIdLogic(), MetamacSrmWeb.getCoreMessages().getString(
                MetamacSrmWeb.getCoreMessages().usageStatus() + dataAttributeDto.getUsageStatus().getName()), dataAttributeDto.getCptIdRef() == null ? null : dataAttributeDto.getCptIdRef().getCode(),
                dataAttributeDto);
        return record;
    }

    /**
     * Returns {@link GroupKeysRecord} from {@link DescriptorDto}
     * 
     * @param groupKeys
     * @return
     */
    public static GroupKeysRecord getGroupKeysRecord(DescriptorDto groupKeys) {
        GroupKeysRecord record = new GroupKeysRecord(groupKeys.getId(), groupKeys.getIdLogic(), null, groupKeys);
        return record;
    }

    /**
     * Returns {@link ConceptRecord} from {@link ExternalItemBtDto}
     * 
     * @param concept
     * @return
     */
    public static ConceptRecord getConceptRecord(ExternalItemDto concept) {
        ConceptRecord record = new ConceptRecord(concept.getCode(), null, concept.getCode(), concept);
        return record;
    }

    /**
     * Returns {@link InternationalAnnotationRecord} from {@link AnnotationDto}
     * 
     * @param annotationDto
     * @return
     */
    public static InternationalAnnotationRecord getInternationalAnnotationRecord(AnnotationDto annotationDto) {
        InternationalAnnotationRecord record = new InternationalAnnotationRecord(annotationDto.getId().toString(), annotationDto.getId() + InternationalStringUtils.getCurrentLocale(),
                annotationDto.getIdLogic(), annotationDto.getTitle(), annotationDto.getType(), annotationDto.getUrl(), InternationalStringUtils.getLocalisedString(annotationDto.getText()),
                InternationalStringUtils.getCurrentLocale(), ((ImageResource) GlobalResources.RESOURCE.getResource(InternationalStringUtils.getCurrentLocale())).getURL(), annotationDto);
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
        InternationalAnnotationRecord record = new InternationalAnnotationRecord(annotationDto.getId().toString(), annotationDto.getId() + locale, annotationDto.getIdLogic(),
                annotationDto.getTitle(), annotationDto.getType(), annotationDto.getUrl(), InternationalStringUtils.getLocalisedString(annotationDto.getText(), locale), locale,
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
        AnnotationRecord record = new AnnotationRecord(annotationDto.getIdLogic(), annotationDto.getTitle(), annotationDto.getType(), annotationDto.getUrl(),
                InternationalStringUtils.getLocalisedString(annotationDto.getText()), annotationDto);
        return record;
    }

    /**
     * Returns {@link AnnotationRecord} from {@link AnnotationDto} given a locale
     * 
     * @param annotationDto
     * @return
     */
    public static AnnotationRecord getAnnotationRecord(AnnotationDto annotationDto, String locale) {
        AnnotationRecord record = new AnnotationRecord(annotationDto.getIdLogic(), annotationDto.getTitle(), annotationDto.getType(), annotationDto.getUrl(),
                InternationalStringUtils.getLocalisedString(annotationDto.getText(), locale), annotationDto);
        return record;
    }

}
