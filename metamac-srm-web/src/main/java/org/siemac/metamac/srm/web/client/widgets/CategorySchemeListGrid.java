package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.category.utils.CategoriesRecordUtils;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;

public class CategorySchemeListGrid extends NavigableListGrid {

    public CategorySchemeListGrid() {
        this.setFields(ResourceFieldUtils.getCategorySchemeListGridFields());
    }

    public void setCategorySchemes(List<CategorySchemeMetamacBasicDto> categorySchemeDtos) {
        removeAllData();
        if (categorySchemeDtos != null) {
            CategorySchemeRecord[] schemeRecords = new CategorySchemeRecord[categorySchemeDtos.size()];
            for (int i = 0; i < categorySchemeDtos.size(); i++) {
                schemeRecords[i] = CategoriesRecordUtils.getCategorySchemeRecord(categorySchemeDtos.get(i));
            }
            this.setData(schemeRecords);
        }
    }
}
