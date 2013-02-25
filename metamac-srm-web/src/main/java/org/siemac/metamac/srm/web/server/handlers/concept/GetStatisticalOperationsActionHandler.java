package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.srm.core.security.shared.SharedSecurityUtils;
import org.siemac.metamac.srm.web.server.rest.RestApiConstants;
import org.siemac.metamac.srm.web.server.rest.StatisticalOperationsRestInternalFacade;
import org.siemac.metamac.srm.web.server.utils.ExternalItemUtils;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.utils.SecurityUtils;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetStatisticalOperationsActionHandler extends SecurityActionHandler<GetStatisticalOperationsAction, GetStatisticalOperationsResult> {

    @Autowired
    private StatisticalOperationsRestInternalFacade statisticalOperationsRestInternalFacade;

    @Autowired
    private ConfigurationService                    configurationService;

    public GetStatisticalOperationsActionHandler() {
        super(GetStatisticalOperationsAction.class);
    }

    @Override
    public GetStatisticalOperationsResult executeSecurityAction(GetStatisticalOperationsAction action) throws ActionException {

        String operationsApiEndpoint = configurationService.getProperty(RestApiConstants.STATISTICAL_OPERATIONS_REST_INTERNAL);

        try {

            int firstResult = action.getFirstResult();
            int maxResults = action.getMaxResults();
            String criteria = action.getCriteria();

            // Operation that the user can access to. If this list is empty, the user can access to all operations.
            Set<String> userOperationCodes = getUserOperations();

            Operations result = null;

            if (SharedSecurityUtils.isAdministrador(SecurityUtils.getMetamacPrincipal(ServiceContextHolder.getCurrentServiceContext())) || (userOperationCodes == null || userOperationCodes.isEmpty())) {
                // THE USER CAN ACCESS TO ALL OPERATIONS
                result = statisticalOperationsRestInternalFacade.findOperations(firstResult, maxResults, null, criteria);
            } else {
                // THE USER ONLY HAS ACCESS TO SOME OPERATIONS
                // If the user only has access to some operations, find these operations.
                // In this situation, it is necessary to call the API to get the operation names.
                result = statisticalOperationsRestInternalFacade.findOperations(firstResult, maxResults, userOperationCodes.toArray(new String[userOperationCodes.size()]), criteria);
            }

            if (result != null && result.getOperations() != null) {
                int firstResultOut = result.getOffset().intValue();
                int totalResults = result.getTotal().intValue();
                List<ExternalItemDto> externalItemDtos = ExternalItemUtils.getOperationsAsExternalItemDtos(result.getOperations(), operationsApiEndpoint);
                return new GetStatisticalOperationsResult(externalItemDtos, firstResultOut, totalResults);
            } else {
                // There is no operations
                return new GetStatisticalOperationsResult(new ArrayList<ExternalItemDto>(), 0, 0);
            }

        } catch (MetamacException e) {
            throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "Error getting MetamacPrincipal from ServiceContext");
        }
    }

    private Set<String> getUserOperations() throws MetamacWebException {
        try {
            MetamacPrincipal metamacPrincipal = SecurityUtils.getMetamacPrincipal(ServiceContextHolder.getCurrentServiceContext());
            return SharedSecurityUtils.getOperationCodesFromMetamacPrincipalInApplication(metamacPrincipal);
        } catch (MetamacException e) {
            throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, "Error getting MetamacPrincipal from ServiceContext");
        }
    }
}
