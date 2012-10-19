package org.siemac.metamac.srm.web.client.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;

public class CategorySchemeRecord extends ItemSchemeRecord {

    public CategorySchemeRecord(Long id, String code, String name, String status, String versionLogic, String urn, CategorySchemeMetamacDto categorySchemeMetamacDto) {
        super(id, code, name, status, versionLogic, urn);
        setCategorySchemeDto(categorySchemeMetamacDto);
    }

    public void setCategorySchemeDto(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        setAttribute(CategorySchemeDS.DTO, categorySchemeMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CategorySchemeMetamacDto) getAttributeAsObject(CategorySchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public CategorySchemeMetamacDto getCategorySchemeDto() {
        return (CategorySchemeMetamacDto) getAttributeAsObject(CategorySchemeDS.DTO);
    }

}
