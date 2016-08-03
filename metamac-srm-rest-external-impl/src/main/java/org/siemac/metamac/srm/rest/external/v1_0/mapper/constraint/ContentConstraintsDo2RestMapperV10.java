package org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.RegionReference;

import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;
import com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue;

public interface ContentConstraintsDo2RestMapperV10 {

    public org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraints toContentConstraints(PagedResult<ContentConstraint> sources, String agencyID, String resourceID, String query,
            String orderBy, Integer limit);
    public org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraint toContentConstraint(ContentConstraint source);
    public RegionReference toRegionReference(String contentConstraintUrn, RegionValue source);
}
