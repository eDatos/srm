package org.siemac.metamac.srm.web.client.model.record;

import java.util.Date;

import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DsdRecord extends ListGridRecord {

    public static final String IDENTIFIER      = "identifier";
    public static final String CODE            = "dsd-code";
    public static final String VERSION         = "version";
    public static final String AGENCY          = "agency";
    public static final String NAME            = "name";
    public static final String DESCRIPTION     = "description";
    public static final String URI             = "uri";
    public static final String URN             = "urn";
    public static final String FINAL_STRUCTURE = "finalStructure";
    public static final String START_DATE      = "startDate";
    public static final String END_DATE        = "endDate";
    public static final String DSD             = "dsd";

    public DsdRecord() {
    }

    // Last modified record
    public DsdRecord(Long id, String code, String name, String description, Boolean finalStructure, DataStructureDefinitionDto dsd) {
        setIdentifier(id);
        setCode(code);
        setName(name);
        setDescription(description);
        setFinalStructure(finalStructure);
        setDsd(dsd);
    }

    // Complete record
    public DsdRecord(Long identifier, String code, String version, String agency, String name, String description, String uri, String urn, Boolean finalStructure, Date startDate, Date endDate) {
        setIdentifier(identifier);
        setCode(code);
        setVersion(version);
        setAgency(agency);
        setName(name);
        setDescription(description);
        setUri(uri);
        setUrn(urn);
        setFinalStructure(finalStructure);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public void setIdentifier(Long attribute) {
        setAttribute(IDENTIFIER, attribute);
    }

    public void setCode(String attribute) {
        setAttribute(CODE, attribute);
    }

    public void setVersion(String attribute) {
        setAttribute(VERSION, attribute);
    }

    public void setAgency(String attribute) {
        setAttribute(AGENCY, attribute);
    }

    public void setName(String attribute) {
        setAttribute(NAME, attribute);
    }

    public void setDescription(String attribute) {
        setAttribute(DESCRIPTION, attribute);
    }

    public void setUri(String attribute) {
        setAttribute(URI, attribute);
    }

    public void setUrn(String attribute) {
        setAttribute(URN, attribute);
    }

    public void setFinalStructure(Boolean attribute) {
        setAttribute(FINAL_STRUCTURE, attribute);
    }

    public void setStartDate(Date startDate) {
        setAttribute(START_DATE, startDate);
    }

    public void setEndDate(Date endDate) {
        setAttribute(END_DATE, endDate);
    }

    public void setDsd(DataStructureDefinitionDto value) {
        setAttribute(DSD, value);
    }

    public Long getIdentifier() {
        return getAttributeAsLong(IDENTIFIER);
    }

    public String getCode() {
        return getAttributeAsString(CODE);
    }

    public String getVersion() {
        return getAttributeAsString(VERSION);
    }

    public String getAgency() {
        return getAttributeAsString(AGENCY);
    }

    public String getName() {
        return getAttributeAsString(NAME);
    }

    public String getDescription() {
        return getAttributeAsString(DESCRIPTION);
    }

    public String getUri() {
        return getAttributeAsString(URI);
    }

    public String getUrn() {
        return getAttributeAsString(URN);
    }

    public Boolean isFinalStructure() {
        return getAttributeAsBoolean(FINAL_STRUCTURE);
    }

    public Date getStartDate() {
        return getAttributeAsDate(START_DATE);
    }

    public Date getEndDate() {
        return getAttributeAsDate(END_DATE);
    }

    public DataStructureDefinitionDto getDsd() {
        return (DataStructureDefinitionDto) getAttributeAsObject(DSD);
    }

}
