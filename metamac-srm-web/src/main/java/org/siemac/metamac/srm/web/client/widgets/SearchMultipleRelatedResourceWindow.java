package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.actions.search.SearchAction;
import org.siemac.metamac.web.common.client.widgets.filters.base.FilterForm;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchMultipleRelatedResourceBaseWindow;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SearchMultipleRelatedResourceWindow<T extends MetamacWebCriteria> extends SearchMultipleRelatedResourceBaseWindow<RelatedResourceDto, T> {

    public SearchMultipleRelatedResourceWindow(String title, FilterForm<T> filter, SearchAction<T> action) {
        super(title, filter, action);
    }
}
