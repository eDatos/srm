package org.siemac.metamac.internal.web.dsd.model.record;

import java.util.Date;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class OperationRecord extends ListGridRecord {

    public static final String IDENTIFIER  = "identifier";
    public static final String NAME        = "name";
    public static final String ACRONYM     = "acronym";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE  = "startDate";
    public static final String END_DATE    = "endDate";

    public OperationRecord() {
    }

    public OperationRecord(String identifier, String name, String acronym, String description, Date startDate, Date endDate) {
        setIdentifier(identifier);
        setName(name);
        setAcronym(acronym);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public void setIdentifier(String attribute) {
        setAttribute(IDENTIFIER, attribute);
    }

    public void setName(String attribute) {
        setAttribute(NAME, attribute);
    }

    public void setAcronym(String attribute) {
        setAttribute(ACRONYM, attribute);
    }

    public void setDescription(String attribute) {
        setAttribute(DESCRIPTION, attribute);
    }

    public void setStartDate(Date startDate) {
        setAttribute(START_DATE, startDate);
    }

    public void setEndDate(Date endDate) {
        setAttribute(END_DATE, endDate);
    }

    public String getIdentifier() {
        return getAttributeAsString(IDENTIFIER);
    }

    public String getName() {
        return getAttributeAsString(NAME);
    }

    public String getDescription() {
        return getAttributeAsString(DESCRIPTION);
    }

    public String getAcronym() {
        return getAttributeAsString(ACRONYM);
    }

    public Date getStartDate() {
        return getAttributeAsDate(START_DATE);
    }

    public Date getEndDate() {
        return getAttributeAsDate(END_DATE);
    }
}
