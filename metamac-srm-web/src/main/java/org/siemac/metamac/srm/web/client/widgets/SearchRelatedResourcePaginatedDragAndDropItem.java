package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchRelatedResourceBasePaginatedDragAndDropItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SearchRelatedResourcePaginatedDragAndDropItem extends SearchRelatedResourceBasePaginatedDragAndDropItem<RelatedResourceDto> {

    public SearchRelatedResourcePaginatedDragAndDropItem(String name, String title, int maxResults, PaginatedAction action) {
        super(name, title, maxResults, action);
    }

    public SearchRelatedResourcePaginatedDragAndDropItem(String name, String title, int maxResults, String formItemWidth, PaginatedAction action) {
        super(name, title, maxResults, formItemWidth, action);
    }
}
