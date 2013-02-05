package org.siemac.metamac.srm.web.server.handlers.organisation;

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
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationSchemeWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemesActionHandler extends SecurityActionHandler<GetOrganisationSchemesAction, GetOrganisationSchemesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemesActionHandler() {
        super(GetOrganisationSchemesAction.class);
    }

    @Override
    public GetOrganisationSchemesResult executeSecurityAction(GetOrganisationSchemesAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        OrganisationSchemeWebCriteria organisationSchemeWebCriteria = action.getCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Only find last versions
        if (organisationSchemeWebCriteria.getIsLastVersion() != null) {
            MetamacCriteriaPropertyRestriction lastVersionRestriction = new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.IS_LAST_VERSION.name(),
                    organisationSchemeWebCriteria.getIsLastVersion(), OperationType.EQ);
            restriction.getRestrictions().add(lastVersionRestriction);
        }

        // Organisation scheme Criteria
        MetamacCriteriaDisjunctionRestriction organisationSchemeCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (!StringUtils.isBlank(organisationSchemeWebCriteria.getCriteria())) {
            organisationSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.CODE.name(), organisationSchemeWebCriteria.getCriteria(), OperationType.ILIKE));
            organisationSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), organisationSchemeWebCriteria.getCriteria(), OperationType.ILIKE));
            organisationSchemeCriteriaDisjuction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.URN.name(), organisationSchemeWebCriteria.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(organisationSchemeCriteriaDisjuction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        try {
            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetOrganisationSchemesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
