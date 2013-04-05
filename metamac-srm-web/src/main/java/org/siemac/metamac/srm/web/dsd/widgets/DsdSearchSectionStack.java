package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.SearchExternalItemWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class DsdSearchSectionStack extends VersionableResourceSearchSectionStack {

    private DsdListUiHandlers        uiHandlers;

    private SearchExternalItemWindow searchOperationWindow;

    public DsdSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        ViewTextItem statisticalOperationUrn = new ViewTextItem(DataStructureDefinitionDS.STATISTICAL_OPERATION_VIEW, getConstants().dsdOperation());
        statisticalOperationUrn.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem statisticalOperation = createStatisticalOperationItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        statisticalOperation.setTitleStyle("formTitle");

        // Add statistical operation item to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] dsdFields = new FormItem[advancedSearchFormItems.length + 2];
        System.arraycopy(advancedSearchFormItems, 0, dsdFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, dsdFields, dsdFields.length - 1, 1);
        dsdFields[dsdFields.length - 3] = statisticalOperationUrn;
        dsdFields[dsdFields.length - 2] = statisticalOperation;
        advancedSearchForm.setFields(dsdFields);
    }

    public DataStructureDefinitionWebCriteria getDataStructureDefinitionWebCriteria() {
        DataStructureDefinitionWebCriteria dataStructureDefinitionWebCriteria = (DataStructureDefinitionWebCriteria) getVersionableResourceWebCriteria(new DataStructureDefinitionWebCriteria());
        dataStructureDefinitionWebCriteria.setStatisticalOperationUrn(advancedSearchForm.getValueAsString(DataStructureDefinitionDS.STATISTICAL_OPERATION_VIEW));
        return dataStructureDefinitionWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveDsdList(DsdListPresenter.DSD_LIST_FIRST_RESULT, DsdListPresenter.DSD_LIST_MAX_RESULTS, getDataStructureDefinitionWebCriteria());
    }

    public void setOperations(GetStatisticalOperationsResult result) {
        if (searchOperationWindow != null) {
            searchOperationWindow.setExternalItems(result.getOperations());
            searchOperationWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getOperations().size(), result.getTotalResults());
        }
    }

    public void setUiHandlers(DsdListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public DsdListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private SearchViewTextItem createStatisticalOperationItem(String name, String title) {
        SearchViewTextItem operationItem = new SearchViewTextItem(name, title);
        operationItem.setRequired(true);
        operationItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final int OPERATION_FIRST_RESULT = 0;
                final int OPERATION_MAX_RESULTS = 16;
                searchOperationWindow = new SearchExternalItemWindow(getConstants().dsdSearchOperations(), OPERATION_MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveStatisticalOperationsForSearchSection(firstResult, maxResults, searchOperationWindow.getSearchCriteria());
                    }
                });
                getUiHandlers().retrieveStatisticalOperationsForSearchSection(OPERATION_FIRST_RESULT, OPERATION_MAX_RESULTS, null);
                searchOperationWindow.getListGrid().setSelectionType(SelectionStyle.SINGLE); // Only one statistical operation can be selected
                searchOperationWindow.getExternalListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String code) {
                        getUiHandlers().retrieveStatisticalOperationsForSearchSection(firstResult, maxResults, code);
                    }
                });
                searchOperationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        ExternalItemDto statisticalOperation = searchOperationWindow.getSelectedExternalItem();
                        searchOperationWindow.destroy();
                        advancedSearchForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION_VIEW, statisticalOperation != null ? statisticalOperation.getUrn() : null);
                        advancedSearchForm.setValue(DataStructureDefinitionDS.STATISTICAL_OPERATION, ExternalItemUtils.getExternalItemName(statisticalOperation));
                    }
                });
            }
        });
        return operationItem;
    }
}
