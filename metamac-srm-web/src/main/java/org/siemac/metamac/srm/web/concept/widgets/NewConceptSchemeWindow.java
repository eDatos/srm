package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchExternalPaginatedItem;

import com.smartgwt.client.types.SelectionStyle;
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
    private final static int            OPERATION_LIST_MAX_RESULTS  = 6;

    private CustomDynamicForm           form;
    private SearchExternalPaginatedItem searchOperationItem;

    private ConceptSchemeListUiHandlers uiHandlers;

    public NewConceptSchemeWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(ConceptSchemeDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getConceptSchemeIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(ConceptSchemeDS.NAME, getConstants().nameableArtefactName());
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

        searchOperationItem = new SearchExternalPaginatedItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation(), FORM_ITEM_CUSTOM_WIDTH + 100, OPERATION_LIST_MAX_RESULTS,
                new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, searchOperationItem.getSearchCriteria());
                    }
                });
        searchOperationItem.setRequired(true);
        searchOperationItem.getListGrid().setSelectionType(SelectionStyle.SINGLE);
        searchOperationItem.setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                uiHandlers.retrieveStatisticalOperations(firstResult, maxResults, criteria);
            }
        });
        searchOperationItem.setWidth(FORM_ITEM_CUSTOM_WIDTH + 100);
        searchOperationItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return ConceptSchemeTypeEnum.OPERATION.getName().equals(form.getValueAsString(ConceptSchemeDS.TYPE));
            }
        });

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().conceptSchemeCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, type, searchOperationItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setOperations(GetStatisticalOperationsResult result) {
        ((SearchExternalPaginatedItem) form.getItem(ConceptSchemeDS.RELATED_OPERATION)).setExternalItems(result.getOperations());
        ((SearchExternalPaginatedItem) form.getItem(ConceptSchemeDS.RELATED_OPERATION)).refreshPaginationInfo(result.getFirstResultOut(), result.getOperations().size(), result.getTotalResults());
    }

    public ConceptSchemeMetamacDto getNewConceptSchemeDto() {
        ConceptSchemeMetamacDto conceptSchemeDto = new ConceptSchemeMetamacDto();
        conceptSchemeDto.setMaintainer(RelatedResourceUtils.getDefaultMaintainerAsRelatedResourceDto());
        conceptSchemeDto.setCode(form.getValueAsString(ConceptSchemeDS.CODE));
        conceptSchemeDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(ConceptSchemeDS.NAME)));
        conceptSchemeDto.setType(ConceptSchemeTypeEnum.valueOf(form.getValueAsString(ConceptSchemeDS.TYPE)));
        conceptSchemeDto.setRelatedOperation(ExternalItemUtils.removeTitle(((SearchExternalPaginatedItem) form.getItem(ConceptSchemeDS.RELATED_OPERATION)).getSelectedExternalItem()));
        conceptSchemeDto.setIsPartial(false);
        return conceptSchemeDto;
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
