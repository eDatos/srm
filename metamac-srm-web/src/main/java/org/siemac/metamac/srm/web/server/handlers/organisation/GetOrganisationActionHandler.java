package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationActionHandler extends SecurityActionHandler<GetOrganisationAction, GetOrganisationResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationActionHandler() {
        super(GetOrganisationAction.class);
    }

    @Override
    public GetOrganisationResult executeSecurityAction(GetOrganisationAction action) throws ActionException {
        try {
            OrganisationMetamacDto organisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetOrganisationResult(organisationMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
