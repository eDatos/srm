package org.siemac.metamac.srm.rest.internal.invocation;

import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.srm.rest.internal.v1_0.service.MetamacRestApisLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(StatisticalOperationsRestInternalFacade.BEAN_ID)
public class StatisticalOperationsRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private MetamacRestApisLocator restApiLocator;

    @Override
    public Operation retrieveOperation(String operationCode) {
        return restApiLocator.getStatisticalOperationsRestInternalFacadeV1_0().retrieveOperationById(operationCode);
    }
}
