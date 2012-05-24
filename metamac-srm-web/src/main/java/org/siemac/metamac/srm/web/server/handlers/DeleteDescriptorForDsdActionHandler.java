package org.siemac.metamac.srm.web.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.web.server.ServiceContextHolder;
import org.siemac.metamac.srm.web.shared.DeleteDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDescriptorForDsdResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteDescriptorForDsdActionHandler extends AbstractActionHandler<DeleteDescriptorForDsdAction, DeleteDescriptorForDsdResult> {

    private static Logger        logger = Logger.getLogger(DeleteDescriptorForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDescriptorForDsdActionHandler() {
        super(DeleteDescriptorForDsdAction.class);
    }

    @Override
    public DeleteDescriptorForDsdResult execute(DeleteDescriptorForDsdAction action, ExecutionContext context) throws ActionException {
        try {
            srmCoreServiceFacade.deleteDescriptorForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(), action.getDescriptorDto());
            return new DeleteDescriptorForDsdResult();
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, " Error deleting descriptor for DSD with id = " + action.getIdDsd() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(DeleteDescriptorForDsdAction action, DeleteDescriptorForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
