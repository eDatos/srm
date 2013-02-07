package org.siemac.metamac.srm.web.server.handlers.category;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CategoryMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.server.utils.MetamacCriteriaUtils;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoriesResult;
import org.siemac.metamac.srm.web.shared.criteria.CategoryWebCriteria;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCategoriesActionHandler extends SecurityActionHandler<GetCategoriesAction, GetCategoriesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCategoriesActionHandler() {
        super(GetCategoriesAction.class);
    }

    @Override
    public GetCategoriesResult executeSecurityAction(GetCategoriesAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(CategoryMetamacCriteriaOrderEnum.CATEGORY_SCHEME_URN.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        CategoryWebCriteria categoryWebCriteria = action.getCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Criteria
        if (!StringUtils.isBlank(categoryWebCriteria.getCriteria())) {
            restriction.getRestrictions().add(MetamacCriteriaUtils.getCategoryCriteriaRestriction(categoryWebCriteria));
        }

        // Is externally published
        if (categoryWebCriteria.getIsExternallyPublished() != null) {
            MetamacCriteriaPropertyRestriction procStatusRestriction = new MetamacCriteriaPropertyRestriction(CategoryMetamacCriteriaPropertyEnum.CATEGORY_SCHEME_EXTERNALLY_PUBLISHED.name(),
                    categoryWebCriteria.getIsExternallyPublished(), OperationType.EQ);
            restriction.getRestrictions().add(procStatusRestriction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<CategoryMetamacDto> result = srmCoreServiceFacade.findCategoriesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetCategoriesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
