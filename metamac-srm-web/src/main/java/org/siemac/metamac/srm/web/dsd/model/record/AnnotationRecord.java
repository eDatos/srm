package org.siemac.metamac.srm.web.dsd.model.record;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationRecord extends ListGridRecord {

    public static final String CODE           = "an-id";
    public static final String TITLE          = "an-title";
    public static final String TYPE           = "an-type";
    public static final String URL            = "an-url";
    public static final String TEXT           = "an-text";
    public static final String ANNOTATION_DTO = "an-annotation";

    public AnnotationRecord() {
    }

    public AnnotationRecord(String code, String title, String type, String url, String text, AnnotationDto annotationDto) {
        setCode(code);
        setTitle(title);
        setType(type);
        setUrl(url);
        setText(text);
        setAnnotationDto(annotationDto);
    }

    public void setCode(String value) {
        setAttribute(CODE, value);
    }

    public String getIdLogic() {
        return getAttributeAsString(CODE);
    }

    public void setTitle(String value) {
        setAttribute(TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(TITLE);
    }

    public void setType(String value) {
        setAttribute(TYPE, value);
    }

    public String getType() {
        return getAttributeAsString(TYPE);
    }

    public void setUrl(String value) {
        setAttribute(URL, value);
    }

    public String getUrl() {
        return getAttributeAsString(URL);
    }

    public void setText(String value) {
        setAttribute(TEXT, value);
    }

    public String getText() {
        return getAttributeAsString(TEXT);
    }

    public void setAnnotationDto(AnnotationDto value) {
        setAttribute(ANNOTATION_DTO, value);
    }

    public AnnotationDto getAnnotationDto() {
        return (AnnotationDto) getAttributeAsObject(ANNOTATION_DTO);
    }

}
