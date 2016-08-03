package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Date;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.model.ds.VariableElementDS;
import org.siemac.metamac.srm.web.code.view.handlers.VariableElementsUiHandlers;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodeWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class VariableElementSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private VariableElementsUiHandlers           uiHandlers;

    private SearchRelatedResourcePaginatedWindow searchDimensionConceptWindow;

    public VariableElementSearchSectionStack() {
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        ((CustomCheckboxItem) advancedSearchForm.getItem(VariableElementDS.IS_GEOGRAPHICAL)).setValue(false);
        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).clearRelatedResource();
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        CustomCheckboxItem isGeographical = new CustomCheckboxItem(VariableElementDS.IS_GEOGRAPHICAL, getConstants().variableIsGeographical());
        CustomCheckboxItem hasShape = new CustomCheckboxItem(VariableElementDS.SHAPE_WKT, getConstants().variableElementHasPolygonShape());

        SearchRelatedResourceLinkItem geographicalGranularityItem = createDimensionConceptItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY, getConstants().variableElementGeographicalGranularity());

        CustomDateItem validFrom = new CustomDateItem(VariableElementDS.VALID_FROM, getConstants().variableElementValidFrom());
        CustomDateItem validTo = new CustomDateItem(VariableElementDS.VALID_TO, getConstants().variableElementValidTo());

        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{isGeographical, hasShape, validFrom, validTo, geographicalGranularityItem, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveVariableElements(SrmWebConstants.ITEM_LIST_FIRST_RESULT, SrmWebConstants.ITEM_LIST_MAX_RESULTS, getVariableElementWebCriteria());
    }

    public VariableElementWebCriteria getVariableElementWebCriteria() {
        VariableElementWebCriteria variableElementWebCriteria = new VariableElementWebCriteria();
        variableElementWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));

        boolean isGeographical = ((CustomCheckboxItem) advancedSearchForm.getItem(VariableElementDS.IS_GEOGRAPHICAL)).getValueAsBoolean();
        variableElementWebCriteria.setVariableType(isGeographical ? VariableTypeEnum.GEOGRAPHICAL : null);
        boolean isHashShape = ((CustomCheckboxItem) advancedSearchForm.getItem(VariableElementDS.SHAPE_WKT)).getValueAsBoolean();
        variableElementWebCriteria.setIsHasShape(isHashShape);

        RelatedResourceDto granularity = ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).getRelatedResourceDto();
        variableElementWebCriteria.setGranularityCodeUrn(granularity != null ? granularity.getUrn() : null);

        variableElementWebCriteria.setValidFromDate(advancedSearchForm.getValue(VariableElementDS.VALID_FROM) != null ? (Date) advancedSearchForm.getValue(VariableElementDS.VALID_FROM) : null);
        variableElementWebCriteria.setValidToDate(advancedSearchForm.getValue(VariableElementDS.VALID_TO) != null ? (Date) advancedSearchForm.getValue(VariableElementDS.VALID_TO) : null);

        return variableElementWebCriteria;
    }

    public void setUiHandlers(VariableElementsUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public VariableElementsUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setGeographicalGranularityCodes(GetRelatedResourcesResult result) {
        if (searchDimensionConceptWindow != null) {
            List<RelatedResourceDto> relatedResourceDtos = result.getRelatedResourceDtos();
            searchDimensionConceptWindow.setRelatedResources(relatedResourceDtos);
            searchDimensionConceptWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
        }
    }

    private SearchRelatedResourceLinkItem createDimensionConceptItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final RelatedResourceTypeEnum relatedResourceType = RelatedResourceTypeEnum.CODE;
        final SearchRelatedResourceLinkItem geographicalGranularityCodeItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        geographicalGranularityCodeItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchDimensionConceptWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveCodesForVariableElementGeographicalGranularity(firstResult, maxResults, new CodeWebCriteria(searchDimensionConceptWindow.getRelatedResourceCriteria()));
                    }
                });

                // Load concepts (to populate the selection window)
                getUiHandlers().retrieveCodesForVariableElementGeographicalGranularity(FIRST_RESULST, MAX_RESULTS, new CodeWebCriteria());

                searchDimensionConceptWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchDimensionConceptWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveCodesForVariableElementGeographicalGranularity(firstResult, maxResults, new CodeWebCriteria(searchDimensionConceptWindow.getRelatedResourceCriteria()));
                    }
                });
                searchDimensionConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedConcept = searchDimensionConceptWindow.getSelectedRelatedResource();
                        searchDimensionConceptWindow.markForDestroy();
                        // Set selected concept in form
                        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(VariableElementDS.GEOGRAPHICAL_GRANULARITY)).setRelatedResource(selectedConcept);
                    }
                });
            }
        });
        return geographicalGranularityCodeItem;
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
