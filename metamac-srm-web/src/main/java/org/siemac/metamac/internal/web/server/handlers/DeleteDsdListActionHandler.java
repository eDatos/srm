package org.siemac.metamac.internal.web.server.handlers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DataStructureDefinitionDto;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.DeleteDsdListAction;
import org.siemac.metamac.internal.web.shared.DeleteDsdListResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteDsdListActionHandler extends AbstractActionHandler<DeleteDsdListAction, DeleteDsdListResult> {

    private static Logger        logger = Logger.getLogger(DeleteDsdListActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDsdListActionHandler() {
        super(DeleteDsdListAction.class);
    }

    @Override
    public DeleteDsdListResult execute(DeleteDsdListAction action, ExecutionContext context) throws ActionException {
        List<DataStructureDefinitionDto> dataStructureDefinitionDtos = action.getDataStructureDefinitionDtos();
        for (DataStructureDefinitionDto dsd : dataStructureDefinitionDtos) {
            try {
                srmCoreServiceFacade.deleteDsd(ServiceContextHelper.getServiceContext(), dsd);
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, " Error deleting dsd. " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteDsdListResult();
    }

    @Override
    public void undo(DeleteDsdListAction action, DeleteDsdListResult result, ExecutionContext context) throws ActionException {

    }

}
