package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.criteria.DataStructureDefinitionCriteriaOrderEnum;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdListActionHandler extends SecurityActionHandler<GetDsdListAction, GetDsdListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdListActionHandler() {
        super(GetDsdListAction.class);
    }

    @Override
    public GetDsdListResult executeSecurityAction(GetDsdListAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();
        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(DataStructureDefinitionCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);
        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);
        try {
            MetamacCriteriaResult<DataStructureDefinitionDto> result = srmCoreServiceFacade.findDsdByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetDsdListResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
