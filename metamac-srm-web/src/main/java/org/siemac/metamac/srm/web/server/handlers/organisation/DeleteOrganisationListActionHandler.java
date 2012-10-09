package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationListAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationListResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteOrganisationListActionHandler extends SecurityActionHandler<DeleteOrganisationListAction, DeleteOrganisationListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteOrganisationListActionHandler() {
        super(DeleteOrganisationListAction.class);
    }

    @Override
    public DeleteOrganisationListResult executeSecurityAction(DeleteOrganisationListAction action) throws ActionException {
        // try {
        // for (String urn : action.getUrns()) {
        // srmCoreServiceFacade.deleteOrganisation(ServiceContextHolder.getCurrentServiceContext(), urn);
        // }
        // return new DeleteOrganisationListResult();
        // } catch (MetamacException e) {
        // throw WebExceptionUtils.createMetamacWebException(e);
        // }
        return new DeleteOrganisationListResult();
    }

}
