package org.siemac.metamac.srm.web.server.handlers.code;

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
import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.criteria.VariableFamilyCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.VariableFamilyCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableFamiliesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariableFamiliesActionHandler extends SecurityActionHandler<GetVariableFamiliesAction, GetVariableFamiliesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariableFamiliesActionHandler() {
        super(GetVariableFamiliesAction.class);
    }

    @Override
    public GetVariableFamiliesResult executeSecurityAction(GetVariableFamiliesAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(VariableFamilyCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Family Criteria
        MetamacCriteriaDisjunctionRestriction variableCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (!StringUtils.isBlank(action.getCriteria())) {
            variableCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableFamilyCriteriaPropertyEnum.CODE.name(), action.getCriteria(), OperationType.ILIKE));
            variableCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(VariableFamilyCriteriaPropertyEnum.NAME.name(), action.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(variableCriteriaDisjuction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<VariableFamilyBasicDto> result = srmCoreServiceFacade.findVariableFamiliesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetVariableFamiliesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
