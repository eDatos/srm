package org.siemac.metamac.srm.web.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.ServiceContextHolder;
import org.siemac.metamac.srm.web.shared.DeleteDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDsdResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteDsdActionHandler extends AbstractActionHandler<DeleteDsdAction, DeleteDsdResult> {

    private static Logger        logger = Logger.getLogger(DeleteDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDsdActionHandler() {
        super(DeleteDsdAction.class);
    }

    @Override
    public DeleteDsdResult execute(DeleteDsdAction action, ExecutionContext context) throws ActionException {
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