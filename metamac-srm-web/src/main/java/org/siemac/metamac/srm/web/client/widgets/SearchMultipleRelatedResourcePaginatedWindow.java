package org.siemac.metamac.srm.web.client.widgets;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseSearchWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class SearchMultipleRelatedResourcePaginatedWindow extends BaseSearchWindow {

    private SearchRelatedResourcePaginatedDragAndDropItem relatedResourcesDragAndDropItem;

    private FormItem                                      initialSelectionItem;

    public SearchMultipleRelatedResourcePaginatedWindow(String title, int maxResults, PaginatedAction action) {
        super(title);
        common(title, maxResults, null, action);
    }

    public SearchMultipleRelatedResourcePaginatedWindow(String title, int maxResults, FormItem initialSelectionItem, PaginatedAction action) {
        super(title);
        this.initialSelectionItem = initialSelectionItem;
        this.initialSelectionItem.setWidth(290);
        common(title, maxResults, initialSelectionItem, action);
    }

    private void common(String title, int maxResults, FormItem initialSelectionItem, PaginatedAction action) {
        relatedResourcesDragAndDropItem = new SearchRelatedResourcePaginatedDragAndDropItem("list", "title", maxResults, FORM_ITEM_CUSTOM_WIDTH, action);
        relatedResourcesDragAndDropItem.setShowTitle(false);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());

        if (initialSelectionItem == null) {
            form.setFields(relatedResourcesDragAndDropItem, saveItem);
        } else {
            relatedResourcesDragAndDropItem.setColSpan(2);
            form.setFields(initialSelectionItem, relatedResourcesDragAndDropItem, saveItem);
        }
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public void setSourceRelatedResources(List<RelatedResourceDto> relatedResources) {
        relatedResourcesDragAndDropItem.setSourceRelatedResources(relatedResources);
    }

    public void setTargetRelatedResources(List<RelatedResourceDto> relatedResources) {
        relatedResourcesDragAndDropItem.setTargetRelatedResources(relatedResources);
    }

    public void setSelectedRelatedResources(List<RelatedResourceDto> relatedResources) {
        relatedResourcesDragAndDropItem.setTargetRelatedResources(relatedResources);
    }

    public List<RelatedResourceDto> getSelectedRelatedResources() {
        return relatedResourcesDragAndDropItem.getSelectedRelatedResources();
    }

    public void refreshSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        relatedResourcesDragAndDropItem.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public void setSearchAction(SearchPaginatedAction action) {
        relatedResourcesDragAndDropItem.setSearchAction(action);
    }

    public String getRelatedResourceCriteria() {
        return relatedResourcesDragAndDropItem.getRelatedResourceCriteria();
    }

    public FormItem getInitialSelectionItem() {
        return initialSelectionItem;
    }

    public String getIntialSelectionValue() {
        return initialSelectionItem.getValue() != null && StringUtils.isNotEmpty(String.valueOf(initialSelectionItem.getValue())) ? String.valueOf(initialSelectionItem.getValue()) : null;
    }
}
