package org.siemac.metamac.internal.web.server.handlers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.domain.srm.dto.DescriptorDto;
import org.siemac.metamac.internal.web.server.ServiceContextHelper;
import org.siemac.metamac.internal.web.shared.FindDescriptorForDsdAction;
import org.siemac.metamac.internal.web.shared.FindDescriptorForDsdResult;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class FindDescriptorForDsdActionHandler extends AbstractActionHandler<FindDescriptorForDsdAction, FindDescriptorForDsdResult> {

    private static Logger        logger = Logger.getLogger(FindDescriptorForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindDescriptorForDsdActionHandler() {
        super(FindDescriptorForDsdAction.class);
    }

    @Override
    public FindDescriptorForDsdResult execute(FindDescriptorForDsdAction action, ExecutionContext context) throws ActionException {
        try {
            List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(ServiceContextHelper.getServiceContext(), action.getIdDsd(), action.getTypeComponentList());
            logger.log(Level.INFO, "ACTION SUCCESSFULLY: findDescriptorForDsd");
            return new FindDescriptorForDsdResult(descriptorDtos);
        } catch (MetamacException e) {
            logger.log(Level.SEVERE, "Error in findDescriptorForDsd with idDsd =  " + action.getIdDsd() + " and typeComponentList = " + action.getTypeComponentList() + ". " + e.getMessage());
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(FindDescriptorForDsdAction action, FindDescriptorForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
