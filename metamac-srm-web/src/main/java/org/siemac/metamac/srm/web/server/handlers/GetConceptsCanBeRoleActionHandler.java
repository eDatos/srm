package org.siemac.metamac.srm.web.server.handlers;

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
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.ConceptMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.GetConceptsCanBeRoleAction;
import org.siemac.metamac.srm.web.shared.GetConceptsCanBeRoleResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetConceptsCanBeRoleActionHandler extends SecurityActionHandler<GetConceptsCanBeRoleAction, GetConceptsCanBeRoleResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetConceptsCanBeRoleActionHandler() {
        super(GetConceptsCanBeRoleAction.class);
    }

    @Override
    public GetConceptsCanBeRoleResult executeSecurityAction(GetConceptsCanBeRoleAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(ConceptMetamacCriteriaOrderEnum.URN.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Concept criteria
        MetamacCriteriaDisjunctionRestriction conceptCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (!StringUtils.isBlank(action.getCriteria())) {
            conceptCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.CODE.name(), action.getCriteria(), OperationType.ILIKE));
            conceptCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.NAME.name(), action.getCriteria(), OperationType.ILIKE));
            conceptCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(ConceptMetamacCriteriaPropertyEnum.URN.name(), action.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(conceptCriteriaDisjuction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<RelatedResourceDto> result = srmCoreServiceFacade.findConceptsCanBeRoleByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetConceptsCanBeRoleResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
