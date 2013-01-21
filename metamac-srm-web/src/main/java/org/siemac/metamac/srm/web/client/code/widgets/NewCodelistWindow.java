package org.siemac.metamac.srm.web.client.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewCodelistWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String FIELD_SAVE             = "save-sch";

    private CustomDynamicForm   form;

    public NewCodelistWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(CodelistDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().codelistCreate());

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

    public CodelistMetamacDto getNewCodelistDto() {
        CodelistMetamacDto codelistDto = new CodelistMetamacDto();
        codelistDto.setMaintainer(RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto());
        codelistDto.setCode(form.getValueAsString(CodelistDS.CODE));
        codelistDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(CodelistDS.NAME)));
        return codelistDto;
    }
}
