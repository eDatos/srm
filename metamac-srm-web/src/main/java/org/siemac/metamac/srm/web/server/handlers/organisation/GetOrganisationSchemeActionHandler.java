package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemeActionHandler extends SecurityActionHandler<GetOrganisationSchemeAction, GetOrganisationSchemeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemeActionHandler() {
        super(GetOrganisationSchemeAction.class);
    }

    @Override
    public GetOrganisationSchemeResult executeSecurityAction(GetOrganisationSchemeAction action) throws ActionException {
        try {
            OrganisationSchemeMetamacDto organisationSchemeMetamacDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetOrganisationSchemeResult(organisationSchemeMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
