package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.presenter.DsdListPresenter;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.SearchExternalItemWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class DsdSearchSectionStack extends VersionableResourceSearchSectionStack {

    private DsdListUiHandlers                    uiHandlers;

    private SearchExternalItemWindow             searchOperationWindow;
    private SearchRelatedResourcePaginatedWindow searchDimensionConceptWindow;
    private SearchRelatedResourcePaginatedWindow searchAttributeConceptWindow;

    public DsdSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {

        // Statistical operation item
        SearchExternalItemLinkItem statisticalOperation = createStatisticalOperationItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());

        // Dimension concept item
        ViewTextItem dimensionConceptUrn = new ViewTextItem(DataStructureDefinitionDS.DIMENSION_CONCEPT_URN, getConstants().dsdDimensionConcept());
        dimensionConceptUrn.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem dimensionConcept = createDimensionConceptItem(DataStructureDefinitionDS.DIMENSION_CONCEPT, getConstants().dsdDimensionConcept());

        // Attribute concept item
        ViewTextItem attributeConceptUrn = new ViewTextItem(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT_URN, getConstants().dsdAttributeConcept());
        attributeConceptUrn.setShowIfCondition(FormItemUtils.getFalseFormItemIfFunction());
        SearchViewTextItem attributeConcept = createAttributeConceptItem(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT, getConstants().dsdAttributeConcept());

        // Add items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] dsdFields = new FormItem[advancedSearchFormItems.length + 5];
        System.arraycopy(advancedSearchFormItems, 0, dsdFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, dsdFields, dsdFields.length - 1, 1);
        dsdFields[dsdFields.length - 6] = statisticalOperation;
        dsdFields[dsdFields.length - 5] = dimensionConceptUrn;
        dsdFields[dsdFields.length - 4] = dimensionConcept;
        dsdFields[dsdFields.length - 3] = attributeConceptUrn;
        dsdFields[dsdFields.length - 2] = attributeConcept;
        advancedSearchForm.setFields(dsdFields);
    }

    public DataStructureDefinitionWebCriteria getDataStructureDefinitionWebCriteria() {
        DataStructureDefinitionWebCriteria dataStructureDefinitionWebCriteria = (DataStructureDefinitionWebCriteria) getVersionableResourceWebCriteria(new DataStructureDefinitionWebCriteria());
        ExternalItemDto statisticalOperation = ((SearchExternalItemLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.STATISTICAL_OPERATION)).getExternalItemDto();
        dataStructureDefinitionWebCriteria.setStatisticalOperationUrn(statisticalOperation != null ? statisticalOperation.getUrn() : null);
        dataStructureDefinitionWebCriteria.setDimensionConceptUrn(advancedSearchForm.getValueAsString(DataStructureDefinitionDS.DIMENSION_CONCEPT_URN));
        dataStructureDefinitionWebCriteria.setAttributeConceptUrn(advancedSearchForm.getValueAsString(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT_URN));
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

    public void setDimensionConcepts(GetConceptsResult result) {
        if (searchDimensionConceptWindow != null) {
            List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getConceptMetamacBasicDtosAsRelatedResourceDtos(result.getConcepts());
            searchDimensionConceptWindow.setRelatedResources(relatedResourceDtos);
            searchDimensionConceptWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
        }
    }

    public void setAttributeConcepts(GetConceptsResult result) {
        if (searchAttributeConceptWindow != null) {
            List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getConceptMetamacBasicDtosAsRelatedResourceDtos(result.getConcepts());
            searchAttributeConceptWindow.setRelatedResources(relatedResourceDtos);
            searchAttributeConceptWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
        }
    }

    public void setUiHandlers(DsdListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public DsdListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private SearchExternalItemLinkItem createStatisticalOperationItem(String name, String title) {
        SearchExternalItemLinkItem operationItem = new SearchExternalItemLinkItem(name, title);
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
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveStatisticalOperationsForSearchSection(firstResult, maxResults, criteria);
                    }
                });
                searchOperationWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        ExternalItemDto statisticalOperation = searchOperationWindow.getSelectedExternalItem();
                        searchOperationWindow.destroy();
                        ((SearchExternalItemLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.STATISTICAL_OPERATION)).setExternalItem(statisticalOperation);
                    }
                });
            }
        });
        return operationItem;
    }

    private SearchViewTextItem createDimensionConceptItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem dimensionConceptItem = new SearchViewTextItem(name, title);
        dimensionConceptItem.setTitleStyle("formTitle");
        dimensionConceptItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchDimensionConceptWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveDimensionConceptsForSearchSection(firstResult, maxResults, searchDimensionConceptWindow.getRelatedResourceCriteria());
                    }
                });

                // Load concepts (to populate the selection window)
                getUiHandlers().retrieveDimensionConceptsForSearchSection(FIRST_RESULST, MAX_RESULTS, null);

                searchDimensionConceptWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchDimensionConceptWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveDimensionConceptsForSearchSection(firstResult, maxResults, criteria);
                    }
                });
                searchDimensionConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedConcept = searchDimensionConceptWindow.getSelectedRelatedResource();
                        searchDimensionConceptWindow.markForDestroy();
                        // Set selected concept in form
                        advancedSearchForm.setValue(DataStructureDefinitionDS.DIMENSION_CONCEPT_URN, selectedConcept != null ? selectedConcept.getUrn() : null);
                        advancedSearchForm.setValue(DataStructureDefinitionDS.DIMENSION_CONCEPT,
                                selectedConcept != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedConcept) : null);
                    }
                });
            }
        });
        return dimensionConceptItem;
    }

    private SearchViewTextItem createAttributeConceptItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchViewTextItem attributeConceptItem = new SearchViewTextItem(name, title);
        attributeConceptItem.setTitleStyle("formTitle");
        attributeConceptItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchAttributeConceptWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveAttributeConceptsForSearchSection(firstResult, maxResults, searchAttributeConceptWindow.getRelatedResourceCriteria());
                    }
                });

                // Load concepts (to populate the selection window)
                getUiHandlers().retrieveAttributeConceptsForSearchSection(FIRST_RESULST, MAX_RESULTS, null);

                searchAttributeConceptWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchAttributeConceptWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveAttributeConceptsForSearchSection(firstResult, maxResults, criteria);
                    }
                });
                searchAttributeConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedConcept = searchAttributeConceptWindow.getSelectedRelatedResource();
                        searchAttributeConceptWindow.markForDestroy();
                        // Set selected concept in form
                        advancedSearchForm.setValue(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT_URN, selectedConcept != null ? selectedConcept.getUrn() : null);
                        advancedSearchForm.setValue(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT,
                                selectedConcept != null ? org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils.getRelatedResourceName(selectedConcept) : null);
                    }
                });
            }
        });
        return attributeConceptItem;
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        ((SearchExternalItemLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.STATISTICAL_OPERATION)).clearExternalItem();
    }
}
