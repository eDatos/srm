package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationsAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteOrganisationsActionHandler extends SecurityActionHandler<DeleteOrganisationsAction, DeleteOrganisationsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteOrganisationsActionHandler() {
        super(DeleteOrganisationsAction.class);
    }

    @Override
    public DeleteOrganisationsResult executeSecurityAction(DeleteOrganisationsAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteOrganisation(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteOrganisationsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
