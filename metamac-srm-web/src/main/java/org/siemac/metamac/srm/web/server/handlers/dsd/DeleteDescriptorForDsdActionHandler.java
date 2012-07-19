package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDescriptorForDsdActionHandler extends SecurityActionHandler<DeleteDescriptorForDsdAction, DeleteDescriptorForDsdResult> {

    private static Logger        logger = Logger.getLogger(DeleteDescriptorForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDescriptorForDsdActionHandler() {
        super(DeleteDescriptorForDsdAction.class);
    }

    @Override
    public DeleteDescriptorForDsdResult executeSecurityAction(DeleteDescriptorForDsdAction action) throws ActionException {
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
