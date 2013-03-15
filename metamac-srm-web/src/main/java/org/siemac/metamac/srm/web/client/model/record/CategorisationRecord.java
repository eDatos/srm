package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.srm.web.client.model.ds.CategorisationDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CategorisationRecord extends ListGridRecord {

    public CategorisationRecord(Long id, String code, String name, String category, String urn, RelatedResourceDto maintainer) {
        setId(id);
        setCode(code);
        setName(name);
        setCategory(category);
        setUrn(urn);
        setMaintainer(maintainer);
    }

    public void setId(Long id) {
        setAttribute(CategorisationDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(CategorisationDS.NAME, name);
    }

    public void setCode(String code) {
        setAttribute(CategorisationDS.CODE, code);
    }

    public void setUrn(String value) {
        setAttribute(CategorisationDS.URN, value);
    }

    public void setCategory(String value) {
        setAttribute(CategorisationDS.CATEGORY, value);
    }

    public Long getId() {
        return getAttributeAsLong(CategorisationDS.ID);
    }

    public String getCode() {
        return getAttribute(CategorisationDS.CODE);
    }

    public String getName() {
        return getAttribute(CategorisationDS.NAME);
    }

    public String getUrn() {
        return getAttributeAsString(CategorisationDS.URN);
    }

    public void setMaintainer(RelatedResourceDto maintainer) {
        setAttribute(CategorisationDS.MAINTAINER, maintainer);
    }

    public RelatedResourceDto getMaintainer() {
        return (RelatedResourceDto) getAttributeAsObject(CategorisationDS.MAINTAINER);
    }
}
