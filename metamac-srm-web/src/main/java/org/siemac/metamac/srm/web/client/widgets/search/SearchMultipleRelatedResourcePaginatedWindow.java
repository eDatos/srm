package org.siemac.metamac.srm.web.client.widgets.search;

import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.SimpleFilterForm;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleRelatedResourceBasePaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SearchMultipleRelatedResourcePaginatedWindow extends SearchMultipleRelatedResourceBasePaginatedWindow<RelatedResourceDto, MetamacWebCriteria> {

    public SearchMultipleRelatedResourcePaginatedWindow(String title, int maxResults, SearchPaginatedAction<MetamacWebCriteria> action) {
        super(title, maxResults, new SimpleFilterForm(), action);
    }
}
