package org.siemac.metamac.srm.web.dsd.model.record;

import org.siemac.metamac.core.common.dto.ExternalItemDto;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * This class should be removed!!
 */
public class ConceptRecord extends ListGridRecord {

    public static final String IDENTIFIER      = "identifier";
    public static final String SCHEME          = "scheme";
    public static final String CONCEPT_CODE_ID = "code-id";
    public static final String CONCEPT         = "concept";

    public ConceptRecord() {
    }

    public ConceptRecord(String id, String scheme, String codeId, ExternalItemDto concept) {
        setIdentifier(id);
        setScheme(scheme);
        setCodeId(codeId);
        setConcept(concept);
    }

    public void setIdentifier(String value) {
        setAttribute(IDENTIFIER, value);
    }

    public void setScheme(String value) {
        setAttribute(SCHEME, value);
    }

    public void setCodeId(String value) {
        setAttribute(CONCEPT_CODE_ID, value);
    }

    public void setConcept(ExternalItemDto value) {
        setAttribute(CONCEPT, value);
    }

    public String getIdentifier() {
        return getAttributeAsString(IDENTIFIER);
    }

    public String getScheme() {
        return getAttributeAsString(SCHEME);
    }

    public String getCodeId() {
        return getAttributeAsString(CONCEPT_CODE_ID);
    }

    public ExternalItemDto getConcept() {
        return (ExternalItemDto) getAttributeAsObject(CONCEPT);
    }

}
