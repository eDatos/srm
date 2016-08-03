package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCodelistsActionHandler extends SecurityActionHandler<DeleteCodelistsAction, DeleteCodelistsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCodelistsActionHandler() {
        super(DeleteCodelistsAction.class);
    }

    @Override
    public DeleteCodelistsResult executeSecurityAction(DeleteCodelistsAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteCodelist(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteCodelistsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
