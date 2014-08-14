package org.siemac.metamac.srm.web.client.widgets.search;

import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.base.FilterForm;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchRelatedResourceBasePaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SearchSingleRelatedResourcePaginatedWindow<T extends MetamacWebCriteria> extends SearchRelatedResourceBasePaginatedWindow<RelatedResourceDto, T> {

    public SearchSingleRelatedResourcePaginatedWindow(String title, int maxResults, FilterForm<T> filter, SearchPaginatedAction<T> action) {
        super(title, maxResults, filter, action);
    }

}
