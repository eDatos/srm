package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchRelatedResourceBasePaginatedItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SearchRelatedResourcePaginatedItem extends SearchRelatedResourceBasePaginatedItem<RelatedResourceDto> {

    public SearchRelatedResourcePaginatedItem(String name, String title, String formItemWidth, int maxResults, PaginatedAction action) {
        super(name, title, formItemWidth, maxResults, action);
    }

    public SearchRelatedResourcePaginatedItem(String name, String title, int maxResults, PaginatedAction action) {
        super(name, title, maxResults, action);
    }
}
