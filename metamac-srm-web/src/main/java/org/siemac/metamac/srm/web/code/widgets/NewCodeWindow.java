package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewCodeWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;

    private static final String FIELD_SAVE             = "save-con";

    private CustomDynamicForm   form;

    public NewCodeWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(CodeDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getCodeIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(CodeDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().codeCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public CodeMetamacDto getNewCodeDto() {
        CodeMetamacDto codeDto = new CodeMetamacDto();
        codeDto.setCode(form.getValueAsString(CodeDS.CODE));
        codeDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(CodeDS.NAME)));
        return codeDto;
    }

    public boolean validateForm() {
        return form.validate(false);
    }
}
