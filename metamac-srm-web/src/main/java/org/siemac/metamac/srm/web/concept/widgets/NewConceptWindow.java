package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewConceptWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;

    private static final String FIELD_SAVE             = "save-con";

    private CustomDynamicForm   form;

    public NewConceptWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(ConceptDS.CODE, getConstants().conceptCode());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(ConceptDS.NAME, getConstants().conceptName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().conceptCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public MetamacConceptDto getNewConceptDto() {
        MetamacConceptDto conceptDto = new MetamacConceptDto();
        conceptDto.setCode(form.getValueAsString(ConceptDS.CODE));
        conceptDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(ConceptDS.NAME)));
        return conceptDto;
    }

    public boolean validateForm() {
        return form.validate();
    }

}
