package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.criteria.VariableCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.VariableCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.code.GetVariablesAction;
import org.siemac.metamac.srm.web.shared.code.GetVariablesResult;
import org.siemac.metamac.srm.web.shared.criteria.VariableWebCriteria;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariablesActionHandler extends SecurityActionHandler<GetVariablesAction, GetVariablesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariablesActionHandler() {
        super(GetVariablesAction.class);
    }

    @Override
    public GetVariablesResult executeSecurityAction(GetVariablesAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(VariableCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Variable Criteria
        VariableWebCriteria variableWebCriteria = action.getCriteria();

        // Codelist criteria
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getVariableCriteriaRestriction(variableWebCriteria));

        // Variable family restriction
        if (StringUtils.isNotBlank(action.getVariableFamilyUrn())) {
            MetamacCriteriaPropertyRestriction familyRestriction = new MetamacCriteriaPropertyRestriction(VariableCriteriaPropertyEnum.VARIABLE_FAMILY_URN.name(), action.getVariableFamilyUrn(),
                    OperationType.EQ);
            restriction.getRestrictions().add(familyRestriction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<VariableBasicDto> result = srmCoreServiceFacade.findVariablesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetVariablesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
