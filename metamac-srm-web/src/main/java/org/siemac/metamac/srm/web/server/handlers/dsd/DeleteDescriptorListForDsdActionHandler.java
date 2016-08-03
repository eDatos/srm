package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDescriptorListForDsdActionHandler extends SecurityActionHandler<DeleteDescriptorListForDsdAction, DeleteDescriptorListForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDescriptorListForDsdActionHandler() {
        super(DeleteDescriptorListForDsdAction.class);
    }

    @Override
    public DeleteDescriptorListForDsdResult executeSecurityAction(DeleteDescriptorListForDsdAction action) throws ActionException {
        List<DescriptorDto> descriptorsToDelete = action.getDescriptorDtos();
        for (DescriptorDto d : descriptorsToDelete) {
            try {
                srmCoreServiceFacade.deleteDescriptorForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), d);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteDescriptorListForDsdResult();
    }

    @Override
    public void undo(DeleteDescriptorListForDsdAction action, DeleteDescriptorListForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
