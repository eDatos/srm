package org.siemac.metamac.srm.web.category.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.category.utils.CategoriesRecordUtils;
import org.siemac.metamac.srm.web.client.widgets.ItemSchemeVersionsSectionStack;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;

public class CategorySchemeVersionsSectionStack extends ItemSchemeVersionsSectionStack {

    public CategorySchemeVersionsSectionStack(String title) {
        super(title);
    }

    public void setCategorySchemes(List<CategorySchemeMetamacBasicDto> categorySchemeDtos) {
        listGrid.selectAllRecords();
        listGrid.removeSelectedData();
        for (CategorySchemeMetamacBasicDto categorySchemeDto : categorySchemeDtos) {
            listGrid.addData(CategoriesRecordUtils.getCategorySchemeRecord(categorySchemeDto));
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
