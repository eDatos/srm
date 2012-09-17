package org.siemac.metamac.srm.web.dsd.model.record;

import org.siemac.metamac.srm.web.client.model.ds.AnnotationDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationRecord extends ListGridRecord {

    public AnnotationRecord() {
    }

    public AnnotationRecord(Long id, String code, String title, String type, String url, String text, AnnotationDto annotationDto) {
        setId(id);
        setCode(code);
        setTitle(title);
        setType(type);
        setUrl(url);
        setText(text);
        setAnnotationDto(annotationDto);
    }

    public void setId(Long id) {
        setAttribute(AnnotationDS.ID, id);
    }

    public Long getId() {
        return getAttributeAsLong(AnnotationDS.ID);
    }

    public void setCode(String value) {
        setAttribute(AnnotationDS.CODE, value);
    }

    public String getCode() {
        return getAttributeAsString(AnnotationDS.CODE);
    }

    public void setTitle(String value) {
        setAttribute(AnnotationDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(AnnotationDS.TITLE);
    }

    public void setType(String value) {
        setAttribute(AnnotationDS.TYPE, value);
    }

    public String getType() {
        return getAttributeAsString(AnnotationDS.TYPE);
    }

    public void setUrl(String value) {
        setAttribute(AnnotationDS.URL, value);
    }

    public String getUrl() {
        return getAttributeAsString(AnnotationDS.URL);
    }

    public void setText(String value) {
        setAttribute(AnnotationDS.TEXT, value);
    }

    public String getText() {
        return getAttributeAsString(AnnotationDS.TEXT);
    }

    public void setAnnotationDto(AnnotationDto value) {
        setAttribute(AnnotationDS.ANNOTATION_DTO, value);
    }

    public AnnotationDto getAnnotationDto() {
        return (AnnotationDto) getAttributeAsObject(AnnotationDS.ANNOTATION_DTO);
    }

}
