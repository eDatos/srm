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
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.criteria.VariableElementCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.VariableElementCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.srm.web.shared.criteria.VariableElementWebCriteria;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariableElementsActionHandler extends SecurityActionHandler<GetVariableElementsAction, GetVariableElementsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariableElementsActionHandler() {
        super(GetVariableElementsAction.class);
    }

    @Override
    public GetVariableElementsResult executeSecurityAction(GetVariableElementsAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(VariableElementCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        VariableElementWebCriteria variableElementWebCriteria = action.getCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Variable element Criteria
        if (StringUtils.isNotBlank(variableElementWebCriteria.getCriteria())) {
            MetamacCriteriaDisjunctionRestriction variableElementCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            variableElementCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.CODE.name(), variableElementWebCriteria.getCriteria(), OperationType.ILIKE));
            variableElementCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.NAME.name(), variableElementWebCriteria.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(variableElementCriteriaDisjuction);
        }

        // Variable restriction
        if (StringUtils.isNotBlank(variableElementWebCriteria.getVariableUrn())) {
            MetamacCriteriaPropertyRestriction variableRestriction = new MetamacCriteriaPropertyRestriction(VariableElementCriteriaPropertyEnum.VARIABLE_URN.name(),
                    variableElementWebCriteria.getVariableUrn(), OperationType.EQ);
            restriction.getRestrictions().add(variableRestriction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<VariableElementDto> result = srmCoreServiceFacade.findVariableElementsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetVariableElementsResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
