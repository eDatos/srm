package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionsAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemeVersionsActionHandler extends SecurityActionHandler<GetOrganisationSchemeVersionsAction, GetOrganisationSchemeVersionsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemeVersionsActionHandler() {
        super(GetOrganisationSchemeVersionsAction.class);
    }

    @Override
    public GetOrganisationSchemeVersionsResult executeSecurityAction(GetOrganisationSchemeVersionsAction action) throws ActionException {
        try {
            List<OrganisationSchemeMetamacBasicDto> versions = srmCoreServiceFacade.retrieveOrganisationSchemeVersions(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetOrganisationSchemeVersionsResult(versions);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
