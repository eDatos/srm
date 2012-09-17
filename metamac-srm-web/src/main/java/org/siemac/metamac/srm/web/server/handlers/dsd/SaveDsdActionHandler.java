package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveDsdActionHandler extends SecurityActionHandler<SaveDsdAction, SaveDsdResult> {

    private static Logger        logger = Logger.getLogger(SaveDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveDsdActionHandler() {
        super(SaveDsdAction.class);
    }

    @Override
    public SaveDsdResult executeSecurityAction(SaveDsdAction action) throws ActionException {
        try {
            DataStructureDefinitionMetamacDto dsdToSave = action.getDsd();
            DataStructureDefinitionMetamacDto savedDsd = null;
            if (dsdToSave.getId() == null) {
                savedDsd = srmCoreServiceFacade.createDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsdToSave);
            } else {
                savedDsd = srmCoreServiceFacade.updateDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), dsdToSave);
            }
            return new SaveDsdResult(savedDsd);
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error saving DSD with id = " + action.getDsd().getId() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveDsdAction action, SaveDsdResult result, ExecutionContext context) throws ActionException {

    }

}
