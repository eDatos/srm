package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.ItemSchemeDS;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ItemSchemeListGrid extends BaseCustomListGrid {

    public ItemSchemeListGrid() {
        super();

        this.setShowAllRecords(true);

        ListGridField codeField = new ListGridField(ItemSchemeDS.CODE, getConstants().identifiableArtefactCode());
        ListGridField nameField = new ListGridField(ItemSchemeDS.NAME, getConstants().nameableArtefactName());
        ListGridField procStatusField = new ListGridField(ItemSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());

        // ToolTip
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ItemSchemeRecord schemeRecord = (ItemSchemeRecord) record;
                return schemeRecord.getCode();
            }
        });
        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ItemSchemeRecord schemeRecord = (ItemSchemeRecord) record;
                return schemeRecord.getName();
            }
        });
        this.setFields(codeField, nameField, procStatusField);
    }

}
