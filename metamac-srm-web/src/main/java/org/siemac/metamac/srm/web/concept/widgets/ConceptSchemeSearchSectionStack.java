package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.concept.presenter.ConceptSchemeListPresenter;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.ConceptSchemeWebCriteria;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.SearchExternalItemWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class ConceptSchemeSearchSectionStack extends VersionableResourceSearchSectionStack {

    private ConceptSchemeListUiHandlers uiHandlers;

    private SearchExternalItemWindow    searchOperationWindow;

    public ConceptSchemeSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {

        // Statistical operation
        ViewTextItem statisticalOperationUrn = new ViewTextItem(ConceptSchemeDS.RELATED_OPERATION_URN, getConstants().conceptSchemeOperation());
        statisticalOperationUrn.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem statisticalOperation = createStatisticalOperationItem(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());

        // Type
        SelectItem conceptSchemeType = new SelectItem(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        conceptSchemeType.setValueMap(CommonUtils.getConceptSchemeTypeHashMap());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] conceptFields = new FormItem[advancedSearchFormItems.length + 3];
        System.arraycopy(advancedSearchFormItems, 0, conceptFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, conceptFields, conceptFields.length - 1, 1);
        conceptFields[conceptFields.length - 4] = statisticalOperationUrn;
        conceptFields[conceptFields.length - 3] = statisticalOperation;
        conceptFields[conceptFields.length - 2] = conceptSchemeType;
        advancedSearchForm.setFields(conceptFields);
    }

    public ConceptSchemeWebCriteria getConceptSchemeWebCriteria() {
        ConceptSchemeWebCriteria conceptSchemeWebCriteria = (ConceptSchemeWebCriteria) getVersionableResourceWebCriteria(new ConceptSchemeWebCriteria());
        conceptSchemeWebCriteria.setStatisticalOperationUrn(advancedSearchForm.getValueAsString(ConceptSchemeDS.RELATED_OPERATION_URN));
        conceptSchemeWebCriteria.setConceptSchemeTypeEnum(!StringUtils.isBlank(advancedSearchForm.getValueAsString(ConceptSchemeDS.TYPE)) ? ConceptSchemeTypeEnum.valueOf(advancedSearchForm
                .getValueAsString(ConceptSchemeDS.TYPE)) : null);
        return conceptSchemeWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveConceptSchemes(ConceptSchemeListPresenter.SCHEME_LIST_FIRST_RESULT, ConceptSchemeListPresenter.SCHEME_LIST_MAX_RESULTS, getConceptSchemeWebCriteria());
    }

    public void setOperations(GetStatisticalOperationsResult result) {
        if (searchOperationWindow != null) {
            searchOperationWindow.setExternalItems(result.getOperations());
            searchOperationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getOperations().size(), result.getTotalResults());
        }
    }

    public void setUiHandlers(ConceptSchemeListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public ConceptSchemeListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private SearchViewTextItem createStatisticalOperationItem(String name, String title) {
        SearchViewTextItem operationItem = new SearchViewTextItem(name, title);
        operationItem.setTitleStyle("formTitle");
        operationItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final int OPERATION_FIRST_RESULT = 0;
                final int OPERATION_MAX_RESULTS = 16;
                searchOperationWindow = new SearchExternalItemWindow(getConstants().conceptSchemeSearchOperations(), OPERATION_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveStatisticalOperationsForSearchSection(firstResult, maxResults, searchOperationWindow.getSearchCriteria());
                    }
                });
                getUiHandlers().retrieveStatisticalOperationsForSearchSection(OPERATION_FIRST_RESULT, OPERATION_MAX_RESULTS, null);
                searchOperationWindow.getListGrid().setSelectionType(SelectionStyle.SINGLE); // Only one statistical operation can be selected
                searchOperationWindow.getExternalListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveStatisticalOperationsForSearchSection(firstResult, maxResults, criteria);
                    }
                });
                searchOperationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        ExternalItemDto statisticalOperation = searchOperationWindow.getSelectedExternalItem();
                        searchOperationWindow.destroy();
                        advancedSearchForm.setValue(ConceptSchemeDS.RELATED_OPERATION_URN, statisticalOperation != null ? statisticalOperation.getUrn() : null);
                        advancedSearchForm.setValue(ConceptSchemeDS.RELATED_OPERATION, ExternalItemUtils.getExternalItemName(statisticalOperation));
                    }
                });
            }
        });
        return operationItem;
    }
}
