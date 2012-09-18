package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveDescriptorForDsdActionHandler extends SecurityActionHandler<SaveDescriptorForDsdAction, SaveDescriptorForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveDescriptorForDsdActionHandler() {
        super(SaveDescriptorForDsdAction.class);
    }

    @Override
    public SaveDescriptorForDsdResult executeSecurityAction(SaveDescriptorForDsdAction action) throws ActionException {
        try {
            DescriptorDto descriptorDto = srmCoreServiceFacade.saveDescriptorForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), action.getDescriptorDto());
            return new SaveDescriptorForDsdResult(descriptorDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(SaveDescriptorForDsdAction action, SaveDescriptorForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
