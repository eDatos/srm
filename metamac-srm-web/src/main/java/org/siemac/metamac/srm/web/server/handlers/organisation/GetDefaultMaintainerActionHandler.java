package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.web.shared.organisation.GetDefaultMaintainerAction;
import org.siemac.metamac.srm.web.shared.organisation.GetDefaultMaintainerResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDefaultMaintainerActionHandler extends SecurityActionHandler<GetDefaultMaintainerAction, GetDefaultMaintainerResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDefaultMaintainerActionHandler() {
        super(GetDefaultMaintainerAction.class);
    }

    @Override
    public GetDefaultMaintainerResult executeSecurityAction(GetDefaultMaintainerAction action) throws ActionException {
        try {
            OrganisationMetamacDto organisationMetamacDto = srmCoreServiceFacade.retrieveMaintainerDefault(ServiceContextHolder.getCurrentServiceContext());
            return new GetDefaultMaintainerResult(organisationMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
