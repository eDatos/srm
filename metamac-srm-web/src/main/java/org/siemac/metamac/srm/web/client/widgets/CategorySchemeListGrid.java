package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.category.utils.RecordUtils;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

public class CategorySchemeListGrid extends BaseCustomListGrid {

    public CategorySchemeListGrid() {
        this.setFields(ResourceFieldUtils.getCategorySchemeListGridFields());
    }

    public void setCategorySchemes(List<CategorySchemeMetamacBasicDto> categorySchemeDtos) {
        removeAllData();
        if (categorySchemeDtos != null) {
            CategorySchemeRecord[] schemeRecords = new CategorySchemeRecord[categorySchemeDtos.size()];
            for (int i = 0; i < categorySchemeDtos.size(); i++) {
                schemeRecords[i] = RecordUtils.getCategorySchemeRecord(categorySchemeDtos.get(i));
            }
            this.setData(schemeRecords);
        }
    }
}
