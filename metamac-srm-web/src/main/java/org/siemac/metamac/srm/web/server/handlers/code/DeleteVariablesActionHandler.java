package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariablesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteVariablesActionHandler extends SecurityActionHandler<DeleteVariablesAction, DeleteVariablesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteVariablesActionHandler() {
        super(DeleteVariablesAction.class);
    }

    @Override
    public DeleteVariablesResult executeSecurityAction(DeleteVariablesAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteVariable(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteVariablesResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
