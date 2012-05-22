package org.siemac.metamac.internal.web.concept.widgets;

import static org.siemac.metamac.internal.web.client.MetamacInternalWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.domain.concept.dto.ConceptSchemeDto;
import org.siemac.metamac.internal.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewConceptSchemeWindow extends CustomWindow {

    private static final String FIELD_SAVE = "save-sch";

    private CustomDynamicForm   form;


    public NewConceptSchemeWindow(String title) {
        super(title);
        setHeight(150);
        setWidth(330);

        RequiredTextItem codeItem = new RequiredTextItem(ConceptSchemeDS.ID_LOGIC, getConstants().conceptSchemeIdLogic());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        RequiredTextItem nameItem = new RequiredTextItem(ConceptSchemeDS.NAME, getConstants().conceptSchemeName());
        ButtonItem saveItem = new ButtonItem(FIELD_SAVE, getConstants().conceptSchemeCreate());
        saveItem.setAlign(Alignment.RIGHT);

        form = new CustomDynamicForm();
        form.setFields(codeItem, nameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public ConceptSchemeDto getNewConceptSchemeDto() {
        ConceptSchemeDto conceptSchemeDto = new ConceptSchemeDto();
        
        conceptSchemeDto.setIdLogic(form.getValueAsString(ConceptSchemeDS.ID_LOGIC));
        conceptSchemeDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(ConceptSchemeDS.NAME)));
        return conceptSchemeDto;
    }

    public boolean validateForm() {
        return form.validate();
    }

}
