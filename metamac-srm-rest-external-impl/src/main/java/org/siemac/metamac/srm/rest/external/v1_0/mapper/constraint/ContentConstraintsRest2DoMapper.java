package org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint;

import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;

import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;

public interface ContentConstraintsRest2DoMapper {

    public RestCriteria2SculptorCriteria<ContentConstraint> getContentConstraintCriteriaMapper();
}
