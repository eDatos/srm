package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DimensionComponentDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDimensionListForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDimensionListForDsdActionHandler extends SecurityActionHandler<DeleteDimensionListForDsdAction, DeleteDimensionListForDsdResult> {

    private static Logger        logger = Logger.getLogger(DeleteDimensionListForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDimensionListForDsdActionHandler() {
        super(DeleteDimensionListForDsdAction.class);
    }

    @Override
    public DeleteDimensionListForDsdResult executeSecurityAction(DeleteDimensionListForDsdAction action) throws ActionException {
        List<DimensionComponentDto> dimensionComponentDtos = action.getDimensionComponentDtos();
        for (DimensionComponentDto d : dimensionComponentDtos) {
            try {
                srmCoreServiceFacade.deleteComponentForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(), d, action.getTypeComponentList());
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, " Error deleting dimension " + action.getTypeComponentList() + " for DSD with id = " + action.getIdDsd() + ". " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteDimensionListForDsdResult();
    }

    @Override
    public void undo(DeleteDimensionListForDsdAction action, DeleteDimensionListForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
