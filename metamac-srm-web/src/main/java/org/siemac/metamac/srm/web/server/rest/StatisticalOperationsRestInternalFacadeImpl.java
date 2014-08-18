package org.siemac.metamac.srm.web.server.rest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.OperationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.web.common.server.rest.utils.RestExceptionUtils;
import org.siemac.metamac.web.common.server.utils.WebTranslateExceptions;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatisticalOperationsRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private RestApiLocator         restApiLocator;

    @Autowired
    private RestExceptionUtils     restExceptionUtils;

    @Autowired
    private WebTranslateExceptions webTranslateExceptions;

    @Override
    public Operation retrieveOperation(ServiceContext serviceContext, String operationCode) throws MetamacWebException {
        try {
            return restApiLocator.getStatisticalOperationsRestFacadeV10().retrieveOperationById(operationCode); // OPERATION ID in the rest API is what we call CODE
        } catch (Exception e) {
            throw manageRestException(serviceContext, e);
        }
    }

    @Override
    public Operations findOperations(ServiceContext serviceContext, int firstResult, int maxResult, String[] operationCodes, String criteria) throws MetamacWebException {
        try {
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);
            String query = buildQuery(operationCodes, criteria);

            Operations findOperationsResult = restApiLocator.getStatisticalOperationsRestFacadeV10().findOperations(query, null, limit, offset);
            return findOperationsResult;
        } catch (Exception e) {
            throw manageRestException(serviceContext, e);
        }
    }

    private String buildQuery(String[] operationCodes, String criteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(criteria)) {
            queryBuilder.append("(").append(OperationCriteriaPropertyRestriction.TITLE).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(OperationCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"").append(")");
        }
        if (ArrayUtils.isNotEmpty(operationCodes)) {
            if (StringUtils.isNotBlank(queryBuilder.toString())) {
                queryBuilder.append(" ").append(LogicalOperator.AND).append(" ");
            }
            queryBuilder.append("(");
            for (int i = 0; i < operationCodes.length; i++) {
                if (i != 0) {
                    queryBuilder.append(" ").append(LogicalOperator.OR).append(" ");
                }
                queryBuilder.append(OperationCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.EQ.name()).append(" \"").append(operationCodes[i]).append("\"");
            }
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }

    //
    // EXCEPTION HANDLERS
    //

    private MetamacWebException manageRestException(ServiceContext ctx, Exception e) throws MetamacWebException {
        return restExceptionUtils.manageMetamacRestException(ctx, e, ServiceExceptionParameters.API_SRM_INTERNAL, restApiLocator.getStatisticalOperationsRestFacadeV10());
    }
}
