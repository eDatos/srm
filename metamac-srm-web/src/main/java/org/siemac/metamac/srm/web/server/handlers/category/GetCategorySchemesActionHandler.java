package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategorySchemesResult;
import org.siemac.metamac.srm.web.shared.criteria.CategorySchemeWebCriteria;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategorySchemesActionHandler extends SecurityActionHandler<GetCategorySchemesAction, GetCategorySchemesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategorySchemesActionHandler() {
        super(GetCategorySchemesAction.class);
    }

    @Override
    public GetCategorySchemesResult executeSecurityAction(GetCategorySchemesAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.RESOURCE_LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        CategorySchemeWebCriteria categorySchemeWebCriteria = action.getCategorySchemeWebCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Category scheme Criteria
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getCategorySchemeCriteriaRestriction(categorySchemeWebCriteria));
        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<CategorySchemeMetamacBasicDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetCategorySchemesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
