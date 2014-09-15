package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.ItemSchemeDS;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridSectionStack;

import com.smartgwt.client.widgets.grid.ListGridField;

public class ItemSchemeVersionsSectionStack extends CustomListGridSectionStack {

    public ItemSchemeVersionsSectionStack(String title) {
        super(new BaseCustomListGrid(), title, "versionSectionStackStyle");

        // Add fields to listGrid
        ListGridField codeField = new ListGridField(ItemSchemeDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        ListGridField nameField = new ListGridField(ItemSchemeDS.NAME, getConstants().nameableArtefactName());
        ListGridField procStatusField = new ListGridField(ItemSchemeDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        ListGridField versionField = new ListGridField(ItemSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        versionField.setWidth("15%");
        listGrid.setFields(codeField, nameField, procStatusField, versionField);

        // Add listGrid to sectionStack
        defaultSection.setItems(listGrid);
    }
}
