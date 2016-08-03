package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.AddVariableElementsToVariableAction;
import org.siemac.metamac.srm.web.shared.code.AddVariableElementsToVariableResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class AddVariableElementsToVariableActionHandler extends SecurityActionHandler<AddVariableElementsToVariableAction, AddVariableElementsToVariableResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public AddVariableElementsToVariableActionHandler() {
        super(AddVariableElementsToVariableAction.class);
    }

    @Override
    public AddVariableElementsToVariableResult executeSecurityAction(AddVariableElementsToVariableAction action) throws ActionException {
        try {
            srmCoreServiceFacade.addVariableElementsToVariable(ServiceContextHolder.getCurrentServiceContext(), action.getVariableElementUrns(), action.getVariableUrn());
            return new AddVariableElementsToVariableResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
