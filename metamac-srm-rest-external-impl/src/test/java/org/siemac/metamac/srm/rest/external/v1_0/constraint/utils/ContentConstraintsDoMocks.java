package org.siemac.metamac.srm.rest.external.v1_0.constraint.utils;

import org.siemac.metamac.srm.core.constraint.serviceapi.utils.ConstraintsMetamacDoMocks;

import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;

public class ContentConstraintsDoMocks {

    public static ContentConstraint mockContentConstraint(String agencyID, String resourceID, String version) {
        return ConstraintsMetamacDoMocks.mockContentConstraintWithKeysFixedValues("prefix", agencyID, resourceID, version);
    }
}
