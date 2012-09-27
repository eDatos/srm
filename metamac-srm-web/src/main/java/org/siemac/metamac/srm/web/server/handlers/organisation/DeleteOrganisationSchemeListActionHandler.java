package org.siemac.metamac.srm.web.server.handlers.organisation;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListAction;
import org.siemac.metamac.srm.web.shared.organisation.DeleteOrganisationSchemeListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteOrganisationSchemeListActionHandler extends SecurityActionHandler<DeleteOrganisationSchemeListAction, DeleteOrganisationSchemeListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteOrganisationSchemeListActionHandler() {
        super(DeleteOrganisationSchemeListAction.class);
    }

    @Override
    public DeleteOrganisationSchemeListResult executeSecurityAction(DeleteOrganisationSchemeListAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteOrganisationScheme(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteOrganisationSchemeListResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
