package org.siemac.metamac.srm.web.server.handlers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.srm.web.server.ServiceContextHelper;
import org.siemac.metamac.srm.web.shared.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.DeleteDescriptorListForDsdResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteDescriptorListForDsdActionHandler extends AbstractActionHandler<DeleteDescriptorListForDsdAction, DeleteDescriptorListForDsdResult> {

    private static Logger        logger = Logger.getLogger(DeleteDescriptorListForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDescriptorListForDsdActionHandler() {
        super(DeleteDescriptorListForDsdAction.class);
    }

    @Override
    public DeleteDescriptorListForDsdResult execute(DeleteDescriptorListForDsdAction action, ExecutionContext context) throws ActionException {
        List<DescriptorDto> descriptorsToDelete = action.getDescriptorDtos();
        for (DescriptorDto d : descriptorsToDelete) {
            try {
                srmCoreServiceFacade.deleteDescriptorForDsd(ServiceContextHelper.getServiceContext(), action.getIdDsd(), d);
            } catch (MetamacException e) {
                logger.log(Level.SEVERE, " Error deleting descriptor for DSD with id = " + action.getIdDsd() + ". " + e.getMessage());
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteDescriptorListForDsdResult();
    }

    @Override
    public void undo(DeleteDescriptorListForDsdAction action, DeleteDescriptorListForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
