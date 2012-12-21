package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodeResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCodeActionHandler extends SecurityActionHandler<DeleteCodeAction, DeleteCodeResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCodeActionHandler() {
        super(DeleteCodeAction.class);
    }

    @Override
    public DeleteCodeResult executeSecurityAction(DeleteCodeAction action) throws ActionException {
        try {
            srmCoreServiceFacade.deleteCode(ServiceContextHolder.getCurrentServiceContext(), action.getUrn());
            return new DeleteCodeResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
