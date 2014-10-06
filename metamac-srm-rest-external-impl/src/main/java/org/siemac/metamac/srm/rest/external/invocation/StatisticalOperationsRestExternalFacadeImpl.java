package org.siemac.metamac.srm.rest.external.invocation;

import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;
import org.siemac.metamac.srm.rest.external.v1_0.service.MetamacRestExternalApisLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(StatisticalOperationsRestExternalFacade.BEAN_ID)
public class StatisticalOperationsRestExternalFacadeImpl implements StatisticalOperationsRestExternalFacade {

    @Autowired
    private MetamacRestExternalApisLocator restApiLocator;

    @Override
    public Operation retrieveOperation(String operationCode) {
        return restApiLocator.getStatisticalOperationsV1_0().retrieveOperationById(operationCode);
    }
}
