package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchExternalPaginatedItem;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewConceptSchemeWindow extends CustomWindow {

    private static final int            FORM_ITEM_CUSTOM_WIDTH      = 300;
    private static final String         FIELD_SAVE                  = "save-sch";

    private final static int            OPERATION_LIST_FIRST_RESULT = 0;
    private final static int            OPERATION_LIST_MAX_RESULTS  = 2;

    private CustomDynamicForm           form;

    private ConceptSchemeListUiHandlers uiHandlers;

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
        type.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                form.markForRedraw();
                if (ConceptSchemeTypeEnum.OPERATION.getName().equals(form.getValueAsString(ConceptSchemeDS.TYPE))) {
                    uiHandlers.retrieveStatisticalOperations(OPERATION_LIST_FIRST_RESULT, OPERATION_LIST_MAX_RESULTS, null);
                }
            }
        });

        SearchExternalPaginatedItem operation = new SearchExternalPaginatedItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation(), FORM_ITEM_CUSTOM_WIDTH + 100,
                OPERATION_LIST_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, null);
                    }
                });
        operation.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String code) {
                uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, code);
            }
        });
        operation.setWidth(FORM_ITEM_CUSTOM_WIDTH + 100);
        operation.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptSchemeTypeEnum.OPERATION.getName().equals(form.getValueAsString(ConceptSchemeDS.TYPE));
            }
        });

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

    public void setOperations(List<ExternalItemDto> externalItemsDtos) {
        ((SearchExternalPaginatedItem) form.getItem(ConceptSchemeDS.RELATED_OPERATION)).setExternalItems(externalItemsDtos);
    }

    public void refreshOperationsPaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        ((SearchExternalPaginatedItem) form.getItem(ConceptSchemeDS.RELATED_OPERATION)).refreshPaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public ConceptSchemeMetamacDto getNewConceptSchemeDto() {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setCode(form.getValueAsString(ConceptSchemeDS.CODE));
        conceptSchemeDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(ConceptSchemeDS.NAME)));
        return conceptSchemeDto;
    }

    public boolean validateForm() {
        return form.validate();
    }

    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

}
