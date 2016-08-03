package org.siemac.metamac.srm.web.client.widgets.filters;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.srm.web.client.widgets.filters.facets.ItemSchemeCriteriaFacetFilter;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class RelatedResourceItemWithSchemeFilterForm extends RelatedResourceItemFilterBaseForm<RelatedResourceItemWebCriteria> {

    protected ItemSchemeCriteriaFacetFilter itemSchemeFilterFacet;

    public RelatedResourceItemWithSchemeFilterForm(final int maxResults, final SearchPaginatedAction<RelatedResourceWebCriteria> action) {
        super();
        itemSchemeFilterFacet = new ItemSchemeCriteriaFacetFilter(maxResults, action);
        criteriaFacet.setColSpan(2);
    }

    public void setFilterResources(List<RelatedResourceDto> resources) {
        if (itemSchemeFilterFacet != null) {
            itemSchemeFilterFacet.setFilterResources(resources);
        }
    }

    public void refreshFilterSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        if (itemSchemeFilterFacet != null) {
            itemSchemeFilterFacet.refreshFilterSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    public ItemSchemeCriteriaFacetFilter getItemSchemeFilterFacet() {
        return itemSchemeFilterFacet;
    }

    @Override
    public List<FacetFilter> getFacets() {
        return Arrays.asList(onlyItemSchemeLastVersionFacet, itemSchemeFilterFacet, criteriaFacet);
    }

    @Override
    public RelatedResourceItemWebCriteria getSearchCriteria() {
        RelatedResourceItemWebCriteria criteria = super.getSearchCriteria();
        itemSchemeFilterFacet.populateCriteria(criteria);
        return criteria;
    }

    @Override
    protected RelatedResourceItemWebCriteria buildEmptySearchCriteria() {
        return new RelatedResourceItemWebCriteria();
    }
}
