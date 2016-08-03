package org.siemac.metamac.srm.web.client.widgets;

import java.util.Date;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class DateWindow extends CustomWindow {

    private static final String FIELD_DATE   = "end-date";
    private static final String FIELD_ACCEPT = "accept-item";

    private CustomDynamicForm   form;

    public DateWindow(String title) {
        super(title);
        setHeight(120);
        setWidth(330);

        CustomDateItem dateItem = new CustomDateItem(FIELD_DATE, MetamacSrmWeb.getConstants().maintainableArtefactValidTo());
        dateItem.setRequired(true);

        CustomButtonItem acceptButtonItem = new CustomButtonItem(FIELD_ACCEPT, MetamacWebCommon.getConstants().accept());

        form = new CustomDynamicForm();
        form.setFields(dateItem, acceptButtonItem);

        addItem(form);
        show();
    }

    public Date getSelectedDate() {
        return (Date) form.getValue(FIELD_DATE);
    }

    public boolean validateForm() {
        return form.validate();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_ACCEPT);
    }
}
