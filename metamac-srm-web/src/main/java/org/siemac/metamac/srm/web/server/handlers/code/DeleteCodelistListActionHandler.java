package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistListAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistListResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCodelistListActionHandler extends SecurityActionHandler<DeleteCodelistListAction, DeleteCodelistListResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCodelistListActionHandler() {
        super(DeleteCodelistListAction.class);
    }

    @Override
    public DeleteCodelistListResult executeSecurityAction(DeleteCodelistListAction action) throws ActionException {
        try {
            for (String urn : action.getUrns()) {
                srmCoreServiceFacade.deleteCodelist(ServiceContextHolder.getCurrentServiceContext(), urn);
            }
            return new DeleteCodelistListResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
