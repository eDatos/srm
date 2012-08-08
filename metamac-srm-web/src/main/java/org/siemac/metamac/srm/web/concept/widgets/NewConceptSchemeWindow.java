package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.concept.dto.MetamacConceptSchemeDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewConceptSchemeWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;

    private static final String FIELD_SAVE             = "save-sch";

    private CustomDynamicForm   form;

    public NewConceptSchemeWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(ConceptSchemeDS.CODE, getConstants().conceptSchemeCode());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredSelectItem type = new RequiredSelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        type.setValueMap(CommonUtils.getConceptSchemeTypeHashMap());
        type.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        SelectItem operation = new SelectItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().conceptSchemeCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, type, operation, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public MetamacConceptSchemeDto getNewConceptSchemeDto() {
        MetamacConceptSchemeDto conceptSchemeDto = new MetamacConceptSchemeDto();

        conceptSchemeDto.setCode(form.getValueAsString(ConceptSchemeDS.CODE));
        conceptSchemeDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(ConceptSchemeDS.NAME)));
        return conceptSchemeDto;
    }

    public boolean validateForm() {
        return form.validate();
    }

}
