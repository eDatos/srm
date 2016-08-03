package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class VersionWindow extends CustomWindow {

    private static final String FIELD_VERSION = "version-scheme";
    private static final String FIELD_ACCEPT  = "accept-item";

    private CustomDynamicForm   form;

    public VersionWindow(String title) {
        super(title);
        setWidth(330);
        setHeight(170);

        InformationLabel informationLabel = new InformationLabel(getConstants().lifeCycleVersionInfoMessage());
        informationLabel.setWidth100();
        informationLabel.setMargin(10);

        SelectItem versionItem = new SelectItem(FIELD_VERSION, getConstants().lifeCycleVersionType());
        versionItem.setRequired(true);
        versionItem.setValueMap(CommonUtils.getVersionTypeHashMap());

        CustomButtonItem acceptButtonItem = new CustomButtonItem(FIELD_ACCEPT, MetamacWebCommon.getConstants().accept());

        form = new CustomDynamicForm();
        form.setFields(versionItem, acceptButtonItem);
        form.setMargin(10);
        form.setAlign(Alignment.CENTER);

        addItem(informationLabel);
        addItem(form);
        show();
    }

    public VersionTypeEnum getSelectedVersion() {
        String value = form.getValueAsString(FIELD_VERSION);
        return (value != null && !value.isEmpty()) ? VersionTypeEnum.valueOf(value) : null;
    }

    public boolean validateForm() {
        return form.validate();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_ACCEPT);
    }
}
