package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersAction;
import org.siemac.metamac.srm.web.shared.code.DeleteCodelistOrdersResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class DeleteCodelistOrdersActionHandler extends SecurityActionHandler<DeleteCodelistOrdersAction, DeleteCodelistOrdersResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public DeleteCodelistOrdersActionHandler() {
        super(DeleteCodelistOrdersAction.class);
    }

    @Override
    public DeleteCodelistOrdersResult executeSecurityAction(DeleteCodelistOrdersAction action) throws ActionException {
        try {
            for (String orderIdentifier : action.getOrderIdentifiers()) {
                srmCoreServiceFacade.deleteCodelistOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), orderIdentifier);
            }
            return new DeleteCodelistOrdersResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
