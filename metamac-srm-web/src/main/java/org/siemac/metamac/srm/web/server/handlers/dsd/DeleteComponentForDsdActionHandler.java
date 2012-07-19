package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static Logger        logger = Logger.getLogger(DeleteComponentForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteComponentForDsdActionHandler() {
        super(DeleteComponentForDsdAction.class);
    }

    @Override
    public DeleteComponentForDsdResult executeSecurityAction(DeleteComponentForDsdAction action) throws ActionException {
        try {
            srmCoreServiceFacade.deleteComponentForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(), action.getComponentDto(), action.getTypeComponentList());
            return new DeleteComponentForDsdResult();
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, " Error deleting component " + action.getTypeComponentList() + " for DSD with id = " + action.getIdDsd() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteComponentForDsdAction action, DeleteComponentForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
