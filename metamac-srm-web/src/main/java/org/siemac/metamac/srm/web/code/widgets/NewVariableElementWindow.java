package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;

public class NewVariableElementWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String FIELD_SAVE             = "save-ele";

    private CustomDynamicForm   form;

    public NewVariableElementWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(VariableElementDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getVariableElementIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem shortNameItem = new RequiredTextItem(VariableElementDS.SHORT_NAME, getConstants().variableElementShortName());
        shortNameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
        lengthRangeValidator.setMin(0);
        lengthRangeValidator.setMax(SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        shortNameItem.setValidators(lengthRangeValidator);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().variableElementCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, shortNameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public VariableElementDto getNewVariableElementDto() {
        VariableElementDto variableDto = new VariableElementDto();
        variableDto.setCode(form.getValueAsString(VariableElementDS.CODE));
        variableDto.setShortName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(VariableElementDS.SHORT_NAME)));
        return variableDto;
    }
}
