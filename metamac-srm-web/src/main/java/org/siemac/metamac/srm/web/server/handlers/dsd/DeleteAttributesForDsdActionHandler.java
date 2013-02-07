package org.siemac.metamac.srm.web.server.handlers.dsd;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributesForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteAttributesForDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteAttributesForDsdActionHandler extends SecurityActionHandler<DeleteAttributesForDsdAction, DeleteAttributesForDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteAttributesForDsdActionHandler() {
        super(DeleteAttributesForDsdAction.class);
    }

    @Override
    public DeleteAttributesForDsdResult executeSecurityAction(DeleteAttributesForDsdAction action) throws ActionException {
        List<DataAttributeDto> dataAttributeDtos = action.getDataAttributeDtos();
        for (DataAttributeDto a : dataAttributeDtos) {
            try {
                srmCoreServiceFacade.deleteComponentForDataStructureDefinition(ServiceContextHolder.getCurrentServiceContext(), action.getDsdUrn(), a);
            } catch (MetamacException e) {
                throw WebExceptionUtils.createMetamacWebException(e);
            }
        }
        return new DeleteAttributesForDsdResult();
    }
}
