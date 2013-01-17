package org.siemac.metamac.srm.web.client.category.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.client.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.category.utils.RecordUtils;
import org.siemac.metamac.srm.web.client.widgets.ItemSchemeVersionsSectionStack;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;

public class CategorySchemeVersionsSectionStack extends ItemSchemeVersionsSectionStack {

    public CategorySchemeVersionsSectionStack(String title) {
        super(title);
    }

    public void setCategorySchemes(List<CategorySchemeMetamacDto> categorySchemeDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (CategorySchemeMetamacDto categorySchemeDto : categorySchemeDtos) {
            listGrid.addData(RecordUtils.getCategorySchemeRecord(categorySchemeDto));
        }
    }

    public void selectCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(CategorySchemeDS.URN, categorySchemeMetamacDto.getUrn());
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }
}
