package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacCriteriaUtils;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.concept.GetConceptSchemesWithConceptsCanBeRoleResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptSchemesWithConceptsCanBeRoleActionHandler extends SecurityActionHandler<GetConceptSchemesWithConceptsCanBeRoleAction, GetConceptSchemesWithConceptsCanBeRoleResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptSchemesWithConceptsCanBeRoleActionHandler() {
        super(GetConceptSchemesWithConceptsCanBeRoleAction.class);
    }

    @Override
    public GetConceptSchemesWithConceptsCanBeRoleResult executeSecurityAction(GetConceptSchemesWithConceptsCanBeRoleAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaOrderEnum.URN.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        // Criteria
        if (action.getCriteria() != null) {
            MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
            restriction.getRestrictions().add(MetamacCriteriaUtils.getConceptSchemeCriteriaRestriction(action.getCriteria()));
            criteria.setRestriction(restriction);
        }

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptSchemesByConditionWithConceptsCanBeRole(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetConceptSchemesWithConceptsCanBeRoleResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
