package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.client.model.record.ItemRecord;

public class CategoryRecord extends ItemRecord {

    public CategoryRecord() {
    }

    public void setCategoryBasicDto(CategoryMetamacBasicDto categoryDto) {
        setAttribute(CategoryDS.DTO, categoryDto);
    }

    public CategoryMetamacBasicDto getCategoryBasicDto() {
        return (CategoryMetamacBasicDto) getAttributeAsObject(CategoryDS.DTO);
    }
}
