package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsAction;
import org.siemac.metamac.srm.web.shared.code.DeleteVariableElementOperationsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteVariableElementOperationsActionHandler extends SecurityActionHandler<DeleteVariableElementOperationsAction, DeleteVariableElementOperationsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteVariableElementOperationsActionHandler() {
        super(DeleteVariableElementOperationsAction.class);
    }

    @Override
    public DeleteVariableElementOperationsResult executeSecurityAction(DeleteVariableElementOperationsAction action) throws ActionException {
        try {
            for (String code : action.getCodes()) {
                srmCoreServiceFacade.deleteVariableElementOperation(ServiceContextHolder.getCurrentServiceContext(), code);
            }
            return new DeleteVariableElementOperationsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
