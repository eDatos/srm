package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.DeleteComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteComponentForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteComponentForDsdActionHandler extends SecurityActionHandler<DeleteComponentForDsdAction, DeleteComponentForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteComponentForDsdActionHandler() {
        super(DeleteComponentForDsdAction.class);
    }

    @Override
    public DeleteComponentForDsdResult executeSecurityAction(DeleteComponentForDsdAction action) throws ActionException {
        try {
            srmCoreServiceFacade.deleteComponentForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), action.getComponentDto(), action.getTypeComponentList());
            return new DeleteComponentForDsdResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteComponentForDsdAction action, DeleteComponentForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
