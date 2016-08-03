package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodeParentResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodeParentActionHandler extends SecurityActionHandler<UpdateCodeParentAction, UpdateCodeParentResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodeParentActionHandler() {
        super(UpdateCodeParentAction.class);
    }

    @Override
    public UpdateCodeParentResult executeSecurityAction(UpdateCodeParentAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCodeParent(ServiceContextHolder.getCurrentServiceContext(), action.getCodeUrn(), action.getNewParentUrn());
            return new UpdateCodeParentResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
