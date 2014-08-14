package org.siemac.metamac.srm.web.client.widgets.search;

import java.util.List;

import org.siemac.metamac.srm.web.client.widgets.filters.RelatedResourceItemWithSchemeFilterForm;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class SearchSingleSrmItemWithSchemeFilterPaginatedWindow extends SearchSingleRelatedResourcePaginatedWindow<RelatedResourceItemWebCriteria> {

    public SearchSingleSrmItemWithSchemeFilterPaginatedWindow(String title, int maxResults, SearchPaginatedAction<RelatedResourceWebCriteria> filterSearchAction,
            SearchPaginatedAction<RelatedResourceItemWebCriteria> action) {
        super(title, maxResults, new RelatedResourceItemWithSchemeFilterForm(maxResults, filterSearchAction), action);
    }

    public void setFilterResources(List<RelatedResourceDto> resources) {
        getFilter().setFilterResources(resources);
    }

    public void refreshFilterSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        getFilter().refreshFilterSourcePaginationInfo(firstResult, elementsInPage, totalResults);
    }

    public RelatedResourceItemWithSchemeFilterForm getFilter() {
        return (RelatedResourceItemWithSchemeFilterForm) getFilterForm();
    }
}
