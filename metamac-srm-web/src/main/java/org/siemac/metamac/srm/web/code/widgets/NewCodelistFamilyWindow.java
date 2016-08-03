package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodelistFamilyDS;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewCodelistFamilyWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String FIELD_SAVE             = "save-sch";

    private CustomDynamicForm   form;

    public NewCodelistFamilyWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(CodelistFamilyDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getCodelistFamilyIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(CodelistFamilyDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().codelistFamilyCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public CodelistFamilyDto getNewCodelistFamilyDto() {
        CodelistFamilyDto codelistFamilyDto = new CodelistFamilyDto();
        codelistFamilyDto.setCode(form.getValueAsString(CodelistFamilyDS.CODE));
        codelistFamilyDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(CodelistFamilyDS.NAME)));
        return codelistFamilyDto;
    }
}
