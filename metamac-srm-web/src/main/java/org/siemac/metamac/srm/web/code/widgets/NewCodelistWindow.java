package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedItem;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewCodelistWindow extends CustomWindow {

    private static final int                   FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String                FIELD_SAVE             = "save-sch";

    private static final int                   VARIABLE_FIRST_RESULT  = 0;
    private static final int                   VARIABLE_MAX_RESULTS   = 8;

    private CustomDynamicForm                  form;
    private SearchRelatedResourcePaginatedItem variableItem;

    public NewCodelistWindow(String title, final CodelistListUiHandlers uiHandlers) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(CodelistDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getCodelistIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(CodelistDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        variableItem = new SearchRelatedResourcePaginatedItem(CodelistDS.VARIABLE, getConstants().variable(), FORM_ITEM_CUSTOM_WIDTH, VARIABLE_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                uiHandlers.retrieveVariables(firstResult, maxResults, variableItem.getSearchCriteria());
            }
        });
        variableItem.setRequired(true);
        variableItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        // Load variables (to populate the listGrid)
        uiHandlers.retrieveVariables(VARIABLE_FIRST_RESULT, VARIABLE_MAX_RESULTS, null);
        variableItem.getListGrid().setSelectionType(SelectionStyle.SINGLE);
        variableItem.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                uiHandlers.retrieveVariables(firstResult, maxResults, variableItem.getSearchCriteria());
            }
        });

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().codelistCreate());

        form = new CustomDynamicForm();
        form.setMargin(15);
        form.setFields(codeItem, nameItem, variableItem, saveItem);

        addItem(form);
        show();
    }

    public void setVariables(GetVariablesResult result) {
        List<RelatedResourceDto> variables = RelatedResourceUtils.getRelatedResourceDtosFromVariableDtos(result.getVariables());
        ((SearchRelatedResourcePaginatedItem) form.getItem(CodelistDS.VARIABLE)).setRelatedResources(variables);
        ((SearchRelatedResourcePaginatedItem) form.getItem(CodelistDS.VARIABLE)).refreshPaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
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
        codelistDto.setIsPartial(false);
        codelistDto.setVariable(variableItem.getSelectedRelatedResource());
        return codelistDto;
    }
}
