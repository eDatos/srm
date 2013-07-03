package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public abstract class SearchItemItem extends SearchRelatedResourceLinkItem {

    protected SearchRelatedResourcePaginatedWithRelatedResourceFilterWindow searchItemWindow;

    private ClickHandler                                                    saveClickHandler;

    private String                                                          informationLabelMessage;

    public SearchItemItem(final String name, String title, final String windowTitle, final String windowFilterListTitle, final String windowFilterTextTitle, final String windowSelectionListTitle,
            CustomLinkItemNavigationClickHandler itemNavigationHandler) {
        super(name, title, itemNavigationHandler);

        getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                final int FIRST_RESULT = 0;
                final int MAX_RESULTS = 6;

                PaginatedAction filterListAction = new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        retrieveItemSchemes(firstResult, maxResults, searchItemWindow.getFilterListCriteria());
                    }

                };
                PaginatedAction selectionListAction = new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        retrieveItems(firstResult, maxResults, searchItemWindow.getSelectionListCriteria(), searchItemWindow.getSelectedRelatedResourceUrnAsFilter());
                    }

                };

                searchItemWindow = new SearchRelatedResourcePaginatedWithRelatedResourceFilterWindow(windowTitle, windowFilterListTitle, windowFilterTextTitle, windowSelectionListTitle, MAX_RESULTS,
                        filterListAction, selectionListAction);

                // Add an information message (if it has been specified")
                if (!StringUtils.isBlank(informationLabelMessage)) {
                    searchItemWindow.setInformationLabelMessage(informationLabelMessage);
                    searchItemWindow.showInformationLabel();
                }

                // Hide the initial filter form (it is empty)
                searchItemWindow.getInitialFilterForm().hide();

                // Load the list of items (to populate the selection window)
                retrieveItemSchemes(FIRST_RESULT, MAX_RESULTS, null);
                retrieveItems(FIRST_RESULT, MAX_RESULTS, null, null);

                // Filter items when the item scheme filter changes
                searchItemWindow.getFilterListItem().getListGrid().addRecordClickHandler(new RecordClickHandler() {

                    @Override
                    public void onRecordClick(RecordClickEvent event) {
                        retrieveItems(FIRST_RESULT, MAX_RESULTS, searchItemWindow.getSelectionListCriteria(), searchItemWindow.getSelectedRelatedResourceUrnAsFilter());
                    }
                });
                searchItemWindow.getClearButton().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        retrieveItems(FIRST_RESULT, MAX_RESULTS, searchItemWindow.getSelectionListCriteria(), searchItemWindow.getSelectedRelatedResourceUrnAsFilter());
                    }
                });

                // Set the search actions
                searchItemWindow.setSelectionListSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        retrieveItems(firstResult, maxResults, criteria, searchItemWindow.getSelectedRelatedResourceUrnAsFilter());
                    }
                });
                searchItemWindow.setFilterListSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        retrieveItemSchemes(firstResult, maxResults, criteria);
                    }
                });

                searchItemWindow.getSave().addClickHandler(saveClickHandler);

            }
        });
    }

    public void setInformationLabelMessage(String informationLabelMessage) {
        this.informationLabelMessage = informationLabelMessage;
    }

    public void setItemSchemes(List<RelatedResourceDto> itemSchemes, int firstResult, int totalResults) {
        if (searchItemWindow != null) {
            searchItemWindow.setFilterRelatedResources(itemSchemes);
            searchItemWindow.refreshFilterListPaginationInfo(firstResult, itemSchemes.size(), totalResults);
        }
    }

    public void setItems(List<RelatedResourceDto> items, int firstResult, int totalResults) {
        if (searchItemWindow != null) {
            searchItemWindow.setRelatedResources(items);
            searchItemWindow.refreshListPaginationInfo(firstResult, items.size(), totalResults);
        }
    }

    public void markSearchWindowForDestroy() {
        searchItemWindow.markForDestroy();
    }

    public void setSaveClickHandler(ClickHandler clickHandler) {
        this.saveClickHandler = clickHandler;
    }

    public SearchRelatedResourcePaginatedWithRelatedResourceFilterWindow getSearchWindow() {
        return searchItemWindow;
    }

    public RelatedResourceDto getSelectedItem() {
        if (searchItemWindow != null) {
            return searchItemWindow.getSelectedRelatedResource();
        }
        return null;
    }

    protected abstract void retrieveItemSchemes(int firstResult, int maxResults, String filterListCriteria);
    protected abstract void retrieveItems(int firstResult, int maxResults, String selectionListCriteria, String selectedRelatedResourceUrnAsFilter);

}
