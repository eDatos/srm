package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.client.category.utils.RecordUtils;

public class CategorySchemeListGrid extends ItemSchemeListGrid {

    public CategorySchemeListGrid() {

    }

    public void setCategorySchemes(List<CategorySchemeMetamacDto> categorySchemeDtos) {
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
