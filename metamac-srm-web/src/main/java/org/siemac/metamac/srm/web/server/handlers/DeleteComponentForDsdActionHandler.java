package org.siemac.metamac.srm.web.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.ServiceContextHelper;
import org.siemac.metamac.srm.web.shared.DeleteComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteComponentForDsdResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteComponentForDsdActionHandler extends AbstractActionHandler<DeleteComponentForDsdAction, DeleteComponentForDsdResult> {

    private static Logger        logger = Logger.getLogger(DeleteComponentForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteComponentForDsdActionHandler() {
        super(DeleteComponentForDsdAction.class);
    }

    @Override
    public DeleteComponentForDsdResult execute(DeleteComponentForDsdAction action, ExecutionContext context) throws ActionException {
        try {
            srmCoreServiceFacade.deleteComponentForDsd(ServiceContextHelper.getServiceContext(), action.getIdDsd(), action.getComponentDto(), action.getTypeComponentList());
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
