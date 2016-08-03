package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewCategorySchemeWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String FIELD_SAVE             = "save-sch";

    private CustomDynamicForm   form;

    public NewCategorySchemeWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(CategorySchemeDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getCategorySchemeIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(CategorySchemeDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().categorySchemeCreate());

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

    public CategorySchemeMetamacDto getNewCategorySchemeDto() {
        CategorySchemeMetamacDto categorySchemeDto = new CategorySchemeMetamacDto();
        categorySchemeDto.setMaintainer(RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto());
        categorySchemeDto.setCode(form.getValueAsString(CategorySchemeDS.CODE));
        categorySchemeDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(CategorySchemeDS.NAME)));
        categorySchemeDto.setIsPartial(false);
        return categorySchemeDto;
    }
}
