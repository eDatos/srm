package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistListUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.CodelistWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;

public class CodelistSearchSectionStack extends VersionableResourceSearchSectionStack {

    private CodelistListUiHandlers               uiHandlers;

    private SearchRelatedResourcePaginatedWindow searchVariableWindow;

    private SearchRelatedResourcePaginatedWindow searchVariableElementsWindow;

    private SearchRelatedResourcePaginatedWindow searchVariableFamilyWindow;

    public CodelistSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        SelectItem accessType = new SelectItem(CodelistDS.ACCESS_TYPE, getConstants().codelistAccessType());
        accessType.setValueMap(CommonUtils.getAccessTypeHashMap());

        // Variable filter
        SearchRelatedResourceLinkItem variableItem = createVariableItem(CodelistDS.VARIABLE, getConstants().variable());

        // Variable element filter
        SearchRelatedResourceLinkItem variableElementItem = createVariableElementsItem(CodelistDS.VARIABLE_ITEM, getConstants().variableElement());

        // Variable family filter
        SearchRelatedResourceLinkItem variableFamilyItem = createVariableFamiliesItem(CodelistDS.VARIABLE_FAMILY, getConstants().variableFamily());

        // Add replaceTo and accessType items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] codelistFields = new FormItem[advancedSearchFormItems.length + 4];
        System.arraycopy(advancedSearchFormItems, 0, codelistFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, codelistFields, codelistFields.length - 1, 1);
        codelistFields[codelistFields.length - 5] = accessType;
        codelistFields[codelistFields.length - 4] = variableItem;
        codelistFields[codelistFields.length - 3] = variableElementItem;
        codelistFields[codelistFields.length - 2] = variableFamilyItem;

        advancedSearchForm.setFields(codelistFields);
    }

    public CodelistWebCriteria getCodelistWebCriteria() {
        CodelistWebCriteria codelistWebCriteria = (CodelistWebCriteria) getVersionableResourceWebCriteria(new CodelistWebCriteria());
        codelistWebCriteria.setAccessType(
                !StringUtils.isBlank(advancedSearchForm.getValueAsString(CodelistDS.ACCESS_TYPE)) ? AccessTypeEnum.valueOf(advancedSearchForm.getValueAsString(CodelistDS.ACCESS_TYPE)) : null);
        RelatedResourceDto variableDto = ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(CodelistDS.VARIABLE)).getRelatedResourceDto();
        if (variableDto != null) {
            String variableUrn = variableDto.getUrn();
            if (!StringUtils.isBlank(variableUrn)) {
                codelistWebCriteria.setVariableUrn(variableUrn);
            }
        }
        RelatedResourceDto variableElementDto = ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(CodelistDS.VARIABLE_ITEM)).getRelatedResourceDto();
        if (variableElementDto != null) {
            String variableElementUrn = variableElementDto.getUrn();
            if (!StringUtils.isBlank(variableElementUrn)) {
                codelistWebCriteria.setVariableElementUrn(variableElementUrn);
            }
        }
        RelatedResourceDto variableFamilyDto = ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(CodelistDS.VARIABLE_FAMILY)).getRelatedResourceDto();
        if (variableFamilyDto != null) {
            String variableFamilyUrn = variableFamilyDto.getUrn();
            if (!StringUtils.isBlank(variableFamilyUrn)) {
                codelistWebCriteria.setVariableFamilyUrn(variableFamilyUrn);
            }
        }
        return codelistWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCodelists(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS, getCodelistWebCriteria());
    }

    public void setUiHandlers(CodelistListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodelistListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setVariables(GetVariablesResult result) {
        searchVariableWindow.setRelatedResources(RelatedResourceUtils.getVariableBasicDtosAsRelatedResourceDtos(result.getVariables()));
        searchVariableWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariables().size(), result.getTotalResults());
    }

    public void setVariableElements(GetVariableElementsResult result) {
        searchVariableElementsWindow.setRelatedResources(RelatedResourceUtils.getVariableElementBasicDtosAsRelatedResourceDtos(result.getVariableElements()));
        searchVariableElementsWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getVariableElements().size(), result.getTotalResults());
    }

    public void setVariableFamilies(GetVariableFamiliesResult result) {
        searchVariableFamilyWindow.setRelatedResources(RelatedResourceUtils.getVariableFamilyBasicDtosAsRelatedResourceDtos(result.getFamilies()));
        searchVariableFamilyWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), result.getFamilies().size(), result.getTotalResults());
    }

    private SearchRelatedResourceLinkItem createVariableItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem variableItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        variableItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchVariableWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableSelection(), MAX_RESULTS, new PaginatedAction() {
                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariablesForSearch(firstResult, maxResults, new VariableWebCriteria(searchVariableWindow.getRelatedResourceCriteria()));
                    }
                });

                // Load variables (to populate the selection window)
                getUiHandlers().retrieveVariablesForSearch(FIRST_RESULST, MAX_RESULTS, null);

                searchVariableWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchVariableWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {
                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariablesForSearch(firstResult, maxResults, new VariableWebCriteria(criteria));
                    }
                });

                searchVariableWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedVariable = searchVariableWindow.getSelectedRelatedResource();
                        searchVariableWindow.markForDestroy();
                        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(CodelistDS.VARIABLE)).setRelatedResource(selectedVariable);
                    }
                });
            }
        });
        return variableItem;
    }

    private SearchRelatedResourceLinkItem createVariableElementsItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem variableElementItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        variableElementItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchVariableElementsWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableElement(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariableElementsForSearch(firstResult, maxResults, new VariableWebCriteria(searchVariableElementsWindow.getRelatedResourceCriteria()));
                    }
                });

                // Load variables families (to populate the selection window)
                getUiHandlers().retrieveVariableElementsForSearch(FIRST_RESULST, MAX_RESULTS, null);

                searchVariableElementsWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchVariableElementsWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariableElementsForSearch(firstResult, maxResults, new MetamacWebCriteria(criteria));
                    }
                });

                searchVariableElementsWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedElementFamily = searchVariableElementsWindow.getSelectedRelatedResource();
                        searchVariableElementsWindow.markForDestroy();
                        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(CodelistDS.VARIABLE_ITEM)).setRelatedResource(selectedElementFamily);
                    }
                });
            }
        });
        return variableElementItem;
    }

    private SearchRelatedResourceLinkItem createVariableFamiliesItem(String name, String title) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;
        final SearchRelatedResourceLinkItem variableFamilyItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        variableFamilyItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchVariableFamilyWindow = new SearchRelatedResourcePaginatedWindow(getConstants().variableFamily(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveVariableFamiliesForSearch(firstResult, maxResults, searchVariableFamilyWindow.getRelatedResourceCriteria());
                    }
                });

                // Load variables families (to populate the selection window)
                getUiHandlers().retrieveVariableFamiliesForSearch(FIRST_RESULST, MAX_RESULTS, null);

                searchVariableFamilyWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchVariableFamilyWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveVariableFamiliesForSearch(firstResult, maxResults, criteria);
                    }
                });

                searchVariableFamilyWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedVariableFamily = searchVariableFamilyWindow.getSelectedRelatedResource();
                        searchVariableFamilyWindow.markForDestroy();
                        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(CodelistDS.VARIABLE_FAMILY)).setRelatedResource(selectedVariableFamily);
                    }
                });
            }
        });
        return variableFamilyItem;
    }

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
