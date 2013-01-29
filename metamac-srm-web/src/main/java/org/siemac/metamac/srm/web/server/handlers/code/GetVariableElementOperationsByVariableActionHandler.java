package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariableElementOperationsByVariableActionHandler extends SecurityActionHandler<GetVariableElementOperationsByVariableAction, GetVariableElementOperationsByVariableResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariableElementOperationsByVariableActionHandler() {
        super(GetVariableElementOperationsByVariableAction.class);
    }

    @Override
    public GetVariableElementOperationsByVariableResult executeSecurityAction(GetVariableElementOperationsByVariableAction action) throws ActionException {
        try {
            List<VariableElementOperationDto> operations = srmCoreServiceFacade.retrieveVariableElementsOperationsByVariable(ServiceContextHolder.getCurrentServiceContext(), action.getVariableUrn());
            return new GetVariableElementOperationsByVariableResult(operations);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
