package org.siemac.metamac.srm.web.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.ComponentDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.SaveComponentForDsdAction;
import org.siemac.metamac.srm.web.shared.SaveComponentForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveComponentForDsdActionHandler extends AbstractActionHandler<SaveComponentForDsdAction, SaveComponentForDsdResult> {

    private static Logger        logger = Logger.getLogger(SaveComponentForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveComponentForDsdActionHandler() {
        super(SaveComponentForDsdAction.class);
    }

    @Override
    public SaveComponentForDsdResult execute(SaveComponentForDsdAction action, ExecutionContext context) throws ActionException {
        try {
            ComponentDto componentDto = srmCoreServiceFacade.saveComponentForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(), action.getComponentDto(),
                    action.getTypeComponentList());
            return new SaveComponentForDsdResult(componentDto);
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, " Error saving component " + action.getTypeComponentList() + " for DSD with id = " + action.getIdDsd() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveComponentForDsdAction action, SaveComponentForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
