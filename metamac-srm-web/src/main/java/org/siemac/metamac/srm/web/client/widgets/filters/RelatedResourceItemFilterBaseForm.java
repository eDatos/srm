package org.siemac.metamac.srm.web.client.widgets.filters;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceItemWebCriteria;
import org.siemac.metamac.web.common.client.widgets.filters.base.SimpleFilterBaseForm;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;
import org.siemac.metamac.web.common.client.widgets.filters.facets.OnlyItemSchemeLastVersionFacetFilter;

public abstract class RelatedResourceItemFilterBaseForm<T extends RelatedResourceItemWebCriteria> extends SimpleFilterBaseForm<T> {

    protected OnlyItemSchemeLastVersionFacetFilter onlyItemSchemeLastVersionFacet;

    public RelatedResourceItemFilterBaseForm() {
        super();
        onlyItemSchemeLastVersionFacet = new OnlyItemSchemeLastVersionFacetFilter();
        onlyItemSchemeLastVersionFacet.setColSpan(2);
        criteriaFacet.setColSpan(2);
    }

    @Override
    public T getSearchCriteria() {
        T searchCriteria = super.getSearchCriteria();

        onlyItemSchemeLastVersionFacet.populateCriteria(searchCriteria);

        return searchCriteria;
    }

    @Override
    public List<FacetFilter> getFacets() {
        return Arrays.asList(onlyItemSchemeLastVersionFacet, criteriaFacet);
    }
}
