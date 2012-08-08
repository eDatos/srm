package org.siemac.metamac.srm.web.server.rest;

import org.siemac.metamac.statistical_operations.rest.internal.v1_0.domain.Operation;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.domain.Operations;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public interface StatisticalOperationsRestInternalFacade {

    public Operation retrieveOperation(String operationCode) throws MetamacWebException;

    public Operations findOperations(int firstResult, int maxResult, String operation) throws MetamacWebException;

}
