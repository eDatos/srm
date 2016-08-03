package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariableElementActionHandler extends SecurityActionHandler<GetVariableElementAction, GetVariableElementResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariableElementActionHandler() {
        super(GetVariableElementAction.class);
    }

    @Override
    public GetVariableElementResult executeSecurityAction(GetVariableElementAction action) throws ActionException {
        try {
            VariableElementDto variableElementDto = srmCoreServiceFacade.retrieveVariableElementByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetVariableElementResult(variableElementDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
