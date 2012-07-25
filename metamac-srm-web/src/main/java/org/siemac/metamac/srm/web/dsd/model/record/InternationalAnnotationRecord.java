package org.siemac.metamac.srm.web.dsd.model.record;

import org.siemac.metamac.domain.srm.dto.AnnotationDto;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class InternationalAnnotationRecord extends ListGridRecord {

    public static final String ID             = "ident";
    public static final String ID_DS          = "id-ds";     // DataSource primary key
    public static final String CODE           = "code";
    public static final String TITLE          = "title";
    public static final String TYPE           = "type";
    public static final String URL            = "url";
    public static final String TEXT           = "text";
    public static final String LOCALE         = "locale";
    public static final String FLAG           = "flag";
    public static final String ANNOTATION_DTO = "annotation";

    public static final String REMOVE_FIELD   = "remove";

    public InternationalAnnotationRecord() {
    }

    public InternationalAnnotationRecord(String id, String idDs, String idLogic, String title, String type, String url, String text, String locale, String flag, AnnotationDto annotationDto) {
        setId(id);
        setIdDs(idDs);
        setCode(idLogic);
        setTitle(title);
        setType(type);
        setUrl(url);
        setText(text);
        setLocale(locale);
        setFlag(flag);
        setAnnotationDto(annotationDto);
    }

    public void setId(String value) {
        setAttribute(ID, value);
    }

    public String getId() {
        return getAttributeAsString(ID);
    }

    public void setIdDs(String value) {
        setAttribute(ID_DS, value);
    }

    public String getIdDs() {
        return getAttributeAsString(ID_DS);
    }

    public void setCode(String value) {
        setAttribute(CODE, value);
    }

    public String getCode() {
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

    public void setLocale(String value) {
        setAttribute(LOCALE, value);
    }

    public String getLocale() {
        return getAttributeAsString(LOCALE);
    }

    public String getFlag() {
        return getAttributeAsString(FLAG);
    }

    public void setFlag(String value) {
        setAttribute(FLAG, value);
    }

    public void setAnnotationDto(AnnotationDto value) {
        setAttribute(ANNOTATION_DTO, value);
    }

    public AnnotationDto getAnnotationDto() {
        return (AnnotationDto) getAttributeAsObject(ANNOTATION_DTO);
    }

    public String getRemoveField() {
        return getAttributeAsString(REMOVE_FIELD);
    }

    public void setRemoveField(String value) {
        setAttribute(REMOVE_FIELD, value);
    }

}
