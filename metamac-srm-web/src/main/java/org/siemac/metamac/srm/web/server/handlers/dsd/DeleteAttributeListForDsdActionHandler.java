package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributeListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributeListForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteAttributeListForDsdActionHandler extends SecurityActionHandler<DeleteAttributeListForDsdAction, DeleteAttributeListForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteAttributeListForDsdActionHandler() {
        super(DeleteAttributeListForDsdAction.class);
    }

    @Override
    public DeleteAttributeListForDsdResult executeSecurityAction(DeleteAttributeListForDsdAction action) throws ActionException {
        List<DataAttributeDto> dataAttributeDtos = action.getDataAttributeDtos();
        for (DataAttributeDto a : dataAttributeDtos) {
            try {
                srmCoreServiceFacade.deleteComponentForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), a, action.getTypeComponentList());
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteAttributeListForDsdResult();
    }

    @Override
    public void undo(DeleteAttributeListForDsdAction action, DeleteAttributeListForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
