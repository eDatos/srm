package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.AddVariablesToVariableFamilyAction;
import org.siemac.metamac.srm.web.shared.code.AddVariablesToVariableFamilyResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class AddVariablesToVariableFamilyActionHandler extends SecurityActionHandler<AddVariablesToVariableFamilyAction, AddVariablesToVariableFamilyResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public AddVariablesToVariableFamilyActionHandler() {
        super(AddVariablesToVariableFamilyAction.class);
    }

    @Override
    public AddVariablesToVariableFamilyResult executeSecurityAction(AddVariablesToVariableFamilyAction action) throws ActionException {
        try {
            srmCoreServiceFacade.addVariablesToVariableFamily(ServiceContextHolder.getCurrentServiceContext(), action.getVariableUrns(), action.getVariableFamilyUrn());
            return new AddVariablesToVariableFamilyResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
