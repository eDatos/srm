package org.siemac.metamac.internal.web.server.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core_facades.serviceapi.SDMXStructureServiceFacade;
import org.siemac.metamac.domain_dto.DescriptorDto;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.SaveDescriptorForDsdAction;
import org.siemac.metamac.internal.web.shared.SaveDescriptorForDsdResult;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SaveDescriptorForDsdActionHandler extends AbstractActionHandler<SaveDescriptorForDsdAction, SaveDescriptorForDsdResult> {

    private static Logger              logger = Logger.getLogger(SaveDescriptorForDsdActionHandler.class.getName());

    @Autowired
    private SDMXStructureServiceFacade sDMXStructureServiceFacade;

    public SaveDescriptorForDsdActionHandler() {
        super(SaveDescriptorForDsdAction.class);
    }

    @Override
    public SaveDescriptorForDsdResult execute(SaveDescriptorForDsdAction action, ExecutionContext context) throws ActionException {
        try {
            DescriptorDto descriptorDto = sDMXStructureServiceFacade.saveDescriptorForDsd(ServiceContextHelper.getServiceContext(), action.getIdDsd(), action.getDescriptorDto());
            return new SaveDescriptorForDsdResult(descriptorDto);
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, " Error saving descriptor for DSD with id = " + action.getIdDsd() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveDescriptorForDsdAction action, SaveDescriptorForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
