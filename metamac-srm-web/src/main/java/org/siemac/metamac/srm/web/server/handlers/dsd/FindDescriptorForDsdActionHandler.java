package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.FindDescriptorForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class FindDescriptorForDsdActionHandler extends SecurityActionHandler<FindDescriptorForDsdAction, FindDescriptorForDsdResult> {

    private static Logger        logger = Logger.getLogger(FindDescriptorForDsdActionHandler.class.getName());

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public FindDescriptorForDsdActionHandler() {
        super(FindDescriptorForDsdAction.class);
    }

    @Override
    public FindDescriptorForDsdResult executeSecurityAction(FindDescriptorForDsdAction action) throws ActionException {
        try {
            List<DescriptorDto> descriptorDtos = srmCoreServiceFacade.findDescriptorForDsd(ServiceContextHolder.getCurrentServiceContext(), action.getIdDsd(), action.getTypeComponentList());
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
