package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

/**
 * Window with a {@link SearchRelatedResourcePaginatedDragAndDropItem} and a {@link SearchRelatedResourcePaginatedItem} as a filter.
 */
public class SearchMultipleRelatedResourcePaginatedWithRelatedResourceFilterWindow extends CustomWindow {

    private static final int                              FORM_ITEM_CUSTOM_WIDTH = 500;

    private static final String                           FIELD_SAVE             = "save-ex";

    private CustomDynamicForm                             filterForm;
    private CustomDynamicForm                             form;

    private SearchRelatedResourcePaginatedItem            filterListItem;
    private SearchRelatedResourcePaginatedDragAndDropItem selectionListItem;

    public SearchMultipleRelatedResourcePaginatedWithRelatedResourceFilterWindow(String title, String filterTitle, String selectionListTitle, int maxResults, PaginatedAction filterListAction,
            PaginatedAction selectionListAction) {
        super(title);
        setAutoSize(true);

        // Filter section

        filterListItem = new SearchRelatedResourcePaginatedItem("filterList", filterTitle, FORM_ITEM_CUSTOM_WIDTH, maxResults, filterListAction);

        filterForm = new CustomDynamicForm();
        filterForm.setTitleOrientation(TitleOrientation.TOP);
        filterForm.setMargin(15);
        filterForm.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        filterForm.setFields(filterListItem);

        // RelatedResource selection

        selectionListItem = new SearchRelatedResourcePaginatedDragAndDropItem("list", selectionListTitle, maxResults, FORM_ITEM_CUSTOM_WIDTH, selectionListAction);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());

        form = new CustomDynamicForm();
        form.setTitleOrientation(TitleOrientation.TOP);
        form.setMargin(15);
        form.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        form.setFields(selectionListItem, saveItem);

        addItem(filterForm);
        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setFilterRelatedResources(List<RelatedResourceDto> relatedResources) {
        filterListItem.setRelatedResources(relatedResources);
    }

    public void setSourceRelatedResources(List<RelatedResourceDto> relatedResources) {
        selectionListItem.setSourceRelatedResources(relatedResources);
    }

    public void setTargetRelatedResources(List<RelatedResourceDto> relatedResources) {
        selectionListItem.setTargetRelatedResources(relatedResources);
    }

    public void setSelectedRelatedResources(List<RelatedResourceDto> relatedResources) {
        selectionListItem.setTargetRelatedResources(relatedResources);
    }

    public RelatedResourceDto getSelectedRelatedResourceAsFilter() {
        return filterListItem.getSelectedRelatedResource();
    }

    public List<RelatedResourceDto> getSelectedRelatedResources() {
        return selectionListItem.getSelectedRelatedResources();
    }

    public void refreshSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        selectionListItem.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public void refreshFilterListPaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        filterListItem.refreshPaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public void setFilterListSearchAction(SearchPaginatedAction action) {
        filterListItem.setSearchAction(action);
    }

    public void setSelectionListSearchAction(SearchPaginatedAction action) {
        selectionListItem.setSearchAction(action);
    }

    public String getFilterListCriteria() {
        return filterListItem.getRelatedResourceCriteria();
    }

    public String getSelectionListCriteria() {
        return selectionListItem.getRelatedResourceCriteria();
    }
}
