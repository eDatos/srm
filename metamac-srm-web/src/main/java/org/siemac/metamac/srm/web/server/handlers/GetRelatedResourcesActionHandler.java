package org.siemac.metamac.srm.web.server.handlers;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.server.utils.MetamacCriteriaUtils;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesAction;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.constants.CommonSharedConstants;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetRelatedResourcesActionHandler extends SecurityActionHandler<GetRelatedResourcesAction, GetRelatedResourcesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetRelatedResourcesActionHandler() {
        super(GetRelatedResourcesAction.class);
    }

    @Override
    public GetRelatedResourcesResult executeSecurityAction(GetRelatedResourcesAction action) throws ActionException {

        try {
            MetamacCriteriaResult<RelatedResourceDto> result = null;
            MetamacCriteria criteria = new MetamacCriteria();
            switch (action.getRelatedArtefactsEnum()) {
                case CONCEPT_SCHEMES_WITH_DSD_PRIMARY_MEASURE:
                    criteria.setRestriction(MetamacCriteriaUtils.getConceptSchemeCriteriaDisjunctionRestriction(action.getCriteria()));
                    result = srmCoreServiceFacade.findConceptSchemesWithConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContextHolder.getCurrentServiceContext(), null, action.getDsdUrn());
                    break;
                case CONCEPTS_WITH_DSD_PRIMARY_MEASURE:
                    criteria.setRestriction(MetamacCriteriaUtils.getConceptCriteriaDisjunctionRestriction(action.getCriteria()));
                    result = srmCoreServiceFacade.findConceptsCanBeDsdPrimaryMeasureByCondition(ServiceContextHolder.getCurrentServiceContext(), null, action.getDsdUrn());
                    break;
                default:
                    throw new MetamacWebException(CommonSharedConstants.EXCEPTION_UNKNOWN, MetamacSrmWeb.getCoreMessages().exception_common_unknown());
            }
            return new GetRelatedResourcesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
