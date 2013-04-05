package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.server.utils.MetamacWebCriteriaUtils;
import org.siemac.metamac.srm.web.shared.criteria.OrganisationContactWebCriteria;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationContactsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationContactsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.organisation.ContactDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationContactsActionHandler extends SecurityActionHandler<GetOrganisationContactsAction, GetOrganisationContactsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationContactsActionHandler() {
        super(GetOrganisationContactsAction.class);
    }

    @Override
    public GetOrganisationContactsResult executeSecurityAction(GetOrganisationContactsAction action) throws ActionException {
        MetamacCriteria criteria = new MetamacCriteria();

        OrganisationContactWebCriteria organisationContactWebCriteria = action.getCriteria();

        MetamacCriteriaConjunctionRestriction restriction = new MetamacCriteriaConjunctionRestriction();

        // Contacts criteria
        restriction.getRestrictions().add(MetamacWebCriteriaUtils.getContactCriteriaRestriction(organisationContactWebCriteria));
        criteria.setRestriction(restriction);

        // Do not paginate the contact list
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setFirstResult(0);
        criteria.getPaginator().setMaximumResultSize(SrmWebConstants.NO_LIMIT_IN_PAGINATION);

        try {
            MetamacCriteriaResult<ContactDto> result = srmCoreServiceFacade.findOrganisationContactsByCondition(ServiceContextHolder.getCurrentServiceContext(), criteria);
            return new GetOrganisationContactsResult(result.getResults());
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
