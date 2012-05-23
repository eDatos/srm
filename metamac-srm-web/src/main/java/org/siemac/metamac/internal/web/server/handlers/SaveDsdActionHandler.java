package org.siemac.metamac.internal.web.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.SaveDsdAction;
import org.siemac.metamac.internal.web.shared.SaveDsdResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveDsdActionHandler extends AbstractActionHandler<SaveDsdAction, SaveDsdResult> {

    private static Logger        logger = Logger.getLogger(SaveDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveDsdActionHandler() {
        super(SaveDsdAction.class);
    }

    @Override
    public SaveDsdResult execute(SaveDsdAction action, ExecutionContext context) throws ActionException {
        try {
            DataStructureDefinitionDto dsd = srmCoreServiceFacade.saveDsd(ServiceContextHelper.getServiceContext(), action.getDsd());
            return new SaveDsdResult(dsd);
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error saving DSD with id = " + action.getDsd().getId() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveDsdAction action, SaveDsdResult result, ExecutionContext context) throws ActionException {

    }

}
