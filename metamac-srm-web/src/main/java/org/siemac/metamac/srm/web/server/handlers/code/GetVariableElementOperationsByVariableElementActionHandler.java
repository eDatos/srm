package org.siemac.metamac.srm.web.server.handlers.code;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementOperationsByVariableElementResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariableElementOperationsByVariableElementActionHandler
        extends
            SecurityActionHandler<GetVariableElementOperationsByVariableElementAction, GetVariableElementOperationsByVariableElementResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariableElementOperationsByVariableElementActionHandler() {
        super(GetVariableElementOperationsByVariableElementAction.class);
    }

    @Override
    public GetVariableElementOperationsByVariableElementResult executeSecurityAction(GetVariableElementOperationsByVariableElementAction action) throws ActionException {
        try {
            List<VariableElementOperationDto> operations = srmCoreServiceFacade.retrieveVariableElementsOperationsByVariableElement(ServiceContextHolder.getCurrentServiceContext(),
                    action.getVariableElementUrn());
            return new GetVariableElementOperationsByVariableElementResult(operations);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
