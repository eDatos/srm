package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeInOrderResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodeInOrderActionHandler extends SecurityActionHandler<UpdateCodeInOrderAction, UpdateCodeInOrderResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodeInOrderActionHandler() {
        super(UpdateCodeInOrderAction.class);
    }

    @Override
    public UpdateCodeInOrderResult executeSecurityAction(UpdateCodeInOrderAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCodeInOrderVisualisation(ServiceContextHolder.getCurrentServiceContext(), action.getCodeUrn(), action.getCodelistOrderIdentifier(), action.getNewCodeIndex());
            return new UpdateCodeInOrderResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
