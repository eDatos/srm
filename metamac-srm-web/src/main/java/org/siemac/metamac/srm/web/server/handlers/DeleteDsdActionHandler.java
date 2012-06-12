package org.siemac.metamac.srm.web.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.DeleteDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDsdActionHandler extends SecurityActionHandler<DeleteDsdAction, DeleteDsdResult> {

    private static Logger        logger = Logger.getLogger(DeleteDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDsdActionHandler() {
        super(DeleteDsdAction.class);
    }

    @Override
    public DeleteDsdResult executeSecurityAction(DeleteDsdAction action) throws ActionException {
        try {
            srmCoreServiceFacade.deleteDsd(ServiceContextHolder.getCurrentServiceContext(), action.getDataStructureDefinitionDto());
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, " Error deleting dsd. " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
        return new DeleteDsdResult();
    }

    @Override
    public void undo(DeleteDsdAction action, DeleteDsdResult result, ExecutionContext context) throws ActionException {

    }

}