package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.SearchMultipleRelatedResourceBasePaginatedWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * Window with a {@link SearchRelatedResourcePaginatedDragAndDropItem}. The source {@link ListGrid} is paginated. An extra simple item can be added in order to filter the
 * results showed in the source {@link ListGrid}.
 */
@Deprecated
public class SearchMultipleRelatedResourcePaginatedWindow extends SearchMultipleRelatedResourceBasePaginatedWindow<RelatedResourceDto> {

    public SearchMultipleRelatedResourcePaginatedWindow(String title, int maxResults, PaginatedAction action) {
        super(title, maxResults, action);
    }

    public SearchMultipleRelatedResourcePaginatedWindow(String title, int maxResults, FormItem initialSelectionItem, PaginatedAction action) {
        super(title, maxResults, initialSelectionItem, action);
    }
}
