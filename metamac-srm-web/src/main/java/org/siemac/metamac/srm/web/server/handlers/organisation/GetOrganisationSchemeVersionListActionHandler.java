package org.siemac.metamac.srm.web.server.handlers.organisation;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionListAction;
import org.siemac.metamac.srm.web.shared.organisation.GetOrganisationSchemeVersionListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetOrganisationSchemeVersionListActionHandler extends SecurityActionHandler<GetOrganisationSchemeVersionListAction, GetOrganisationSchemeVersionListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetOrganisationSchemeVersionListActionHandler() {
        super(GetOrganisationSchemeVersionListAction.class);
    }

    @Override
    public GetOrganisationSchemeVersionListResult executeSecurityAction(GetOrganisationSchemeVersionListAction action) throws ActionException {
        try {
            List<OrganisationSchemeMetamacDto> versions = srmCoreServiceFacade.retrieveOrganisationSchemeVersions(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetOrganisationSchemeVersionListResult(versions);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
