package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.RemoveVariablesFromVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.RemoveVariablesFromVariableFamilyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class RemoveVariablesFromVariableFamilyActionHandler extends SecurityActionHandler<RemoveVariablesFromVariableFamilyAction, RemoveVariablesFromVariableFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public RemoveVariablesFromVariableFamilyActionHandler() {
        super(RemoveVariablesFromVariableFamilyAction.class);
    }

    @Override
    public RemoveVariablesFromVariableFamilyResult executeSecurityAction(RemoveVariablesFromVariableFamilyAction action) throws ActionException {
        try {
            for (String urn : action.getVariableUrns()) {
                srmCoreServiceFacade.removeVariableFromVariableFamily(ServiceContextHolder.getCurrentServiceContext(), urn, action.getVariableFamilyUrn());
            }
            return new RemoveVariablesFromVariableFamilyResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
