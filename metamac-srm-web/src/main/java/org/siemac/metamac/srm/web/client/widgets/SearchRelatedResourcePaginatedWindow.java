package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.SearchRelatedResourceBasePaginatedWindow;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * Window with a {@link SearchRelatedResourcePaginatedItem}. The source {@link ListGrid} is paginated.
 */
public class SearchRelatedResourcePaginatedWindow extends SearchRelatedResourceBasePaginatedWindow<RelatedResourceDto> {

    public SearchRelatedResourcePaginatedWindow(String title, int maxResults, PaginatedAction action) {
        super(title, maxResults, action);
    }

    public SearchRelatedResourcePaginatedWindow(String title, int maxResults, FormItem initialSelectionItem, PaginatedAction action) {
        super(title, maxResults, initialSelectionItem, action);
    }
}
