package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.web.code.widgets.CodesOpennessLevelEditableTreeGrid;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.layout.VLayout;

public class EditCodelistOpennessLevelWindow extends CustomWindow {

    private static final String                FIELD_SAVE = "save-ex";

    private CustomDynamicForm                  form;

    private CodesOpennessLevelEditableTreeGrid codesOpennessLevelEditableTreeGrid;

    public EditCodelistOpennessLevelWindow(String title) {
        super(title);
        setAutoSize(true);

        int width = 650;

        VLayout vLayout = new VLayout();
        vLayout.setWidth(width);
        vLayout.setMargin(15);

        codesOpennessLevelEditableTreeGrid = new CodesOpennessLevelEditableTreeGrid();
        codesOpennessLevelEditableTreeGrid.setWidth(width);
        vLayout.addMember(codesOpennessLevelEditableTreeGrid);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        form = new CustomDynamicForm();
        form.setWidth(width);
        form.setMargin(5);
        form.setFields(saveItem);
        vLayout.addMember(form);

        addItem(vLayout);
        show();
    }

    public CodesOpennessLevelEditableTreeGrid getCodesOpennessLevelEditableTreeGrid() {
        return codesOpennessLevelEditableTreeGrid;
    }

    public HasClickHandlers getSaveClickHandlers() {
        return form.getItem(FIELD_SAVE);
    }
}
