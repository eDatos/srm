package org.siemac.metamac.srm.rest.external.v1_0.mapper.category;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;

public interface CategoriesRest2DoMapper {

    public RestCriteria2SculptorCriteria<CategorySchemeVersionMetamac> getCategorySchemeCriteriaMapper();
    public RestCriteria2SculptorCriteria<CategoryMetamac> getCategoryCriteriaMapper();
    public RestCriteria2SculptorCriteria<Categorisation> getCategorisationCriteriaMapper();
}
