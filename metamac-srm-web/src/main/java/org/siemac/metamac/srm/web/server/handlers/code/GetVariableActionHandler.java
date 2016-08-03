package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.GetVariableAction;
import org.siemac.metamac.srm.web.shared.code.GetVariableResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class GetVariableActionHandler extends SecurityActionHandler<GetVariableAction, GetVariableResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public GetVariableActionHandler() {
        super(GetVariableAction.class);
    }

    @Override
    public GetVariableResult executeSecurityAction(GetVariableAction action) throws ActionException {
        try {
            VariableDto variableDto = srmCoreServiceFacade.retrieveVariableByUrn(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new GetVariableResult(variableDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
