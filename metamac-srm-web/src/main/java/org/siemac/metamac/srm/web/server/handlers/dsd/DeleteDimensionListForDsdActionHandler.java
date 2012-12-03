package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDimensionListForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteDimensionListForDsdActionHandler extends SecurityActionHandler<DeleteDimensionListForDsdAction, DeleteDimensionListForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteDimensionListForDsdActionHandler() {
        super(DeleteDimensionListForDsdAction.class);
    }

    @Override
    public DeleteDimensionListForDsdResult executeSecurityAction(DeleteDimensionListForDsdAction action) throws ActionException {
        List<DimensionComponentDto> dimensionComponentDtos = action.getDimensionComponentDtos();
        for (DimensionComponentDto d : dimensionComponentDtos) {
            // TODO pendiente error compilación jenkins
//            try {
//                srmCoreServiceFacade.deleteComponentForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), d, action.getTypeComponentList());
//            } catch (MetamacException e) {
//                throw WebExceptionUtils.createMetamacWebException(e);
//            }
        }
        return new DeleteDimensionListForDsdResult();
    }

    @Override
    public void undo(DeleteDimensionListForDsdAction action, DeleteDimensionListForDsdResult result, ExecutionContext context) throws ActionException {

    }

}
