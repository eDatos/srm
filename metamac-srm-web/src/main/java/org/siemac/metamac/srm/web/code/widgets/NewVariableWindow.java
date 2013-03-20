package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedDragAndDropItem;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.code.presenter.VariableListPresenter;
import org.siemac.metamac.srm.web.code.view.handlers.VariableListUiHandlers;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

public class NewVariableWindow extends CustomWindow {

    private static final String    FORM_ITEM_CUSTOM_WIDTH = FormItemUtils.FORM_ITEM_WIDTH;
    private static final String    FIELD_SAVE             = "save-var";

    private CustomDynamicForm      form;

    private VariableListUiHandlers uiHandlers;

    public NewVariableWindow(String title, VariableListUiHandlers uiHandlers) {
        super(title);
        setAutoSize(true);

        this.uiHandlers = uiHandlers;

        RequiredTextItem codeItem = new RequiredTextItem(VariableDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getVariableIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(VariableDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem shortNameItem = new RequiredTextItem(VariableDS.SHORT_NAME, getConstants().variableShortName());
        shortNameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        SearchRelatedResourcePaginatedDragAndDropItem familyItem = createFamilyItem();
        familyItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().variableCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, shortNameItem, familyItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public VariableDto getNewVariableDto() {
        VariableDto variableDto = new VariableDto();
        variableDto.setCode(form.getValueAsString(VariableDS.CODE));
        variableDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(VariableDS.NAME)));
        variableDto.setShortName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(VariableDS.SHORT_NAME)));
        variableDto.getFamilies().addAll(((SearchRelatedResourcePaginatedDragAndDropItem) form.getItem(VariableDS.FAMILIES)).getSelectedRelatedResources());
        return variableDto;
    }

    public void setUiHandlers(VariableListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setVariableFamilies(List<RelatedResourceDto> families, int firstResult, int totalResults) {
        familyItem.setSourceRelatedResources(families);
        familyItem.refreshSourcePaginationInfo(firstResult, families.size(), totalResults);
    }

    private SearchRelatedResourcePaginatedDragAndDropItem familyItem;

    private SearchRelatedResourcePaginatedDragAndDropItem createFamilyItem() {
        familyItem = new SearchRelatedResourcePaginatedDragAndDropItem(VariableDS.FAMILIES, getConstants().variableFamily(), VariableListPresenter.FAMILY_LIST_MAX_RESULTS, FORM_ITEM_CUSTOM_WIDTH,
                new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        uiHandlers.retrieveVariableFamilies(firstResult, maxResults, familyItem.getRelatedResourceCriteria());
                    }
                });

        familyItem.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                uiHandlers.retrieveVariableFamilies(firstResult, maxResults, criteria);
            }
        });
        // Set required with a customValidator
        CustomValidator customValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return familyItem.getSelectedRelatedResources() != null && !familyItem.getSelectedRelatedResources().isEmpty();
            }
        };
        familyItem.setValidators(customValidator);
        familyItem.setTitleStyle("staticFormItemTitle");
        return familyItem;
    }
}
