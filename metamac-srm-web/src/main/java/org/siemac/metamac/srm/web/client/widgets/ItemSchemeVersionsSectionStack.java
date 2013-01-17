package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.ItemSchemeDS;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;

import com.smartgwt.client.widgets.grid.ListGridField;

public class ItemSchemeVersionsSectionStack extends CustomSectionStack {

    public ItemSchemeVersionsSectionStack(String title) {
        super(new BaseCustomListGrid(), title);
        setStyleName("versionSectionStackStyle");

        // Add fields to listGrid
        ListGridField codeField = new ListGridField(ItemSchemeDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        ListGridField nameField = new ListGridField(ItemSchemeDS.NAME, getConstants().nameableArtefactName());
        ListGridField versionField = new ListGridField(ItemSchemeDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());
        versionField.setWidth("15%");
        listGrid.setFields(codeField, nameField, versionField);

        // Add listGrid to sectionStack
        section.setItems(listGrid);
    }
}
