package org.siemac.metamac.srm.rest.internal.v1_0.mapper.constraint;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;

import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;

public interface ContentConstraintsRest2DoMapper {

    public RestCriteria2SculptorCriteria<ContentConstraint> getContentConstraintCriteriaMapper();

    public ContentConstraint contentConstraintRestToEntity(ServiceContext ctx, org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint source) throws MetamacException;
}
