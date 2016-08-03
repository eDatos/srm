package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesVariableElementsAction;
import org.siemac.metamac.srm.web.shared.code.UpdateCodesVariableElementsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class UpdateCodesVariableElementsActionHandler extends SecurityActionHandler<UpdateCodesVariableElementsAction, UpdateCodesVariableElementsResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public UpdateCodesVariableElementsActionHandler() {
        super(UpdateCodesVariableElementsAction.class);
    }

    @Override
    public UpdateCodesVariableElementsResult executeSecurityAction(UpdateCodesVariableElementsAction action) throws ActionException {
        try {
            srmCoreServiceFacade.updateCodesVariableElements(ServiceContextHolder.getCurrentServiceContext(), action.getCodelistUrn(), action.getVariableElementsIdByCodeId());
            return new UpdateCodesVariableElementsResult();
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
