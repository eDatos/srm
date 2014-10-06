package org.siemac.metamac.srm.rest.external.invocation;

import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

public interface StatisticalOperationsRestExternalFacade {

    public static final String BEAN_ID = "statisticalOperationsRestExternalFacade";

    public Operation retrieveOperation(String operationCode);
}
