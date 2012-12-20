package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.BaseSearchWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class SearchRelatedResourceWindow extends BaseSearchWindow {

    private SearchRelatedResourcePaginatedItem listGridItem;

    public SearchRelatedResourceWindow(String title, int maxResults, PaginatedAction action) {
        super(title, maxResults);

        listGridItem = new SearchRelatedResourcePaginatedItem("list", "title", FORM_ITEM_CUSTOM_WIDTH, maxResults, action);
        listGridItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        listGridItem.setShowTitle(false);

        getListGrid().setSelectionType(SelectionStyle.SINGLE);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());

        form.setFields(listGridItem, saveItem);
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setRelatedResources(List<RelatedResourceDto> relatedResources) {
        listGridItem.setRelatedResources(relatedResources);
    }

    public RelatedResourceDto getSelectedRelatedResource() {
        return listGridItem.getSelectedRelatedResource();
    }

    public void refreshSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        listGridItem.refreshPaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public void setSearchAction(SearchPaginatedAction action) {
        listGridItem.setSearchAction(action);
    }

    public SearchRelatedResourcePaginatedItem getListGridItem() {
        return listGridItem;
    }

    public BaseCustomListGrid getListGrid() {
        return listGridItem.getListGrid();
    }
}
