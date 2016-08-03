package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchRelatedResourceBasePaginatedWithRelatedResourceFilterWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SearchRelatedResourcePaginatedWithRelatedResourceFilterWindow extends SearchRelatedResourceBasePaginatedWithRelatedResourceFilterWindow<RelatedResourceDto> {

    public SearchRelatedResourcePaginatedWithRelatedResourceFilterWindow(String title, String filterListTitle, String filterTextTitle, String selectionListTitle, int maxResults,
            PaginatedAction filterListAction, PaginatedAction selectionListAction) {
        super(title, filterListTitle, filterTextTitle, selectionListTitle, maxResults, filterListAction, selectionListAction);
    }
}
