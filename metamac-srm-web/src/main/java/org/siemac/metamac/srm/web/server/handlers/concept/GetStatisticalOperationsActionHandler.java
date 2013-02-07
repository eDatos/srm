package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.srm.web.server.rest.RestApiConstants;
import org.siemac.metamac.srm.web.server.rest.StatisticalOperationsRestInternalFacade;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsAction;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.DtoUtils;
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

        int firstResult = 0;
        int totalResults = 0;
        List<ExternalItemDto> externalItemDtos = new ArrayList<ExternalItemDto>();
        Operations result = statisticalOperationsRestInternalFacade.findOperations(action.getFirstResult(), action.getMaxResults(), action.getOperation());
        if (result != null && result.getOperations() != null) {
            firstResult = result.getOffset().intValue();
            totalResults = result.getTotal().intValue();
            for (Resource resource : result.getOperations()) {
                // Do not store rest api endpoint
                String uri = StringUtils.removeStart(resource.getSelfLink().getHref(), operationsApiEndpoint);

                ExternalItemDto externalItemDto = new ExternalItemDto(resource.getId(), uri, resource.getUrn(), TypeExternalArtefactsEnum.STATISTICAL_OPERATION,
                        DtoUtils.getInternationalStringDtoFromInternationalString(resource.getTitle()));
                externalItemDtos.add(externalItemDto);

            }
        }
        return new GetStatisticalOperationsResult(externalItemDtos, firstResult, totalResults);
    }

}
