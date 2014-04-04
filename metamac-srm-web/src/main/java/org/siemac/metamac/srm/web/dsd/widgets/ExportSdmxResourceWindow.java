package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public abstract class ExportSdmxResourceWindow extends CustomWindow {

    private static final String EXPORT_BUTTON_ID        = "export-resource";
    private static final String INFO_AMOUNT_ITEM_ID     = "info-amount";
    private static final String INFO_REFERENCES_ITEM_ID = "references";

    private static final int    FORM_ITEM_CUSTOM_WIDTH  = 400;

    private CustomDynamicForm   form;

    public ExportSdmxResourceWindow() {
        super(getConstants().exportResources());
        setAutoSize(true);

        RequiredSelectItem infoAmountItem = new RequiredSelectItem(INFO_AMOUNT_ITEM_ID, getConstants().exportResourcesInfoAmount());
        infoAmountItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        infoAmountItem.setValueMap(CommonUtils.getStructureParameterDetailTypeHashMap());

        RequiredSelectItem referencesItem = new RequiredSelectItem(INFO_REFERENCES_ITEM_ID, getConstants().exportResourcesReferences());
        referencesItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        referencesItem.setValueMap(CommonUtils.getStructureParameterReferencesTypeHashMap());

        CustomButtonItem exportItem = new CustomButtonItem(EXPORT_BUTTON_ID, getConstants().download());
        exportItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (form.validate(false)) {
                    buildFormValuesAndStartExportation();
                }
            }
        });

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(infoAmountItem, referencesItem, exportItem);

        addItem(form);

        show();
    }

    protected void buildFormValuesAndStartExportation() {
        String infoAmountKey = form.getValueAsString(INFO_AMOUNT_ITEM_ID);
        ExportDetailEnum infoAmount = ExportDetailEnum.valueOf(infoAmountKey);

        String referencesKey = form.getValueAsString(INFO_REFERENCES_ITEM_ID);
        ExportReferencesEnum infoReferences = ExportReferencesEnum.valueOf(referencesKey);

        startExportation(infoAmount, infoReferences);
        hide();
    }

    protected abstract void startExportation(ExportDetailEnum detail, ExportReferencesEnum references);

}
