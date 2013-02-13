package org.siemac.metamac.srm.web.server.rest;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.OperationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticalOperationsRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private RestApiLocator restApiLocator;

    @Override
    public Operation retrieveOperation(String operationCode) throws MetamacWebException {
        try {
            return restApiLocator.getStatisticalOperationsRestFacadeV10().retrieveOperationById(operationCode); // OPERATION ID in the rest API is what we call CODE
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = e.toErrorObject(WebClient.client(restApiLocator.getStatisticalOperationsRestFacadeV10()),
                    org.siemac.metamac.rest.common.v1_0.domain.Exception.class);
            throw WebExceptionUtils.createMetamacWebException(exception);
        } catch (Exception e) {
            throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "Error retrieving statistical operation");
        }
    }

    @Override
    public Operations findOperations(int firstResult, int maxResult, String operation) throws MetamacWebException {
        try {
            String query = null;
            if (!StringUtils.isBlank(operation)) {
                query = OperationCriteriaPropertyRestriction.TITLE + " " + ComparisonOperator.ILIKE.name() + " \"" + operation + "\"";
                query += " " + LogicalOperator.OR.name() + " " + OperationCriteriaPropertyRestriction.ID + " " + ComparisonOperator.ILIKE.name() + " \"" + operation + "\"";
            }

            // Pagination
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);

            Operations findOperationsResult = restApiLocator.getStatisticalOperationsRestFacadeV10().findOperations(query, null, limit, offset);
            return findOperationsResult;
        } catch (ServerWebApplicationException e) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = e.toErrorObject(WebClient.client(restApiLocator.getStatisticalOperationsRestFacadeV10()),
                    org.siemac.metamac.rest.common.v1_0.domain.Exception.class);
            throw WebExceptionUtils.createMetamacWebException(exception);
        } catch (Exception e) {
            throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "Error finding statistical operations");
        }
    }
}
