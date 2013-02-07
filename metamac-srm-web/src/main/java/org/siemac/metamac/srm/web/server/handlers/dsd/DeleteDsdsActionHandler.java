package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdsAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDsdsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDsdsActionHandler extends SecurityActionHandler<DeleteDsdsAction, DeleteDsdsResult> {

    private static Logger        logger = Logger.getLogger(DeleteDsdsActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDsdsActionHandler() {
        super(DeleteDsdsAction.class);
    }

    @Override
    public DeleteDsdsResult executeSecurityAction(DeleteDsdsAction action) throws ActionException {
        for (String urn : action.getUrns()) {
            try {
                srmCoreServiceFacade.deleteDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), urn);
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, " Error deleting dsd. " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteDsdsResult();
    }
}
