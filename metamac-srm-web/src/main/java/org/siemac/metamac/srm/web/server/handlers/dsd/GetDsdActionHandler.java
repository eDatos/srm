package org.siemac.metamac.srm.web.server.handlers.dsd;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetDsdActionHandler extends SecurityActionHandler<GetDsdAction, GetDsdResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetDsdActionHandler() {
        super(GetDsdAction.class);
    }

    @Override
    public GetDsdResult executeSecurityAction(GetDsdAction action) throws ActionException {
        try {
            DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDsdByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetDsdResult(dataStructureDefinitionMetamacDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDsdAction action, GetDsdResult result, ExecutionContext context) throws ActionException {

    }

}
