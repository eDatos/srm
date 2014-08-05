package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.SearchMultipleRelatedResourceBasePaginatedWithRelatedResourceFilterWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

/**
 * Window with a {@link SearchRelatedResourcePaginatedDragAndDropItem} and a {@link SearchRelatedResourcePaginatedItem} as a filter.
 */
@Deprecated
public class SearchMultipleRelatedResourcePaginatedWithRelatedResourceFilterWindow extends SearchMultipleRelatedResourceBasePaginatedWithRelatedResourceFilterWindow<RelatedResourceDto> {

    public SearchMultipleRelatedResourcePaginatedWithRelatedResourceFilterWindow(String title, String filterListTitle, String filterTextTitle, String selectionListTitle, int maxResults,
            PaginatedAction filterListAction, PaginatedAction selectionListAction) {
        super(title, filterListTitle, filterTextTitle, selectionListTitle, maxResults, filterListAction, selectionListAction);
    }
}
