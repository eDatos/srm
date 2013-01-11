package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableFamiliesResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteVariableFamiliesActionHandler extends SecurityActionHandler<DeleteVariableFamiliesAction, DeleteVariableFamiliesResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteVariableFamiliesActionHandler() {
        super(DeleteVariableFamiliesAction.class);
    }

    @Override
    public DeleteVariableFamiliesResult executeSecurityAction(DeleteVariableFamiliesAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteVariableFamily(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteVariableFamiliesResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
