package org.siemac.metamac.srm.web.client.widgets.filters;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.web.common.client.widgets.filters.base.SimpleFilterBaseForm;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;
import org.siemac.metamac.web.common.client.widgets.filters.facets.OnlyLastVersionFacetFilter;

public class SimpleSrmRelatedResourceHiddenLastVersionFilterForm extends SimpleFilterBaseForm<RelatedResourceWebCriteria> {

    protected OnlyLastVersionFacetFilter onlyLastVersionFacet;

    public SimpleSrmRelatedResourceHiddenLastVersionFilterForm() {
        super();
        onlyLastVersionFacet = new OnlyLastVersionFacetFilter();
        onlyLastVersionFacet.setColSpan(2);
        criteriaFacet.setColSpan(2);
    }

    // IMPORTANT: This method must be inherited if you change the WebCriteria in T
    @Override
    public RelatedResourceWebCriteria getSearchCriteria() {
        RelatedResourceWebCriteria searchCriteria = super.getSearchCriteria();

        onlyLastVersionFacet.populateCriteria(searchCriteria);

        return searchCriteria;
    }

    @Override
    protected RelatedResourceWebCriteria buildEmptySearchCriteria() {
        return new RelatedResourceWebCriteria();
    }

    @Override
    public List<FacetFilter> getFacets() {
        return Arrays.asList((FacetFilter) criteriaFacet);
    }
}
