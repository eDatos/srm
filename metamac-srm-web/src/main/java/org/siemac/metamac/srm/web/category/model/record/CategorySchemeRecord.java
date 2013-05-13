package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;

public class CategorySchemeRecord extends ItemSchemeRecord {

    public CategorySchemeRecord() {
    }

    public void setCategorySchemeBasicDto(CategorySchemeMetamacBasicDto categorySchemeMetamacDto) {
        setAttribute(CategorySchemeDS.DTO, categorySchemeMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CategorySchemeMetamacBasicDto) getAttributeAsObject(CategorySchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public CategorySchemeMetamacBasicDto getCategorySchemeBasicDto() {
        return (CategorySchemeMetamacBasicDto) getAttributeAsObject(CategorySchemeDS.DTO);
    }
}
