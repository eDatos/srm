package org.siemac.metamac.srm.web.server.handlers.concept;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaDisjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
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
        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();
        if (!StringUtils.isBlank(action.getCriteria())) {
            MetamacCriteriaDisjunctionRestriction conceptSchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), action.getCriteria(), OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), action.getCriteria(), OperationType.ILIKE));
            conceptSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), action.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(conceptSchemeCriteriaDisjuction);
        }
        criteria.setRestriction(restriction);

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
