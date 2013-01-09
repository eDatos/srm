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
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.criteria.CodelistFamilyCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.CodelistFamilyCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.GetCodelistFamiliesResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetCodelistFamiliesActionHandler extends SecurityActionHandler<GetCodelistFamiliesAction, GetCodelistFamiliesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetCodelistFamiliesActionHandler() {
        super(GetCodelistFamiliesAction.class);
    }

    @Override
    public GetCodelistFamiliesResult executeSecurityAction(GetCodelistFamiliesAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        // Order
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.DESC);
        order.setPropertyName(CodelistFamilyCriteriaOrderEnum.LAST_UPDATED.name());
        List<MetamacCriteriaOrder> criteriaOrders = new ArrayList<MetamacCriteriaOrder>();
        criteriaOrders.add(order);
        criteria.setOrdersBy(criteriaOrders);

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Family Criteria
        MetamacCriteriaDisjunctionRestriction codelistCriteriaDisjuction = new MetamacCriteriaDisjunctionRestriction();
        if (!StringUtils.isBlank(action.getCriteria())) {
            codelistCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistFamilyCriteriaPropertyEnum.CODE.name(), action.getCriteria(), OperationType.ILIKE));
            codelistCriteriaDisjuction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(CodelistFamilyCriteriaPropertyEnum.NAME.name(), action.getCriteria(), OperationType.ILIKE));
            restriction.getRestrictions().add(codelistCriteriaDisjuction);
        }

        criteria.setRestriction(restriction);

        // Pagination
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(action.getFirstResult());
        criteria.getPaginator().setMaximumResultSize(action.getMaxResults());
        criteria.getPaginator().setCountTotalResults(true);

        // TODO
        // try {
        // MetamacCriteriaResult<CodelistMetamacDto> result = srmCoreServiceFacade.findCodelistsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
        // return new GetCodelistFamiliesResult(result.getResults(), result.getPaginatorResult().getFirstResult(), result.getPaginatorResult().getTotalResults());
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }

        List<CodelistFamilyDto> families = new ArrayList<CodelistFamilyDto>();
        families.add(createFamily("codelist-family-0001"));
        families.add(createFamily("codelist-family-0002"));
        families.add(createFamily("codelist-family-0003"));
        families.add(createFamily("codelist-family-0004"));
        families.add(createFamily("codelist-family-0005"));
        return new GetCodelistFamiliesResult(families, 1, 5);
    }

    private CodelistFamilyDto createFamily(String code) {
        CodelistFamilyDto family = new CodelistFamilyDto();
        family.setCode(code);
        family.setUrn("urn:" + code);

        InternationalStringDto name = new InternationalStringDto();
        LocalisedStringDto es = new LocalisedStringDto();
        es.setLocale("es");
        es.setLabel(code);
        LocalisedStringDto pt = new LocalisedStringDto();
        pt.setLocale("pt");
        pt.setLabel(code);
        name.addText(es);
        name.addText(pt);
        family.setName(name);

        return family;
    }
}
