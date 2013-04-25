package org.siemac.metamac.srm.web.client.model.record;

import java.util.Date;

import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DsdRecord extends ListGridRecord {

    public DsdRecord() {
    }

    public DsdRecord(Long id, String code, String name, String urn, String description, Boolean finalStructure, String procStatus, String version) {
        setId(id);
        setCode(code);
        setName(name);
        setDescription(description);
        setFinalStructure(finalStructure);
        setUrn(urn);
        setProcStatus(procStatus);
        setVersion(version);
    }

    public void setId(Long attribute) {
        setAttribute(DataStructureDefinitionDS.ID, attribute);
    }

    public void setCode(String attribute) {
        setAttribute(DataStructureDefinitionDS.CODE, attribute);
    }

    public void setVersion(String attribute) {
        setAttribute(DataStructureDefinitionDS.VERSION_LOGIC, attribute);
    }

    public void setMaintainer(String attribute) {
        setAttribute(DataStructureDefinitionDS.MAINTAINER, attribute);
    }

    public void setName(String attribute) {
        setAttribute(DataStructureDefinitionDS.NAME, attribute);
    }

    public void setDescription(String attribute) {
        setAttribute(DataStructureDefinitionDS.DESCRIPTION, attribute);
    }

    public void setUrn(String attribute) {
        setAttribute(DataStructureDefinitionDS.URN, attribute);
    }

    public void setFinalStructure(Boolean attribute) {
        setAttribute(DataStructureDefinitionDS.FINAL, attribute);
    }

    public void setProcStatus(String procStatus) {
        setAttribute(DataStructureDefinitionDS.PROC_STATUS, procStatus);
    }

    public void setStartDate(Date startDate) {
        setAttribute(DataStructureDefinitionDS.VALID_FROM, startDate);
    }

    public void setEndDate(Date endDate) {
        setAttribute(DataStructureDefinitionDS.VALID_TO, endDate);
    }

    public void setDsdBasicDto(DataStructureDefinitionMetamacBasicDto value) {
        setAttribute(DataStructureDefinitionDS.DTO, value);
    }

    public Long getId() {
        return getAttributeAsLong(DataStructureDefinitionDS.ID);
    }

    public String getCode() {
        return getAttributeAsString(DataStructureDefinitionDS.CODE);
    }

    public String getVersion() {
        return getAttributeAsString(DataStructureDefinitionDS.VERSION_LOGIC);
    }

    public String getAgency() {
        return getAttributeAsString(DataStructureDefinitionDS.MAINTAINER);
    }

    public String getName() {
        return getAttributeAsString(DataStructureDefinitionDS.NAME);
    }

    public String getDescription() {
        return getAttributeAsString(DataStructureDefinitionDS.DESCRIPTION);
    }

    public String getUrn() {
        return getAttributeAsString(DataStructureDefinitionDS.URN);
    }

    public Boolean isFinalStructure() {
        return getAttributeAsBoolean(DataStructureDefinitionDS.FINAL);
    }

    public Date getStartDate() {
        return getAttributeAsDate(DataStructureDefinitionDS.VALID_FROM);
    }

    public Date getEndDate() {
        return getAttributeAsDate(DataStructureDefinitionDS.VALID_TO);
    }

    public DataStructureDefinitionMetamacBasicDto getDsdBasicDto() {
        return (DataStructureDefinitionMetamacBasicDto) getAttributeAsObject(DataStructureDefinitionDS.DTO);
    }
}
