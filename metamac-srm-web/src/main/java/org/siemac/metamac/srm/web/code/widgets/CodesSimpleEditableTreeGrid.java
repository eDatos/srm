package org.siemac.metamac.srm.web.code.widgets;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;

public class CodesSimpleEditableTreeGrid extends BaseCodesSimpleTreeGrid {

    protected Tree          tree;

    public CodesSimpleEditableTreeGrid() {
        super();

    }

    @Override
    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
        // TODO
        return super.getCellCSSText(record, rowNum, colNum);
    }
}
