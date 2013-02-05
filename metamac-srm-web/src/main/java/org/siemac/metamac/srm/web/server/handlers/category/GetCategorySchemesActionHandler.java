package org.siemac.metamac.srm.web.server.handlers.category;

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
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
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
        order.setPropertyName(CategorySchemeVersionMetamacCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        CategorySchemeWebCriteria categorySchemeWebCriteria = action.getCategorySchemeWebCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Only find last versions
        if (categorySchemeWebCriteria.getIsLastVersion() != null) {
            MetamacCriteriaPropertyRestriction lastVersionRestriction = new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(),
                    categorySchemeWebCriteria.getIsLastVersion(), OperationType.EQ);
            restriction.getRestrictions().add(lastVersionRestriction);
        }

        // Category scheme Criteria
        MetamacCriteriaDisjunctionRestriction categorySchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (!StringUtils.isBlank(categorySchemeWebCriteria.getCriteria())) {
            categorySchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), categorySchemeWebCriteria.getCriteria(), OperationType.ILIKE));
            categorySchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(CategorySchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), categorySchemeWebCriteria.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(categorySchemeCriteriaDisjuction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<CategorySchemeMetamacDto> result = srmCoreServiceFacade.findCategorySchemesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetCategorySchemesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
