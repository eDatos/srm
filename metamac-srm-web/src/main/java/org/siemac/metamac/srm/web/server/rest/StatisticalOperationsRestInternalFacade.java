package org.siemac.metamac.srm.web.server.rest;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface StatisticalOperationsRestInternalFacade {

    public Operation retrieveOperation(ServiceContext serviceContext, String operationCode) throws MetamacWebException;
    public Operations findOperations(ServiceContext serviceContext, int firstResult, int maxResult, String[] operationCodes, String criteria) throws MetamacWebException;
}
