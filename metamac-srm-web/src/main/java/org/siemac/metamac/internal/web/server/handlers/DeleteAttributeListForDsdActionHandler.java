package org.siemac.metamac.internal.web.server.handlers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core_facades.serviceapi.SDMXStructureServiceFacade;
import org.siemac.metamac.domain_dto.DataAttributeDto;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.DeleteAttributeListForDsdAction;
import org.siemac.metamac.internal.web.shared.DeleteAttributeListForDsdResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteAttributeListForDsdActionHandler extends AbstractActionHandler<DeleteAttributeListForDsdAction, DeleteAttributeListForDsdResult> {

    private static Logger              logger = Logger.getLogger(DeleteAttributeListForDsdActionHandler.class.getName());

    @Autowired
    private SDMXStructureServiceFacade sDMXStructureServiceFacade;

    public DeleteAttributeListForDsdActionHandler() {
        super(DeleteAttributeListForDsdAction.class);
    }

    @Override
    public DeleteAttributeListForDsdResult execute(DeleteAttributeListForDsdAction action, ExecutionContext context) throws ActionException {
        List<DataAttributeDto> dataAttributeDtos = action.getDataAttributeDtos();
        for (DataAttributeDto a : dataAttributeDtos) {
            try {
                sDMXStructureServiceFacade.deleteComponentForDsd(ServiceContextHelper.getServiceContext(), action.getIdDsd(), a, action.getTypeComponentList());
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, " Error deleting attribute " + action.getTypeComponentList() + " for DSD with id = " + action.getIdDsd() + ". " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteAttributeListForDsdResult();
    }

    @Override
    public void undo(DeleteAttributeListForDsdAction action, DeleteAttributeListForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
