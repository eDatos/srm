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
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistsActionHandler extends SecurityActionHandler<GetCodelistsAction, GetCodelistsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistsActionHandler() {
        super(GetCodelistsAction.class);
    }

    @Override
    public GetCodelistsResult executeSecurityAction(GetCodelistsAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(CodelistVersionMetamacCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Only find last versions
        MetamacCriteriaPropertyRestriction lastVersionRestriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(), Boolean.TRUE,
                OperationType.EQ);
        restriction.getRestrictions().add(lastVersionRestriction);

        // Codelist Criteria
        if (StringUtils.isNotBlank(action.getCriteria())) {
            MetamacCriteriaDisjunctionRestriction codelistCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
            codelistCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.CODE.name(), action.getCriteria(), OperationType.ILIKE));
            codelistCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.NAME.name(), action.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(codelistCriteriaDisjuction);
        }

        // Proc status restriction
        if (action.getProcStatus() != null) {
            MetamacCriteriaPropertyRestriction procStatusRestriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(), action.getProcStatus(),
                    OperationType.EQ);
            restriction.getRestrictions().add(procStatusRestriction);
        }

        // Codelist family restriction
        if (StringUtils.isNotBlank(action.getCodelistFamilyUrn())) {
            MetamacCriteriaPropertyRestriction familyRestriction = new MetamacCriteriaPropertyRestriction(CodelistVersionMetamacCriteriaPropertyEnum.CODELIST_FAMILY_URN.name(),
                    action.getProcStatus(), OperationType.EQ);
            restriction.getRestrictions().add(familyRestriction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<CodelistMetamacDto> result = srmCoreServiceFacade.findCodelistsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetCodelistsResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
